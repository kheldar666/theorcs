package org.libermundi.theorcs.configuration;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.repositories.security.RememberMeTokenRepository;
import org.libermundi.theorcs.repositories.security.impl.PersistentTokenRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${theorcs.security.rememberme.key}")
    private String rememberMeKey;

    @Value("${spring.profiles.active}")
    private String env;

    private UserDetailsService userDetailsService;

    private RememberMeTokenRepository rememberMeTokenRepository;

    public SecurityConfiguration(@Qualifier("UserDetailsService") UserDetailsService userDetailsService,
                                 RememberMeTokenRepository rememberMeTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.rememberMeTokenRepository = rememberMeTokenRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        log.info("Configuring WebSecurity");
        web
            .ignoring()
            .antMatchers("/webjars/**","/js/**","/images/**","/css/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring HttpSecurity");
        httpSecurity
                .headers()
                .cacheControl();

        httpSecurity
                .authorizeRequests()
                    .expressionHandler(securityExpressionHandler())
                        .antMatchers("/*","/h2-console/**")
                            .permitAll()
                        .antMatchers("/admin/**")
                            .hasRole("ADMIN")
                        .antMatchers("/secure/**")
                            .hasRole("USER")
                .and()
                    .formLogin()
                        .successHandler(savedRequestAwareAuthenticationSuccessHandler())
                            .loginPage("/login")
                            .failureUrl("/login?error")
                            .permitAll()
                .and()
                    .rememberMe()
                        .key(rememberMeKey)
                        .rememberMeServices(rememberMeServices())
                        .tokenValiditySeconds(86400)
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .deleteCookies("remember-me")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true);

        if (!Strings.isNullOrEmpty(env) && env.equals("dev")) {
            // We do that to allow the usage of H2 Console
            log.warn("*******************************************************");
            log.warn("Application in Development Mode. Disabling CSRF Checks.");
            log.warn("*******************************************************");
            httpSecurity
                    .csrf()
                    .disable();

            httpSecurity
                    .headers()
                    .frameOptions().disable();
        } else {
            httpSecurity
                    .csrf()
                    .csrfTokenRepository(csrfTokenRepository());
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        log.info("Configuring AuthenticationManagerBuilder");
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        log.info("Creating RememberMeServices Bean with Key : " + rememberMeKey);
        return new PersistentTokenBasedRememberMeServices(
                rememberMeKey,
                userDetailsService,
                new PersistentTokenRepositoryImpl(rememberMeTokenRepository)
        );
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        log.info("Creating RoleHierarchy Bean");
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_SYSTEM > ROLE_ROOT ROLE_ROOT > ROLE_ADMIN ROLE_ADMIN > ROLE_USER ROLE_USER > ROLE_ANONYMOUS");
        return roleHierarchy;
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> securityExpressionHandler() {
        log.info("Creating SecurityExpressionHandler Bean");
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    private SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        log.info("Creating SavedRequestAwareAuthenticationSuccessHandler Bean");
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        auth.setDefaultTargetUrl("/secure/index");
        return auth;
    }

    private PasswordEncoder passwordEncoder() {
        log.info("Creating PasswordEncoder Bean");
        return new BCryptPasswordEncoder(12);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        log.info("Creating CsrfTokenRepository Bean");
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}