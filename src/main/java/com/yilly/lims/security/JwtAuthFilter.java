package com.yilly.lims.security;

import com.yilly.lims.entity.Operator;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final OperatorRepository operatorRepo;
    private final AuthService authService; // 用于校验黑名单（登出失效）

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // 1) 黑名单（已登出）
            if (authService.isBlacklisted(token)) {
                write401(res, "Token revoked");
                return;
            }

            try {
                // 2) 解析并验签
                Jws<Claims> jws = jwtUtil.parse(token);
                String username = jws.getBody().getSubject();

                // 已认证则跳过
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 3) 查用户与权限
                    Operator op = operatorRepo.findByUsername(username).orElse(null);
                    if (op == null) {
                        write401(res, "User not found");
                        return;
                    }
                    List<GrantedAuthority> authorities =
                            (op.getRole() == null || op.getRole().getPermissions() == null)
                                    ? List.of()
                                    : op.getRole().getPermissions().stream()
                                    .map(p -> (GrantedAuthority) new SimpleGrantedAuthority(p.getPermissionName()))
                                    .toList();

                    // 4) 放入 SecurityContext
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) {
                // token 过期/签名不对/格式错误 → 401 JSON
                write401(res, "TOKEN_INVALID_OR_EXPIRED");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private void write401(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        res.getWriter().write("{\"message\":\"" + msg + "\"}");
    }
}
