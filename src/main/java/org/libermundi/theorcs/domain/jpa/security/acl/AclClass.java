package org.libermundi.theorcs.domain.jpa.security.acl;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "acl_class", uniqueConstraints = @UniqueConstraint(columnNames = "class"))
@Getter
@Setter
public class AclClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, name = "class", nullable = false)
    private String clazz;

}
