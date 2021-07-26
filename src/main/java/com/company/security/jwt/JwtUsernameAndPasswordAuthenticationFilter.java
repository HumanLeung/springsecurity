package com.company.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


//@Component
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {



        private final AuthenticationManager authenticationManager;

        public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
//        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
//        setUsernameParameter("email");
            System.out.println("passwordFilter");
    }

//    public JwtUsernameAndPasswordAuthenticationFilter() {
//       super.setAuthenticationManager(authenticationManager);
//        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
//        System.out.println(super.obtainPassword(request)+ "obtain");
//        System.out.println(request.getParameter("password"));

        System.out.println("attempt is working "+this.getClass().getName());
        System.out.println(SecurityContextHolder.getContext());

//        AbstractAuthenticationProcessingFilter
        try {
//            UsernameAndPasswordAuthenticationRequest authenticationRequest
//             = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            EmailPassAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), EmailPassAuthenticationRequest.class);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
              authenticationRequest.getEmail(),
              authenticationRequest.getPassword()
      );


            System.out.println(authenticationManager.getClass().getName()+" this is Rest test");
           Authentication authenticate = authenticationManager.authenticate(authentication);
            System.out.println(authenticate.isAuthenticated()+" this is authenticate");
            System.out.println(authenticationManager.getClass().getName()+" this is Rest test");
//            System.out.println(authenticationManager.getClass().getName() +" this is authenticate");
            System.out.println(SecurityContextHolder.getContext()+" this is Holder");
            System.out.println(authenticate+" this is authenticate");
       return authenticate;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("filter is works");
//        chain.doFilter(request, response);
//    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successful is working");

        System.out.println(request.getInputStream().available()+" request ");

        String hash = "securesecuresecuresecuresecuresecuresecuresecure";
        System.out.println("this is continue");
        System.out.println(authResult.getCredentials()+"+"+authResult.getPrincipal().toString()+"+"+authResult.getDetails()+"+"+authResult.getName());
       String token = Jwts.builder().setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(hash.getBytes()))
                .compact();
        System.out.println(SecurityContextHolder.getContext().getAuthentication()+" this is Holder2");

        response.addHeader("Authorization", "Bearer "+ token);
    }
}

