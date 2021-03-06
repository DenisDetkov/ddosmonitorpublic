package com.ddos.java.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${application.security.admin.username}")
    private String adminUsername;

    @Value("${application.security.admin.password}")
    private String adminPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/admin/verifying").hasAnyRole("MODERATOR")
                .anyRequest().permitAll()
                .and()
                .formLogin().and().cors().and().csrf().ignoringAntMatchers("/global/*");
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password("{noop}"+adminPassword)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
