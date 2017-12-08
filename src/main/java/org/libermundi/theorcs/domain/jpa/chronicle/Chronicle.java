package org.libermundi.theorcs.domain.jpa.chronicle;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.base.StatefulEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"title","game","admin"},callSuper = true)
public final class Chronicle extends StatefulEntity {

	@Column(length=50,nullable = false)
	private String title;

	@Column(length=125)
	private String subTitle;

	@Lob
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(length=8,nullable=false)
	private ChronicleType type;

	@Enumerated(EnumType.STRING)
	@Column(length=12,nullable=false)
	private ChronicleGenre genre;

	@Enumerated(EnumType.STRING)
	@Column(length=12,nullable=false)
	private ChronicleStyle style;

	@Enumerated(EnumType.STRING)
	@Column(length=7,nullable=false)
	private ChroniclePace pace;

	@Column(length=15)
	private String password;

	@Column(length=50)
	private String date;

	@Lob
	private String background;

	@ManyToOne
	private Game game;

	@ManyToOne
	private User admin;

	private boolean openForInscription;

	@OneToMany
	private Set<Faction> factions = Sets.newHashSet();

}
