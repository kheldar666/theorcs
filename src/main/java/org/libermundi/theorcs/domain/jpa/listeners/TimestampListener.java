package org.libermundi.theorcs.domain.jpa.listeners;

import org.libermundi.theorcs.domain.jpa.base.Timestampable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class TimestampListener {
	
	@PreUpdate
	public void setModifiedDate(Timestampable t) {
		Date updatedDate = new Date();
		t.setUpdatedDate(updatedDate);
	}
	
	@PrePersist
	public void setCreatedDate(Timestampable t) {
		Date createdDate = new Date();
		t.setCreatedDate(createdDate);
		t.setUpdatedDate(createdDate);
	}
}
