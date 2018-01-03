package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends BaseRepository<News, Long> {
    List<News> findAllByChronicle(Chronicle chronicle);
}
