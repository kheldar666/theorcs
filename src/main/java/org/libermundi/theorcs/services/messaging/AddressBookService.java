package org.libermundi.theorcs.services.messaging;

import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.AddressBook;
import org.libermundi.theorcs.services.base.BaseService;

import java.util.Set;

public interface AddressBookService  extends BaseService<AddressBook> {
    AddressBook findDefaultAddressBook(Character character);

    Set<AddressBook> findAllByOwner(Character character);
}
