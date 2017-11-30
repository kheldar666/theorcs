// [LICENCE-HEADER]
//
package org.libermundi.theorcs.domain.jpa.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.listeners.TimestampListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;


/**
 * Entity with full status: Enabled, Undeletable and Timestampable
 *
 */
@Getter
@Setter
@MappedSuperclass
@ToString(of = {"id","active","createdDate","updatedDate","deleted"})
@EntityListeners(TimestampListener.class)
public abstract class StatefulEntity extends UnDeletableEntity implements Enabled, Timestampable {
    private boolean enabled = Boolean.TRUE;
    private Date createdDate;
    private Date updatedDate;

}
