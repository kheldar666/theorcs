package org.libermundi.theorcs.domain.jpa.chronicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name"},callSuper = true)
public final class GameSystem extends Identity {
	@Column(length=50, nullable=false)
	private String name;
	
}
