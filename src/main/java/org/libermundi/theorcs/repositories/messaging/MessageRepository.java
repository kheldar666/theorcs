package org.libermundi.theorcs.repositories.messaging;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends UndeletableRepository<Message,Long> {
    List<Message> findAllByFolderAndDeleted(MessageFolder folder, boolean deleted, Sort sort);

    @Query("select count (m) from Message m where m.markAsRead = false and m.folder = ?1 and m.deleted = false")
    Long countUnread(MessageFolder messageFolder);

    @Query("select count (m) from Message m where m.markAsRead = false and m.folder.owner = ?1 and m.deleted = false")
    Long countUnread(Character character);

}
