package com.tweetapp.security.config;

import com.tweetapp.constants.Constants;
import com.tweetapp.exception.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import com.tweetapp.security.filter.JwtRequestFilter;
import com.tweetapp.security.logout.JWTLogoutSuccessHandler;
import com.tweetapp.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SpringSecurityWithJWTConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${jwt.get.token.uri}")
    private String authenticationPath;

    private MyUserDetailsService myUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;
    private JwtUnAuthorizedResponseAuthenticationEntryPoint entryPoint;
    private JWTLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Autowired
    public SpringSecurityWithJWTConfiguration(final MyUserDetailsService myUserDetailsService,
                                              final JwtRequestFilter jwtRequestFilter,
                                              final JwtUnAuthorizedResponseAuthenticationEntryPoint entryPoint,
                                              final JWTLogoutSuccessHandler jwtLogoutSuccessHandler) {

        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.entryPoint = entryPoint;
        this.jwtLogoutSuccessHandler = jwtLogoutSuccessHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService) // without no encode password
        .passwordEncoder(encodePWD()); // with encode password
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(authenticationPath).permitAll()
                .antMatchers(Constants.FORGOT_PASSWORD_PATH).permitAll()
                .antMatchers(Constants.RESET_PASSWORD_PATH).permitAll()
                .antMatchers(Constants.USER_REGISTER).permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/h2-console/**/**").permitAll()
                .anyRequest().authenticated();

       // http.headers().frameOptions().disable();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**");

    }

    @Bean
    public PasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public PasswordEncoder encodePWD() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*")
                        .allowedOrigins("*").allowedMethods("*");
            }
        };
    }
}