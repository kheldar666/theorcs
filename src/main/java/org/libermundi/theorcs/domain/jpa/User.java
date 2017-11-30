package org.libermundi.theorcs.domain.jpa;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.Gender;
import org.libermundi.theorcs.domain.Salutation;
import org.libermundi.theorcs.domain.jpa.base.Account;
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;
import org.libermundi.theorcs.domain.jpa.listeners.PasswordListener;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(of={"id","fullName","email"})
@Entity
@EntityListeners({PasswordListener.class})
public class User extends UidAuditableEntity implements UserDetails, Account {

    @Enumerated(value = EnumType.STRING)
    @Column(length=7,nullable=false)
    private Gender gender= Gender.DONTSAY;

    @Enumerated(value = EnumType.STRING)
    @Column(length=4,nullable=false)
    private Salutation salutation=Salutation.NONE;

    @Column(length=30)
    private String firstName;

    @Column(length=50)
    private String lastName;

    @Column(length=25)
    private String nickName;

    @Column(length=30,unique=true,nullable=false)
    private String username;

    @Column(length=60,nullable=false)
    private String password;

    @Column(length=255,unique=true,nullable=false)
    private String email;

    @Lob
    private Byte[] avatar;

    @Lob
    private String description;

    @Column(length=25)
    private String phoneNumber;

    @Column(length=25)
    private String cellularNumber;

    @Column(length=36)
    private String activationKey;

    @ManyToMany
    @JoinTable(name = "Users2Authorities")
    private Set<Authority> authorities = Sets.newHashSet();

    private Address address;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    @Transient
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

}
