package personal.ch.trainbookingsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置没有访问权限的自定义画面
        http.exceptionHandling().accessDeniedPage("/unauth.html");

        http.authorizeRequests()
                .antMatchers("/toLogin", "/toRegister", "/register", "/", "/index","/css/**","/js/**","/bootstrap/**","/less/**","/plugins/**").permitAll()
//                .antMatchers("/admin/**","/css/**").hasAuthority("admin")
//                .antMatchers("/adminIndex").hasAuthority("ADMIN")
//                .antMatchers("/hello/hello/**").hasAuthority("USER")
//                .antMatchers("/unauth.html").hasAuthority("ADMIN")
                .antMatchers("/adminIndex", "/admin/**").hasRole("ADMIN")
                .anyRequest().fullyAuthenticated();

        http.formLogin()
                .loginPage("/toLogin")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index").successHandler(authenticationSuccessHandler)
                .and()
//                .rememberMe().rememberMeParameter("remember")
//                .and()
                .logout().logoutSuccessUrl("/index")
                .and()
                .sessionManagement()
                .invalidSessionUrl("/index").maximumSessions(1)
                .and()
                .and()
                .csrf().disable()
                .logout().logoutSuccessUrl("/index");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
        auth.authenticationProvider(authenticationProvider());

//        auth.inMemoryAuthentication()
//                .withUser("user").password(new BCryptPasswordEncoder().encode("123456")).roles("USER")
//                .and()
//                .withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN");
    }

    /**
     * BCryptPasswordEncoder 是 PasswordEncoder的接口实现
     * 密码加密
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 不隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        // 设置自定义认证方式，用户登录认证
        provider.setUserDetailsService(userDetailService);
        // 设置密码加密程序认证
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
