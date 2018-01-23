package org.libermundi.theorcs.services.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageContent;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.domain.jpa.messaging.MessageType;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.repositories.MessageFolderRepository;
import org.libermundi.theorcs.repositories.MessageRepository;
import org.libermundi.theorcs.services.MessagingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("MessagingService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class MessagingServiceImpl extends AbstractServiceImpl<Message> implements MessagingService {
    private final MessageFolderRepository messageFolderRepository;

    private final MessageRepository messageRepository;

    public MessagingServiceImpl(MessageFolderRepository messageFolderRepository, MessageRepository messageRepository) {
        this.messageFolderRepository = messageFolderRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void sendMessage(MessageForm messageForm) {
        MessageFolder inbox = messageFolderRepository.findInbox();
        MessageContent content = new MessageContent();
            content.setAnonymous(messageForm.isAnonymous());
            content.setAppendSignature(messageForm.isSignature());
            content.setContent(messageForm.getContent());
            content.setSubject(messageForm.getSubject());
            content.setDate(new Date());


        messageForm.getTo().forEach(to -> {
            messageRepository.save(
                    createNew(messageForm.getFrom(),to,content,MessageType.TO, inbox)
            );
        });

        messageForm.getCc().forEach(cc -> {
            messageRepository.save(
                    createNew(messageForm.getFrom(),cc,content,MessageType.CC, inbox)
            );
        });

        messageForm.getBcc().forEach(bcc -> {
            messageRepository.save(
                    createNew(messageForm.getFrom(),bcc,content,MessageType.BCC, inbox)
            );
        });
    }

    @Override
    public List<MessageFolder> getFolderList(Character character) {
        List<MessageFolder> folderList = Lists.newArrayList();

        folderList.add(messageFolderRepository.findInbox());
        folderList.add(messageFolderRepository.findSent());
        folderList.add(messageFolderRepository.findTrash());

        folderList.addAll(messageFolderRepository.findAllByOwner(character, Sort.by("name")));

        return folderList;
    }

    @Override
    public MessageFolder findMessageFolderById(Character character, Long messageFolderId) {
        Optional<MessageFolder> optional = messageFolderRepository.findById(messageFolderId);
        if(!optional.isPresent() || (optional.isPresent() && !optional.get().getOwner().equals(character))){
            throw new EntityNotFoundException("Could not find MessageFolder ID : " + messageFolderId);
        }
        return optional.get();
    }

    private Message createNew(Character sender, Character recipient, MessageContent messageContent, MessageType messageType, MessageFolder messageFolder) {
        Message message = createNew();
            message.setSender(sender);
            message.setRecipient(recipient);
            message.setContent(messageContent);
            message.setMarkAsRead(Boolean.FALSE);
            message.setMessageType(messageType);
            message.setFolder(messageFolder);

        return message;
    }

    @Override
    public Message createNew() {
        return new Message();
    }

    @Override
    public void initData() {
        if(messageFolderRepository.count() == 0) {
            // First we need to create the 3 basic folders that everyone will use
            // Inbox, Sent and Trash
            MessageFolder inbox = new MessageFolder();
                inbox.setName(MessageFolder.INBOX);
                inbox.setForSentMessage(Boolean.FALSE);
                inbox.setOwner(null);

                messageFolderRepository.save(inbox);

            MessageFolder sent = new MessageFolder();
                sent.setName(MessageFolder.SENT);
                sent.setForSentMessage(Boolean.TRUE);
                sent.setOwner(null);

                messageFolderRepository.save(sent);

            MessageFolder trash = new MessageFolder();
                trash.setName(MessageFolder.TRASH);
                trash.setForSentMessage(Boolean.FALSE);
                trash.setOwner(null);

                messageFolderRepository.save(trash);
        }


    }
}
