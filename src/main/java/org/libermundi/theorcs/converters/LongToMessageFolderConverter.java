package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.repositories.MessageFolderRepository;
import org.libermundi.theorcs.services.ChronicleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LongToMessageFolderConverter implements Converter<Long, MessageFolder> {
    private final MessageFolderRepository messageFolderRepository;

    public LongToMessageFolderConverter(MessageFolderRepository messageFolderRepository) {
        this.messageFolderRepository = messageFolderRepository;
    }

    @Override
    public MessageFolder convert(Long source) {
        Optional<MessageFolder> opt =  messageFolderRepository.findById(source);
        if(opt.isPresent()) {
            return opt.get();
        }
        return null;
    }
}
