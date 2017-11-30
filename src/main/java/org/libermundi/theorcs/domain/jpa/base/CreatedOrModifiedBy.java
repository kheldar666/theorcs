package org.libermundi.theorcs.domain.jpa.base;


public interface CreatedOrModifiedBy {

    String getCreatedBy();

    void setCreatedBy(String username);
    
    String getModifiedBy();

    void setModifiedBy(String username);
}
