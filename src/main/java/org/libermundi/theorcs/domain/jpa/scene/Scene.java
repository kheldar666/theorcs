package org.libermundi.theorcs.domain.jpa.scene;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Scene extends UidAuditableEntity {
    @ManyToOne(optional = false)
    private Character author;

    @Column(length = 125, nullable = false)
    private String title;

    @Column(nullable = false, length = 15)
    private String date;

    private Boolean locked;

    @OneToMany(mappedBy = "scene")
    private Set<Post> posts = Sets.newHashSet();

    @ManyToOne
    private Chronicle chronicle;

    @ManyToMany
    private Set<Character> registeredCharacters = Sets.newHashSet();
}
