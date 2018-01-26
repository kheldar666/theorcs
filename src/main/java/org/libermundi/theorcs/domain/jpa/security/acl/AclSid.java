package org.libermundi.theorcs.domain.jpa.security.acl;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "acl_sid", uniqueConstraints = @UniqueConstraint(columnNames = {"sid","principal"}))
@Getter
@Setter
public class AclSid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean principal;

    @Column(length = 100, nullable = false)
    private String sid;


}
