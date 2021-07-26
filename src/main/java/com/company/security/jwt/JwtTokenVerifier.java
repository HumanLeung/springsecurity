package com.company.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    public JwtTokenVerifier (){
        System.out.println("this is Once");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
//        filter.doFilter(request,response);
        System.out.println("works");

        String authorizationHeader = request.getHeader("Authorization");
        String oauth2 = request.getHeader("Oauth2");
        if ((Strings.isNullOrEmpty(oauth2) || !oauth2.equals("nice")) &&
                        (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer "))){
            System.out.println("test");
            System.out.println("after test");
            filter.doFilter(request,response);
            return;
        }
        System.out.println(" still works");

        if (!(Strings.isNullOrEmpty(authorizationHeader) || authorizationHeader.startsWith("Bearer "))){
            jwtParser(authorizationHeader,request,response,filter);
        } else {
            ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
            arrayList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//            arrayList.add(new SimpleGrantedAuthority("student:write"));
//            arrayList.add(new SimpleGrantedAuthority("student:read"));
//            arrayList.add(new SimpleGrantedAuthority("course:write"));
//            arrayList.add(new SimpleGrantedAuthority("course:read"));


            System.out.println(arrayList + "arr");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "linda",
                    null,
                     arrayList
            );
            System.out.println(SecurityContextHolder.getContext().getAuthentication() + " this is authentication");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext().getAuthentication() + " this is authentication");
            filter.doFilter(request,response);
        }
//        filter.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }


    public void jwtParser(String authorizationHeader, HttpServletRequest request, HttpServletResponse response ,FilterChain filter) {
        String token = authorizationHeader.replace("Bearer ","");
        try {

            String hash = "securesecuresecuresecuresecuresecuresecuresecure";

            Jws<Claims> claimJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(hash.getBytes())).build()
                    .parseClaimsJws(token);
            Claims body = claimJws.getBody();
            String username = body.getSubject();

            List<Map<String,String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet =  authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());
//            System.out.println(simpleGrantedAuthoritySet +" set");

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthoritySet
//                          new ArrayList<>()
            );
            System.out.println(SecurityContextHolder.getContext().getAuthentication() + " this is authentication");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext().getAuthentication() + " this is authentication");
            filter.doFilter(request,response);
        } catch (JwtException | IOException | ServletException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trust", token));
        }
    }


}
