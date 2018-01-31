package org.libermundi.theorcs.services.impl;

import org.libermundi.theorcs.services.StringFormatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service("StringFormatService")
public class StringFormatServiceImpl implements StringFormatService {
    private static final long A_DAY_IN_MILLISECONDS = 24*60*60*1000;

    private final SimpleDateFormat dateFormat;

    private final SimpleDateFormat timeFormat;

    private final MessageSource messageSource;

    public StringFormatServiceImpl(@Value("${theorcs.general.dateformat}") String dateformat,
                                   @Value("${theorcs.general.timeformat}") String timeformat,
                                   MessageSource messageSource) {
        this.dateFormat = new SimpleDateFormat(dateformat);
        this.timeFormat = new SimpleDateFormat(timeformat);
        this.messageSource = messageSource;
    }

    @Override
    public String formatDate(Date date) {
        return dateFormat.format(date);
    }

    @Override
    public String formatTime(Date date) {
        return timeFormat.format(date);
    }

    @Override
    public String formatDateForMessaging(Date date) {

        Locale locale = LocaleContextHolder.getLocale();

        Date now = new Date();

        long dateDiff = now.getTime() - date.getTime();

        if(dateDiff > A_DAY_IN_MILLISECONDS) {
            Object[] args = {formatDate(date),formatTime(date)};
            return messageSource.getMessage("components.messaging.date_sent",args,null,locale);
        } else {
            Object[] args = {formatTime(date)};
            return messageSource.getMessage("components.messaging.time_sent",args,null,locale);
        }
    }
}
