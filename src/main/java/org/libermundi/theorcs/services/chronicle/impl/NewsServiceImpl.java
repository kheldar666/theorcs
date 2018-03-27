package org.libermundi.theorcs.services.chronicle.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.utils.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.repositories.chronicle.NewsRepository;
import org.libermundi.theorcs.services.chronicle.ChronicleService;
import org.libermundi.theorcs.services.chronicle.NewsService;
import org.libermundi.theorcs.services.utils.PictureService;
import org.libermundi.theorcs.services.base.impl.AbstractServiceImpl;
import org.libermundi.theorcs.services.utils.StringFormatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Implementation of NewsService
 *
 */
@Slf4j
@Service("NewsService")
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class NewsServiceImpl extends AbstractServiceImpl<News> implements NewsService {
	private final String dateFormat;

	private final ChronicleService chronicleService;

	private final PictureService pictureService;

	public NewsServiceImpl(NewsRepository newsRepository, ChronicleService chronicleService,
			@Value("${theorcs.general.dateformat}") String dateFormat,
						   PictureService pictureService) {
		setRepository(newsRepository,News.class);
		this.chronicleService = chronicleService;
		this.dateFormat = dateFormat;
		this.pictureService = pictureService;
	}

	@Override
	public List<News> getAllNews(Chronicle chronicle) {
		List<News> newsList = ((NewsRepository)getRepository()).findAllByChronicle(chronicle);
		return newsList;
	}

	@Override
	public News createNew() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		News news = new News();
		 news.setDate(sdf.format(new Date()));
		return news;
	}

	@Override
	public void initData() {
		Chronicle chronicle = chronicleService.findByTitle("Terror on the Orient Express");
		News n1 = createNew();
		 n1.setChronicle(chronicle);
		 n1.setTitle("Le Prince Villon organise une grande soirée pour les 200 ans de son Règne");
		 n1.setContent(StringFormatService.LOREM_IPSUM);

		 Picture p1 = pictureService.getPicture(new ClassPathResource("static/images/demo/paris-bastille.jpg"));

		 n1.setPicture(p1);

		 save(n1);

		 News n2 = createNew();
		 n2.setChronicle(chronicle);
		 n2.setTitle("Gros accident de la route sur la A1");
		 n2.setContent(StringFormatService.LOREM_IPSUM);

		Picture p2 = pictureService.getPicture(new ClassPathResource("static/images/demo/paris-champs-elysees.jpg"));

		n2.setPicture(p2);

		save(n2);
	}

}
