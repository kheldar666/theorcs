package org.libermundi.theorcs.domain.jpa.base;


public interface Uid {
    String PROP_UID = "uid";
    
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
