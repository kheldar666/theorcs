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
package org.libermundi.theorcs.domain.jpa.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.UUID;


/**
 * Entity with ID is Long type
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(of = "id")
@MappedSuperclass
public abstract class Identity {
    
    private Long id;
    private boolean deleted=Boolean.FALSE;

    @Transient
    private final String tempUuid = UUID.randomUUID().toString();

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : tempUuid.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Identity identity = (Identity) obj;

        return id != null ? id.equals(identity.id) : tempUuid.equals(identity.tempUuid);
    }

}
