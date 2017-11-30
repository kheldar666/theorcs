package org.libermundi.theorcs.configuration;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void configure(WebSecurity web) throws Exception {
        log.info("Configuring WebSecurity");
        web
                .ignoring()
                .antMatchers("/js/**","/images/**","/css/**");
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
                .anyRequest()
                    .hasRole("USER");

        if (!Strings.isNullOrEmpty(env) && env.equals("dev")) {
            log.warn("****************************************************");
            log.warn("Application in Development Mode. Enabling H2Console.");
            log.warn("****************************************************");
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

    private SecurityExpressionHandler<FilterInvocation> securityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ROOT > ROLE_ADMIN ROLE_ADMIN > ROLE_USER ROLE_USER > ROLE_ANONYMOUS");
        return roleHierarchy;
    }

    @Bean
    protected CsrfTokenRepository csrfTokenRepository() {
        log.info("Creating CsrfTokenRepository Bean");
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}