package org.libermundi.theorcs.domain.jpa.chronicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name","gameSystem"},callSuper = true)
public final class Game extends Identity {

	@Column(length=50, nullable=false)
	private String name;

	@ManyToOne
	private GameSystem gameSystem;

}
