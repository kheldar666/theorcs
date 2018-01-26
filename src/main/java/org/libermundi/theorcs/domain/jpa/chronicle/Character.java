package org.libermundi.theorcs.domain.jpa.chronicle;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.libermundi.theorcs.domain.Gender;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Characters") // character is a reserved keyword on MySQL
@Getter
@Setter
@NoArgsConstructor
@ToString(of={"name","chronicle"},callSuper = true)
public final class Character extends UidAuditableEntity {

	@Column(length=50, nullable=false)
	private String name;

	@OneToOne
	private User player;

	@ManyToOne
	private Chronicle chronicle;

	@Lob
	private Byte[] sheet;

	@Lob
	private String description;

	@Lob
	private String background;

	@Lob
	private String hiddenInfos;

	private int age = 0;

	@Enumerated(EnumType.STRING)
	@Column(length=7,nullable=true)
	private Gender gender = Gender.DONTSAY;

	private boolean defaultCharacter = Boolean.FALSE;

	@ManyToMany
	private Set<CharacterGroup> groups = Sets.newHashSet();

	@ManyToOne
	private CharacterGroup defaultGroup;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Picture avatar;


}
