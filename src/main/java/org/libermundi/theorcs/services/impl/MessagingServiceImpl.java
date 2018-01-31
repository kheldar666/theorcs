package org.libermundi.theorcs.services.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.*;
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
        MessageContent content = new MessageContent();
            content.setAnonymous(messageForm.isAnonymous());
            content.setAppendSignature(messageForm.isSignature());
            content.setContent(messageForm.getContent());
            content.setSubject(messageForm.getSubject());
            content.setDate(new Date());


        messageForm.getTo().forEach(to -> {
            MessageFolder inbox = messageFolderRepository.findInbox(to);
            messageRepository.save(
                    createNew(messageForm.getFrom(),to,content,MessageType.TO, inbox)
            );
        });

        messageForm.getCc().forEach(cc -> {
            MessageFolder inbox = messageFolderRepository.findInbox(cc);
            messageRepository.save(
                    createNew(messageForm.getFrom(),cc,content,MessageType.CC, inbox)
            );
        });

        messageForm.getBcc().forEach(bcc -> {
            MessageFolder inbox = messageFolderRepository.findInbox(bcc);
            messageRepository.save(
                    createNew(messageForm.getFrom(),bcc,content,MessageType.BCC, inbox)
            );
        });
    }

    @Override
    public List<MessageFolder> getFolderList(Character character) {
        List<MessageFolder> folderList = Lists.newArrayList();

        folderList.addAll(messageFolderRepository.findAllByOwner(character, Sort.by("indexOrder")));

        return folderList;
    }

    @Override
    public MessageFolder findInbox(Character character) {
        return messageFolderRepository.findInbox(character);
    }

    @Override
    public MessageFolder findMessageFolderById(Character character, Long messageFolderId) {
        Optional<MessageFolder> optional = messageFolderRepository.findById(messageFolderId);
        if(!optional.isPresent() || (optional.isPresent() && !optional.get().getOwner().equals(character))){
            throw new EntityNotFoundException("Could not find MessageFolder ID : " + messageFolderId);
        }
        return optional.get();
    }

    @Override
    public MessageFolder findMessageFolderByName(Character character, String folder) {
        MessageFolder messageFolder = messageFolderRepository.findByNameAndCharacter(folder,character);
        return messageFolder;
    }

    @Override
    public List<Message> findMessagesByFolder(Character character, MessageFolder messageFolder) {
        if(messageFolder.getOwner().equals(character)){
            return messageRepository.findAllByFolder(messageFolder, Sort.by("content.date").descending());
        } else {
            return null;
        }

    }

    @Override
    public Long getUnreadMessageCount(MessageFolder messageFolder) {
        return messageRepository.countUnread(messageFolder);
    }

    @Override
    public Long getUnreadMessageCount(Character character) {
        return messageRepository.countUnread(character);
    }

    @Override
    public void markAsRead(Message ... messages) {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            message.setMarkAsRead(Boolean.TRUE);
            messageRepository.save(message);
        }
    }

    @Override
    public void initFolders(Character character) {
        if(messageFolderRepository.findAllByOwner(character, Sort.by("indexOrder")).isEmpty()){
            MessageFolder inbox = new MessageFolder();
            inbox.setName(MessageFolder.INBOX);
            inbox.setMessageFolderType(MessageFolderType.INBOX);
            inbox.setOwner(character);
            inbox.setIndexOrder(1);

            messageFolderRepository.save(inbox);

            MessageFolder sent = new MessageFolder();
            sent.setName(MessageFolder.SENT);
            sent.setMessageFolderType(MessageFolderType.SENT);
            sent.setOwner(character);
            sent.setIndexOrder(2);

            messageFolderRepository.save(sent);

            MessageFolder trash = new MessageFolder();
            trash.setName(MessageFolder.TRASH);
            trash.setMessageFolderType(MessageFolderType.TRASH);
            trash.setOwner(character);
            trash.setIndexOrder(3);

            messageFolderRepository.save(trash);
        }
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
        MessageForm messageForm = new MessageForm();


    }
}
