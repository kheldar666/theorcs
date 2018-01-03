package org.libermundi.theorcs.domain.jpa.chronicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"title","date"},callSuper = true)
public class News extends Identity {
    @Column(length = 125, nullable = false)
    private String title;

    @Column(nullable = false)
    private Date date;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private Byte[] picture;

    @ManyToOne
    private Chronicle chronicle;
}
