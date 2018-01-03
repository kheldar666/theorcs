package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;

import java.util.List;

public interface NewsService extends BaseService<News> {
    List<News> getAllNews(Chronicle chronicle);
}
