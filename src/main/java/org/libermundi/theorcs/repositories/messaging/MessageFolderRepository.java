package org.libermundi.theorcs.repositories.messaging;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageFolderRepository extends UndeletableRepository<MessageFolder, Long> {

    @Query("select f from MessageFolder f where messageFolderType = org.libermundi.theorcs.domain.jpa.messaging.MessageFolderType.INBOX AND owner = ?1")
    MessageFolder findInbox(Character character);

    @Query("select f from MessageFolder f where messageFolderType = org.libermundi.theorcs.domain.jpa.messaging.MessageFolderType.SENT AND owner = ?1")
    MessageFolder findSent(Character character);

    @Query("select f from MessageFolder f where messageFolderType = org.libermundi.theorcs.domain.jpa.messaging.MessageFolderType.TRASH AND owner = ?1")
    MessageFolder findTrash(Character character);

    @Query("select f from MessageFolder f where id = ?1 AND owner = ?2")
    MessageFolder findById(Long id, Character character);

    List<MessageFolder> findAllByOwner(Character owner, Sort sort);

    @Query("select f from MessageFolder f where name = ?1 AND owner = ?2")
    MessageFolder findByNameAndCharacter(String folder, Character character);
}
