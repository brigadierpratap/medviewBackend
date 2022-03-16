package com.app.medview.filter;

import com.app.medview.service.DoctorDetailService;
import com.app.medview.service.MyUserDetailService;
import com.app.medview.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component@Slf4j@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final MyUserDetailService myUserDetailService;

    private final DoctorDetailService doctorDetailService;

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {



            final String authHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    jwt = authHeader.substring(7);
                    username = jwtUtil.extractUsername(jwt);

                    if (username == null) {
                        request.setAttribute("exception", new Exception("Invalid Token"));
                        response.sendRedirect("/error");
                    }
                } catch (ExpiredJwtException e) {
                    request.setAttribute("exception", e);


                    //System.out.println("JWT Token has expired");
                    e.printStackTrace();
                }


            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtUtil.extractAllClaims(jwt);
                log.warn(claims.toString());

                UserDetails userDetails = null;
                if(claims.get("roles").equals("DT"))
                    userDetails = doctorDetailService.loadUserByUsername(username);
                else userDetails = myUserDetailService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
            filterChain.doFilter(request, response);



    }
}