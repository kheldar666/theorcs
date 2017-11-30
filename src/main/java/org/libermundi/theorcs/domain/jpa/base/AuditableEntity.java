package org.libermundi.theorcs.domain.jpa.base;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuditableEntity extends StatefulEntity implements CreatedOrModifiedBy {
    private String createdBy;
    private String modifiedBy;
}
