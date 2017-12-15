package org.libermundi.theorcs.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
public class AclSecurityConfiguration {
    @Bean
    public MutableAclService aclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        MutableAclService aclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        return aclService;
    }

    @Bean
    public AclCache aclCache(CacheManager cacheManager, PermissionGrantingStrategy permissionGrantingStrategy) {
        return new SpringCacheBasedAclCache(cacheManager.getCache("default"),permissionGrantingStrategy, aclAuthorizationStrategy());
    }

    @Bean
    public AuditLogger auditLogger() {
        return new ConsoleAuditLogger();
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ROOT"));
    }

    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, AuditLogger auditLogger) {
        return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy(AuditLogger auditLogger) {
        return new DefaultPermissionGrantingStrategy(auditLogger);
    }

    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator(AclService aclService) {
        return new AclPermissionEvaluator(aclService);
    }

}
