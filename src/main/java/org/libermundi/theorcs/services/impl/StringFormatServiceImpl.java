package org.libermundi.theorcs.services.impl;

import org.libermundi.theorcs.services.StringFormatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class StringFormatServiceImpl implements StringFormatService {
    private final SimpleDateFormat dateFormat;

    public StringFormatServiceImpl(@Value("${theorcs.general.dateformat}") String dateformat) {
        this.dateFormat = new SimpleDateFormat(dateformat);
    }

    @Override
    public String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
