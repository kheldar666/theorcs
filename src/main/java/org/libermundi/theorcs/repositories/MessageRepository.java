package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends BaseRepository<Message,Long> {
}
