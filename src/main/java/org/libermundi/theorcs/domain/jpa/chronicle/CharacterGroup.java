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
import org.libermundi.theorcs.domain.jpa.base.UidAuditableEntity;
import org.libermundi.theorcs.domain.jpa.scene.Scene;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name","chronicle"},callSuper = true)
public final class CharacterGroup extends UidAuditableEntity {

	@Column(length=128, nullable=false)
	private String name;

	@Lob
	private String description;

	@Column(length=256, nullable=false)
	private String reason;

	@Column(nullable = false, length = 15)
	private String date;

	@ManyToOne
	private Chronicle chronicle;

	@Enumerated(EnumType.STRING)
	@Column(length=64, nullable=false)
	private CharacterGroupType type;

	private Boolean publicVisibility;

	private Boolean open;

	@ManyToMany
	private Set<Character> members = Sets.newHashSet();

	@ManyToMany
	private Set<Character> leaders = Sets.newHashSet();

	@OneToMany(mappedBy = "characterGroup")
	private Set<Scene> scenes = Sets.newHashSet();

}
