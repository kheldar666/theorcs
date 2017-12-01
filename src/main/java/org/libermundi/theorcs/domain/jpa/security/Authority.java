package org.libermundi.theorcs.domain.jpa.security;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Authority extends Identity implements GrantedAuthority {

    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = Sets.newHashSet();;

    public Authority(String authority) {
        this.authority = authority;
    }
}
