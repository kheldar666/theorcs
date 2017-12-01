package org.libermundi.theorcs.domain.jpa.base;


import lombok.Getter;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.listeners.CreatedOrUpdatedByListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(CreatedOrUpdatedByListener.class)
public abstract class AuditableEntity extends StatefulEntity implements CreatedOrUpdatedBy {
    private String createdBy;
    private String updatedBy;
}
