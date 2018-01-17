package org.libermundi.theorcs.domain.jpa.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name"},callSuper = true)
public class MessageFolder extends Identity{

    private String name;

    @ManyToOne
    private Character owner;

    private boolean forSentMessage;

}
