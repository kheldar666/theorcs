package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.Picture;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends BaseRepository<Picture, Long> {

}
