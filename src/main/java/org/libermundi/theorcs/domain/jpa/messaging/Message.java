package org.libermundi.theorcs.domain.jpa.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.StatefulEntity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"sender"},callSuper = true)
public class Message extends StatefulEntity {

    @ManyToOne(optional = true) //If from == null => Sent by System
    private Character sender;

    @ManyToMany
    private Set<Character> toRecipient;

    @ManyToMany
    private Set<Character> ccRecipient;

    @ManyToMany
    private Set<Character> bccRecipient;

    @ManyToOne
    private MessageFolder folder;

    @ManyToOne(cascade = CascadeType.ALL)
    private MessageContent content;

    private boolean markAsRead;

}
