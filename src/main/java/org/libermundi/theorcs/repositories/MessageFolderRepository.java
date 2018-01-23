package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageFolderRepository extends BaseRepository<MessageFolder, Long> {

    @Query("select f from MessageFolder f where name = org.libermundi.theorcs.domain.jpa.messaging.MessageFolder.INBOX")
    MessageFolder findInbox();

    @Query("select f from MessageFolder f where name = org.libermundi.theorcs.domain.jpa.messaging.MessageFolder.SENT")
    MessageFolder findSent();

    @Query("select f from MessageFolder f where name = org.libermundi.theorcs.domain.jpa.messaging.MessageFolder.TRASH")
    MessageFolder findTrash();

    List<MessageFolder> findAllByOwner(Character owner, Sort sort);

}
