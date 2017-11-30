package org.libermundi.theorcs.domain.jpa.listeners;

import org.libermundi.theorcs.domain.jpa.base.Uid;

import javax.persistence.PrePersist;
import java.util.UUID;

public class UidListener {
	
	@PrePersist
	public void setUid(Uid uid) {
		if(uid.getUid() == null || uid.getUid().isEmpty()){
			uid.setUid(UUID.randomUUID().toString());
		}
	}
}
