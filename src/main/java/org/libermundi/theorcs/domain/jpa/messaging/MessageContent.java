package org.libermundi.theorcs.domain.jpa.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"subject","date"},callSuper = true)
public class MessageContent extends Identity {

    @Column(nullable = false)
    private String subject;

    private Date date;

    @Lob
    private String content;

    private boolean anonymous;

    private boolean appendSignature;

}
