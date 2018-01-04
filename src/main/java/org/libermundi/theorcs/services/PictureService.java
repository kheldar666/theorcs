package org.libermundi.theorcs.services;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService extends BaseService<Picture> {
    public Picture savePicture(MultipartFile picture);
}
