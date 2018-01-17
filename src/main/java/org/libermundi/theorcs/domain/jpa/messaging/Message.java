package org.libermundi.theorcs.domain.jpa.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.StatefulEntity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"subject","date"},callSuper = true)
public class Message extends StatefulEntity {

    @ManyToOne(optional = true) //If from == null => Sent by System
    private Character from;

    @ManyToOne
    private MessageFolder folder;

    @Enumerated(EnumType.STRING)
    @Column(length=3,nullable=false)
    private MessageType messageType;

    @ManyToOne
    private MessageContent content;

    private boolean markAsRead;

}
