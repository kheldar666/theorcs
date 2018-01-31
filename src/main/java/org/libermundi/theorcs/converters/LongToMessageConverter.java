package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.MessagingService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToMessageConverter implements Converter<Long, Message> {
    private final MessagingService messagingService;

    public LongToMessageConverter(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public Message convert(Long source) {
        return messagingService.findById(source);
    }
}
