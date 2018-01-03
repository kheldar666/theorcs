package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.repositories.NewsRepository;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Implementation of NewsService
 *
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)
public class NewsServiceImpl extends AbstractServiceImpl<News> implements NewsService {

	private final ChronicleService chronicleService;

	public NewsServiceImpl(NewsRepository newsRepository, ChronicleService chronicleService) {
		setRepository(newsRepository,News.class);
		this.chronicleService = chronicleService;
	}

	@Override
	public List<News> getAllNews(Chronicle chronicle) {
		List<News> newsList = ((NewsRepository)getRepository()).findAllByChronicle(chronicle);
		return newsList;
	}

	@Override
	public News createNew() {
		News news = new News();
		 news.setDate(new Date());
		return news;
	}

	@Override
	public void initData() {
		Chronicle chronicle = chronicleService.findByTitle("Terror on the Orient Express");
		News n1 = createNew();
		 n1.setChronicle(chronicle);
		 n1.setTitle("Le Prince Villon organise une grande soirée pour la 200 ans de son Règne");
		 n1.setContent("\n" +
				 "\n" +
				 "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc cursus dolor id posuere laoreet. Integer gravida lectus nunc, aliquam ornare felis lobortis non. Fusce pretium quis ipsum condimentum luctus. Nam nec eros a massa feugiat molestie. Aliquam nec tincidunt lorem. Nam in elit sit amet erat commodo pulvinar quis at purus. Aenean id pharetra nunc, at suscipit mauris. Suspendisse vel rhoncus lacus, non fringilla nisl.\n" +
				 "\n" +
				 "Praesent facilisis justo vitae tellus laoreet vulputate. Sed volutpat elementum lacus, et sollicitudin justo feugiat vitae. Pellentesque dictum suscipit eros at placerat. Integer eget facilisis nisl. Proin porttitor ante vitae leo aliquet ornare. Curabitur mollis nisl vel lectus accumsan, quis finibus est vulputate. Cras vitae venenatis quam, eget vehicula nulla. Sed hendrerit mauris in magna accumsan, a porttitor nisl tincidunt. Duis non convallis dolor. Duis mollis lorem eu rhoncus mollis. Suspendisse interdum felis vel laoreet faucibus.\n" +
				 "\n" +
				 "Curabitur dapibus rhoncus elit, vitae maximus enim luctus eget. Praesent sem purus, vehicula a sollicitudin vitae, molestie eget nisi. Vestibulum vitae venenatis magna. Curabitur eget sapien lacus. Suspendisse id sapien vitae lorem ultricies efficitur. Sed venenatis, ligula vel aliquam luctus, leo augue accumsan ligula, eget egestas ligula risus molestie diam. Vestibulum eu nunc est. In hac habitasse platea dictumst.\n" +
				 "\n" +
				 "Morbi mollis maximus turpis eu accumsan. Nulla vel sollicitudin sapien. Integer tempor finibus lorem et elementum. Donec maximus cursus massa, ut hendrerit enim pharetra a. Phasellus nisi nisl, accumsan vitae varius et, maximus nec nunc. Quisque lacinia ipsum at iaculis dignissim. Morbi non aliquam magna, eget cursus purus. Pellentesque vel leo vestibulum, suscipit tortor vel, pretium est. Nullam id ultrices sem, vestibulum dictum ipsum. Suspendisse lectus felis, ornare nec elit et, feugiat convallis nibh.\n" +
				 "\n" +
				 "Sed aliquam quam urna, ut venenatis ex aliquam ut. Curabitur tempus sodales faucibus. Nam condimentum neque nec sem sodales aliquet. Integer sit amet tellus a eros congue congue et et urna. Quisque suscipit interdum tellus bibendum posuere. Quisque pulvinar massa eu enim condimentum congue. Donec a hendrerit odio. Sed rutrum varius urna eget mattis. Etiam vestibulum risus sit amet odio vestibulum commodo. Ut vehicula, lacus ac malesuada condimentum, urna dui elementum eros, quis accumsan dui justo sed tortor. Vivamus convallis convallis augue, et fermentum mi luctus et. Ut in fringilla mi. Maecenas eleifend nisi ut erat sodales pretium. ");


		 save(n1);

		 News n2 = createNew();
		 n2.setChronicle(chronicle);
		 n2.setTitle("Gros accident de la route sur la A1");
		 n2.setContent("\n" +
				 "\n" +
				 "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc cursus dolor id posuere laoreet. Integer gravida lectus nunc, aliquam ornare felis lobortis non. Fusce pretium quis ipsum condimentum luctus. Nam nec eros a massa feugiat molestie. Aliquam nec tincidunt lorem. Nam in elit sit amet erat commodo pulvinar quis at purus. Aenean id pharetra nunc, at suscipit mauris. Suspendisse vel rhoncus lacus, non fringilla nisl.\n" +
				 "\n" +
				 "Praesent facilisis justo vitae tellus laoreet vulputate. Sed volutpat elementum lacus, et sollicitudin justo feugiat vitae. Pellentesque dictum suscipit eros at placerat. Integer eget facilisis nisl. Proin porttitor ante vitae leo aliquet ornare. Curabitur mollis nisl vel lectus accumsan, quis finibus est vulputate. Cras vitae venenatis quam, eget vehicula nulla. Sed hendrerit mauris in magna accumsan, a porttitor nisl tincidunt. Duis non convallis dolor. Duis mollis lorem eu rhoncus mollis. Suspendisse interdum felis vel laoreet faucibus.\n" +
				 "\n" +
				 "Curabitur dapibus rhoncus elit, vitae maximus enim luctus eget. Praesent sem purus, vehicula a sollicitudin vitae, molestie eget nisi. Vestibulum vitae venenatis magna. Curabitur eget sapien lacus. Suspendisse id sapien vitae lorem ultricies efficitur. Sed venenatis, ligula vel aliquam luctus, leo augue accumsan ligula, eget egestas ligula risus molestie diam. Vestibulum eu nunc est. In hac habitasse platea dictumst.\n" +
				 "\n" +
				 "Morbi mollis maximus turpis eu accumsan. Nulla vel sollicitudin sapien. Integer tempor finibus lorem et elementum. Donec maximus cursus massa, ut hendrerit enim pharetra a. Phasellus nisi nisl, accumsan vitae varius et, maximus nec nunc. Quisque lacinia ipsum at iaculis dignissim. Morbi non aliquam magna, eget cursus purus. Pellentesque vel leo vestibulum, suscipit tortor vel, pretium est. Nullam id ultrices sem, vestibulum dictum ipsum. Suspendisse lectus felis, ornare nec elit et, feugiat convallis nibh.\n" +
				 "\n" +
				 "Sed aliquam quam urna, ut venenatis ex aliquam ut. Curabitur tempus sodales faucibus. Nam condimentum neque nec sem sodales aliquet. Integer sit amet tellus a eros congue congue et et urna. Quisque suscipit interdum tellus bibendum posuere. Quisque pulvinar massa eu enim condimentum congue. Donec a hendrerit odio. Sed rutrum varius urna eget mattis. Etiam vestibulum risus sit amet odio vestibulum commodo. Ut vehicula, lacus ac malesuada condimentum, urna dui elementum eros, quis accumsan dui justo sed tortor. Vivamus convallis convallis augue, et fermentum mi luctus et. Ut in fringilla mi. Maecenas eleifend nisi ut erat sodales pretium. ");

		 save(n2);
	}

}
