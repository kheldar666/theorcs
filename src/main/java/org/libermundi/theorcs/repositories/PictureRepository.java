package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends UndeletableRepository<Picture, Long> {

}
