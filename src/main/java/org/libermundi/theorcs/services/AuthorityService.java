package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.security.Authority;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthorityService extends BaseService<Authority> {
    Authority findByAuthority(String authority);
    /**
     * Check if the {@link Authentication} provided is in the Collection of authorities either directly or through transitive relation
     * @param authorities
     * @param authority
     * @return true if
     *
     * @see RoleHierarchy#getReachableGrantedAuthorities(Collection)
     */
    boolean hasReachableAuthority(Collection<? extends GrantedAuthority> authorities, GrantedAuthority authority);

    /**
     * @see AuthorityService#hasReachableAuthority(Collection, GrantedAuthority)
     * @param user
     * @param authority
     * @return
     */
    boolean hasReachableAuthority(User user, GrantedAuthority authority);
}
