package org.libermundi.theorcs.services.utils;

import org.libermundi.theorcs.domain.jpa.utils.Picture;
import org.libermundi.theorcs.services.base.BaseService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService extends BaseService<Picture> {
    Picture savePicture(MultipartFile picture);
    Picture getPicture(Resource resource);
}
