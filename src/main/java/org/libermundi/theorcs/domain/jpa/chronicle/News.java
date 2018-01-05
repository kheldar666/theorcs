package org.libermundi.theorcs.domain.jpa.chronicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.base.Identity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"title","date"},callSuper = true)
public class News extends Identity {
    @Column(length = 125, nullable = false)
    private String title;

    @Column(nullable = false, length = 15)
    private String date;

    @Lob
    @Column(nullable = false)
    private String content;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Picture picture;

    @ManyToOne
    private Chronicle chronicle;
}
