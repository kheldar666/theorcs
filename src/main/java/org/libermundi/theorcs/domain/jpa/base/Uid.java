package org.libermundi.theorcs.domain.jpa.base;


public interface Uid {
    /**
     * @return a Unique Identifier for this Object.
     */
	String getUid();
	
	/**
	 * Set a Unique Identifier for this Object
	 * @param uid
	 */
	void setUid(String uid);
  
}
