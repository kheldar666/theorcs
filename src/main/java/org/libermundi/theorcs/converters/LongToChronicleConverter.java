package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.ChronicleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToChronicleConverter implements Converter<Long, Chronicle> {
    private final ChronicleService chronicleService;

    public LongToChronicleConverter(ChronicleService chronicleService) {
        this.chronicleService = chronicleService;
    }

    @Override
    public Chronicle convert(Long source) {
        return chronicleService.findById(source);
    }
}
