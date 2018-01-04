package org.libermundi.theorcs.converters;

import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.NewsService;
import org.libermundi.theorcs.services.PictureService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LongToPictureConverter implements Converter<Long, Picture> {
    private final PictureService pictureService;

    public LongToPictureConverter(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Override
    public Picture convert(Long source) {
        return pictureService.findById(source);
    }
}
