package com.example.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {
    // 注入 PasswordEncoder 类到 spring 容器中
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder password(){
        return new BCryptPasswordEncoder();
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(password());
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
// 配置认证
        http.formLogin()
                .loginPage("/login.html") // 配置哪个 url 为登录页面
                .loginProcessingUrl("/user/login") // 设置哪个是登录的 url。
                .defaultSuccessUrl("/test/index").permitAll() // 登录成功之后跳转到哪个 url
                .failureForwardUrl("/error");// 登录失败之后跳转到哪个 url
        http.authorizeRequests()
                .antMatchers("/","/test/hello","/user/login").permitAll().anyRequest().authenticated(); //需要认证
        http.csrf().disable();
    }
}