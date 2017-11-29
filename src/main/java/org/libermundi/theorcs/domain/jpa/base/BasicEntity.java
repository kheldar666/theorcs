// [LICENCE-HEADER]
//
package org.libermundi.theorcs.domain.jpa.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;


/**
 * A simple Entity that have an Id and is undeletable
 *
 */
@Getter
@Setter
@ToString(of = {"id","deleted"})
@MappedSuperclass
public class BasicEntity extends Identity implements Undeletable {
    private boolean deleted=Boolean.FALSE;


}
