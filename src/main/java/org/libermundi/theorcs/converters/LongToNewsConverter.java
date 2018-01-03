package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.NewsService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToNewsConverter implements Converter<Long, News> {
    private final NewsService newsService;

    public LongToNewsConverter(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public News convert(Long source) {
        return newsService.findById(source);
    }
}
