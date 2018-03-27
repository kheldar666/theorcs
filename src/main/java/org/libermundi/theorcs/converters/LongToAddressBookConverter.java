package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.messaging.AddressBook;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.services.messaging.AddressBookService;
import org.libermundi.theorcs.services.messaging.MessagingService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToAddressBookConverter implements Converter<Long, AddressBook> {
    private final AddressBookService addressBookService;

    public LongToAddressBookConverter(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @Override
    public AddressBook convert(Long source) {
        return addressBookService.findById(source);
    }
}
