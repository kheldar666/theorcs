/*
 * Copyright (c) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.libermundi.theorcs.domain.jpa.chronicle;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.Gender;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
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
	private Byte[] picture;

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
	private Set<Faction> factions = Sets.newHashSet();

	@ManyToOne
	private Faction defaultFaction;

}
