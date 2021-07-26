package com.company.security.jwt;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TestFilter extends OncePerRequestFilter{


    public TestFilter() {
        System.out.println("annomys");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("annomys");
        System.out.println(request.getHeader("Oauth2"));
        if (request.getHeader("Oauth2") != null){
            if (!request.getHeader("Oauth2").equals("nice")){
                filterChain.doFilter(request,response);
                return ;
            }
            ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
            arrayList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            Authentication authentication = new Oauth2Token(
                    "linda",
                    null,
                    arrayList
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request,response);
        }


//        return;
    }
}
