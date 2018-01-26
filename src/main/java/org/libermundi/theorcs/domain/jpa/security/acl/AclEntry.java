package org.libermundi.theorcs.domain.jpa.security.acl;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "acl_entry", uniqueConstraints = @UniqueConstraint(columnNames = {"acl_object_identity","ace_order"}))
@Getter
@Setter
public class AclEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "acl_object_identity", nullable = false)
    private AclObjectIdentity aclObjectIdentity;

    @Column(name = "ace_order", nullable = false)
    private int aclOrder;

    @ManyToOne
    @JoinColumn(name = "sid", nullable = false)
    private AclSid aclSid;

    @Column(name = "mask", nullable = false)
    private int mask;

    @Column(name = "granting", nullable = false)
    private boolean granting;

    @Column(name = "audit_success", nullable = false)
    private boolean auditSuccess;

    @Column(name = "audit_failure", nullable = false)
    private boolean auditFailure;

}
