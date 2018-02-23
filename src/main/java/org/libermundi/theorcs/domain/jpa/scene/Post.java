package org.libermundi.theorcs.domain.jpa.scene;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Post extends UidAuditableEntity {
    @ManyToOne(optional = false)
    private Character perso;

    @Lob
    private String content;

    @Column(nullable = false, length = 15)
    private String date;

    @ManyToOne
    private Scene scene;
}
