package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message,Long> {
    List<Message> findAllByFolder(MessageFolder folder);
}
