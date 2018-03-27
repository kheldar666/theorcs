package org.libermundi.theorcs.services.messaging.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.*;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.repositories.messaging.MessageFolderRepository;
import org.libermundi.theorcs.repositories.messaging.MessageRepository;
import org.libermundi.theorcs.services.base.impl.AbstractServiceImpl;
import org.libermundi.theorcs.services.messaging.MessagingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service("MessagingService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class MessagingServiceImpl extends AbstractServiceImpl<Message> implements MessagingService {
    private final MessageFolderRepository messageFolderRepository;

    public MessagingServiceImpl(MessageFolderRepository messageFolderRepository, MessageRepository messageRepository) {
        this.messageFolderRepository = messageFolderRepository;
        setRepository(messageRepository,Message.class);
    }

    @Override
    public void sendMessage(MessageForm messageForm) {
        MessageContent content = new MessageContent();
            content.setAnonymous(messageForm.isAnonymous());
            content.setAppendSignature(messageForm.isSignature());
            content.setContent(messageForm.getContent());
            content.setSubject(messageForm.getSubject());
            content.setDate(new Date());

        // We Save the Message in the Sent folder of the Sender (if the Sender is not the System :) )
        if(messageForm.getFrom() != null) {
            MessageFolder sent = messageFolderRepository.findSent(messageForm.getFrom());
            Message messageSent = createNew(messageForm.getFrom(),messageForm.getTo(), messageForm.getCc(), messageForm.getBcc(),content, sent);
            messageSent.setMarkAsRead(Boolean.TRUE);
            getRepository().save(
                messageSent
            );
        }


        //Then one copy of the message per Recipient (To/Cc/Bcc)
        messageForm.getTo().forEach(to -> {
            MessageFolder inbox = messageFolderRepository.findInbox(to);
            getRepository().save(
                    createNew(messageForm.getFrom(),messageForm.getTo(), messageForm.getCc(), messageForm.getBcc(),content, inbox)
            );
        });

        messageForm.getCc().forEach(cc -> {
            MessageFolder inbox = messageFolderRepository.findInbox(cc);
            getRepository().save(
                    createNew(messageForm.getFrom(),messageForm.getTo(), messageForm.getCc(), messageForm.getBcc(),content, inbox)
            );
        });

        messageForm.getBcc().forEach(bcc -> {
            MessageFolder inbox = messageFolderRepository.findInbox(bcc);
            getRepository().save(
                    createNew(messageForm.getFrom(),messageForm.getTo(), messageForm.getCc(), messageForm.getBcc(),content, inbox)
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
            return ((MessageRepository)getRepository()).findAllByFolderAndDeleted(messageFolder, Boolean.FALSE, Sort.by("content.date").descending());
        } else {
            return null;
        }

    }

    @Override
    public Long getUnreadMessageCount(MessageFolder messageFolder) {
        return ((MessageRepository)getRepository()).countUnread(messageFolder);
    }

    @Override
    public Long getUnreadMessageCount(Character character) {
        return ((MessageRepository)getRepository()).countUnread(character);
    }

    @Override
    public String getRecipientNamesAsStringList(Message message, MessageType messageType) {
        Set<Character> recipents = Sets.newHashSet();
        switch (messageType) {
            case TO:
                recipents = message.getToRecipient();
                break;
            case CC:
                recipents = message.getCcRecipient();
                break;
            case BCC:
                recipents = message.getBccRecipient();
                break;
        };

        if(recipents.isEmpty()) return "-";


        final AtomicInteger l = new AtomicInteger(recipents.size());
        final AtomicInteger i =  new AtomicInteger(0);;
        StringBuilder stringBuilder = new StringBuilder();
        recipents.forEach(recipent -> {
            i.addAndGet(1);
            stringBuilder.append(recipent.getName());
            if(i.intValue() < l.intValue()){
                stringBuilder.append(", ");
            }
        });

        return stringBuilder.toString();
    }

    @Override
    public void markAsRead(Message ... messages) {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            message.setMarkAsRead(Boolean.TRUE);
            getRepository().save(message);
        }
    }


    @Override
    public boolean isRecipent(Message message, Character character) {

        if(message.getToRecipient().contains(character)
                || message.getCcRecipient().contains(character)
                || message.getBccRecipient().contains(character)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean isSender(Message message, Character character) {
        return message.getSender() != null && message.getSender().equals(character);
    }

    @Override
    public void moveToTrash(Message message) {
        moveToFolder(message, messageFolderRepository.findTrash(message.getFolder().getOwner()));
    }

    @Override
    public void moveToInbox(Message message) {
        moveToFolder(message, messageFolderRepository.findInbox(message.getFolder().getOwner()));
    }

    @Override
    public void moveToSent(Message message) {
        moveToFolder(message, messageFolderRepository.findSent(message.getFolder().getOwner()));
    }

    @Override
    public void moveToFolder(Message message, MessageFolder folder) {
        message.setFolder(folder);
        getRepository().save(message);
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

    private Message createNew(Character sender, Set<Character> toRecipients, Set<Character> ccRecipients,
                              Set<Character> bccRecipients, MessageContent messageContent, MessageFolder messageFolder) {
        Message message = createNew();
            message.setSender(sender);
            message.setToRecipient(toRecipients);
            message.setCcRecipient(ccRecipients);
            message.setBccRecipient(bccRecipients);
            message.setContent(messageContent);
            message.setMarkAsRead(Boolean.FALSE);
            message.setFolder(messageFolder);

        return message;
    }

    @Override
    public Message createNew() {
        return new Message();
    }

    @Override
    public void initData() {
        // Nothing to do
    }
}
