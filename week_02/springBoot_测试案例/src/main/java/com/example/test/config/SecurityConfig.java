package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    // 注入 PasswordEncoder 类到 spring 容器中
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/layui/**","/index") //表示配置请求路径
//                .permitAll() // 指定 URL 无需保护。
//                .anyRequest() // 其他请求
//                .authenticated(); //需要认证
//    }}
