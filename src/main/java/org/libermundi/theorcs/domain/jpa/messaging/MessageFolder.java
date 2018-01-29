package org.libermundi.theorcs.domain.jpa.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name"},callSuper = true)
public class MessageFolder extends Identity{
    public final static String INBOX = "Inbox";
    public final static String SENT = "Sent";
    public final static String TRASH = "Trash";

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Character owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 5)
    private MessageFolderType messageFolderType;

    @Column(nullable = false)
    private int indexOrder;

}
