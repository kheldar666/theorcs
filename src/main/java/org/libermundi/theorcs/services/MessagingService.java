package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.forms.MessageForm;

import java.util.List;

public interface MessagingService extends BaseService<Message> {
    void sendMessage(MessageForm messageForm);

    List<MessageFolder> getFolderList(Character character);

    MessageFolder findMessageFolderById(Character character, Long folderId);

    MessageFolder findMessageFolderByName(Character character, String folder);

    List<Message> findMessagesByFolder(Character character, MessageFolder messageFolder);

    void initFolders(Character character);

    MessageFolder findInbox(Character character);

    Long getUnreadMessageCount(MessageFolder messageFolder);

    Long getUnreadMessageCount(Character character);

    void markAsRead(Message ... messages);
}

