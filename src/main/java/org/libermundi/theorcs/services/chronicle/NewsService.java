package org.libermundi.theorcs.services.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.base.BaseService;

import java.util.List;

public interface NewsService extends BaseService<News> {
    List<News> getAllNews(Chronicle chronicle);
}
