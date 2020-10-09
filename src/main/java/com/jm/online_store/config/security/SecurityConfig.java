package com.jm.online_store.config.security;

import com.jm.online_store.config.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler successHandler;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    @Setter
    private OAuth2UserService OAuth2UserService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(OAuth2UserService)
                .and().and()
                .authorizeRequests()
                .antMatchers("/customer").hasRole("CUSTOMER");

        http.formLogin()
                .loginPage("/login") // указываем страницу с формой логина
                .successHandler(successHandler) //указываем логику обработки при логине
                .loginProcessingUrl("/login")// указываем action с формы логина
                .usernameParameter("login_username") // Указываем параметры логина и пароля с формы логина
                .passwordParameter("login_password")
                .permitAll(); // даем доступ к форме логина всем

        http.logout().permitAll() // разрешаем делать логаут всем
                .logoutSuccessUrl("/") // указываем URL при удачном логауте
                .and().csrf().disable();

        http.authorizeRequests()
                .antMatchers("/api/feedback/").access("hasRole('ROLE_CUSTOMER')")
                .antMatchers("/api/products/productChangeMonitor", "/manager/**").access("hasRole('ROLE_MANAGER')")
                .antMatchers("/customer/**").access("hasAnyRole('ROLE_CUSTOMER','ROLE_ADMIN')")
                .antMatchers("/authority/**", "/api/commonSettings/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
                .antMatchers("/admin/**", "/api/users/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/denied")
                .and()
                .rememberMe();
    }

    @Bean
    public AuthenticationManager authManager() throws Exception {
        return this.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
