package com.company.security.config;

import com.company.security.auth.ApplicationUserService;
import com.company.security.jwt.JwtTokenVerifier;
import com.company.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.company.security.jwt.TestFilter;
import com.company.security.test.Test1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.company.security.config.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//   private JwtUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter;

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService){
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        System.out.println(ADMIN.name() + "this is admin");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // authorizeRequests() Allows restricting access based
        // upon the HttpServletRequest using RequestMatcher
        // implementations.
        // X-XSRF-TOKEN
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
//                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter())
                .addFilter(getAuthenticationA())
//                .addFilter(jwtUsernameAndPasswordAuthenticationFilter)
//                .addFilterAfter(new TestFilter(authenticationManager()),JwtUsernameAndPasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenVerifier(),JwtUsernameAndPasswordAuthenticationFilter.class)
//                .addFilter(new TestFilter())
                .authorizeRequests()
                .antMatchers("/","index,","/css/*","/js/*")
                .permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.GET,"/rest/**").hasAnyRole(ADMIN.name())
                //Matchers is go through one by one, if one is block it will return false and not going to check next matchers
                .antMatchers("/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())


//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())


                // spring security's configuration http.anyRequest().authenticated() is
                // that any request must be authenticated otherwise my Spring app will
                // return a 401 response.
                .anyRequest()
                .authenticated();
//                .and()
//                .httpBasic();
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/courses", true).permitAll()
//                .passwordParameter("password")
//                .usernameParameter("username")
//                .and()
//                .rememberMe().tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
//                .key("somethingverysecured")
//                .rememberMeParameter("remember-me")
//                 .and()
//                  .logout().
//                   logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login");
//                .loginProcessingUrl("/processCourse").permitAll();
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails annaSmithUser = User.builder()
//                .username("annaSmith")
//                .password(passwordEncoder.encode("password"))
//                //have to be encoded
//                // java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
//     //           .roles(STUDENT.name()) // ROLE_STUDENT
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//       UserDetails lindaUser =  User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password"))
//     //           .roles(ADMIN.name())
//               .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomUser =  User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password"))
//    //            .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                annaSmithUser,
//                lindaUser,
//                tomUser
//
//        );

    @Autowired
    Test1 test1;



    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        System.out.println(test1+"test1");
        return super.authenticationManagerBean();
//        return authenticationManager();
    }

    @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
//    auth.userDetailsService(applicationUserService);
}


  public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
       return provider;
    }

   public  UsernamePasswordAuthenticationFilter getAuthenticationA() throws Exception {
       //        filter.setAuthenticationManager(authenticationManager());
        return new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager());
    }
}
