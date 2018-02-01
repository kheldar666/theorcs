package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.domain.jpa.messaging.MessageType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message,Long> {
    List<Message> findAllByFolder(MessageFolder folder, Sort sort);

    @Query("select count (m) from Message m where m.markAsRead = false and m.folder = ?1 and m.deleted = false")
    Long countUnread(MessageFolder messageFolder);

    @Query("select count (m) from Message m where m.markAsRead = false and m.recipient = ?1 and m.deleted = false")
    Long countUnread(Character character);

    @Query("select m.recipient from Message m where m = ?1 and m.messageType = ?2")
    List<Character> getAllRecipents(Message message, MessageType messageType);
}
