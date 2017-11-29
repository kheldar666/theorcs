// [LICENCE-HEADER]
//

package org.libermundi.theorcs.domain.jpa.base;

/**
 * Interface marks class which cannot be deleted. If someone calls one of DAO's delete
 * methods object will be hidden instead of being actually deleted from the datasource.
 *
 */

public interface Undeletable {

    /**
     * Check if object is deleted from the Application POV.
     *
     * @return true when object is deleted
     */
    boolean isDeleted();

    /**
     * Set object as default one.
     *
     * @param deleted value of deleted flag
     */
    void setDeleted(boolean deleted);
}
