package org.libermundi.theorcs.domain.jpa.security.acl;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "acl_object_identity", uniqueConstraints = @UniqueConstraint(columnNames = {"object_id_class","object_id_identity"}))
@Getter
@Setter
public class AclObjectIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id_class", nullable = false)
    private AclClass objectClass;

    @Column(name = "object_id_identity", length = 36)
    private String objectIdentity;

    @ManyToOne
    @JoinColumn(name = "parent_object")
    private AclObjectIdentity parentObject;

    @ManyToOne
    @JoinColumn(name = "owner_sid")
    private AclSid owner;

    @Column(nullable = false, name = "entries_inheriting")
    private boolean entriesInheriting;

}
