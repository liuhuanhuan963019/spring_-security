package com.nuist.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author liuhuanhuan
 * @version 1.0
 * @date 2022/5/5 21:37
 * @Description
 */
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    // 授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人都可以访问，功能也只有对应权限的人才可以进行访问
        //请求授权的规则
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        // 没有权限就请求到login页面
        http.formLogin()
                .loginPage("/toLoginForm")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login");


        // 防止网站工具
        http.csrf().disable();  // 关闭csrf功能
        //注销，开启注销功能 退出之后去首页
        http.logout().logoutSuccessUrl("/");

        // 开启记住我的功能 cookie  默认保存2周
        http.rememberMe().rememberMeParameter("remember");
    }

    // 认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 从内存中获取数据
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("lhh").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and().withUser("test").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2");
        // 从数据库中获取数据
//        auth.jdbcAuthentication().dataSource().withUser();
    }
}
