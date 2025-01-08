package com.kirillpolyakov.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirillpolyakov.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private ObjectMapper objectMapper;

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user").authenticated()
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/apprentice/getAll").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/apprentice/get/**").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/apprentice/update").hasAnyRole("ADMIN, APPRENTICE")
                .antMatchers("/apprentice/update/password").hasAnyRole("ADMIN, APPRENTICE")
                .antMatchers("/apprentice/delete/**").hasAnyRole("ADMIN, APPRENTICE")
                .antMatchers("/trainer/add").hasRole("ADMIN")
                .antMatchers("/trainer/getAll").hasAnyRole("ADMIN", "APPRENTICE")
                .antMatchers("/trainer/get/**").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/trainer/update").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/trainer/update/password").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/trainer/delete/**").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/trainer_schedule/add/**").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/trainer_schedule/delete/**").hasAnyRole("ADMIN, TRAINER")
                .antMatchers("/training/trainer/**").hasAnyRole("ADMIN, TRAINER")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    request.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    response.setCharacterEncoding("utf-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    objectMapper.writeValue(response.getWriter(),
                            new ResponseResult<>("No authentication", null));
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    request.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    response.setCharacterEncoding("utf-8");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    objectMapper.writeValue(response.getWriter(),
                            new ResponseResult<>("Forbidden", null));
                })
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
