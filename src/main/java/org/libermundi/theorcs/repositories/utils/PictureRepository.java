package org.libermundi.theorcs.repositories.utils;

import org.libermundi.theorcs.domain.jpa.utils.Picture;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends UndeletableRepository<Picture, Long> {

}
