package org.libermundi.theorcs.services;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService extends BaseService<Picture> {
    Picture savePicture(MultipartFile picture);
    Picture getPicture(Resource resource);
}
