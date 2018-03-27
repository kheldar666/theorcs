package org.libermundi.theorcs.services.messaging.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.messaging.AddressBook;
import org.libermundi.theorcs.repositories.messaging.AddressBookRepository;
import org.libermundi.theorcs.services.base.impl.AbstractServiceImpl;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.messaging.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Slf4j
@Service("AddressBookService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class AddressBookServiceImpl extends AbstractServiceImpl<AddressBook> implements AddressBookService {

    private final CharacterService characterService;

    private final AddressBookRepository addressBookRepository;

    public AddressBookServiceImpl(CharacterService characterService, AddressBookRepository addressBookRepository) {
        this.characterService = characterService;
        this.addressBookRepository = addressBookRepository;
    }


    @Override
    public AddressBook createNew() {
        return new AddressBook();
    }

    @Override
    public void initData() {
        Iterable<Character> allCharacters = characterService.findAll();
        for (Character owner : allCharacters) {
            AddressBook addressBook = createNew(owner);
            addressBook.setName("Default");
            addressBookRepository.save(addressBook);
        }
    }

    private AddressBook createNew(Character owner) {
        AddressBook book = createNew();
        book.setOwner(owner);
        return book;
    }

    @Override
    public AddressBook findDefaultAddressBook(Character character) {
        return addressBookRepository.findFirstByOwnerOrderByIdAsc(character);
    }

    @Override
    public Set<AddressBook> findAllByOwner(Character character) {
        return addressBookRepository.findAllByOwnerOrderByName(character);
    }
}
