package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.repositories.PictureRepository;
import org.libermundi.theorcs.services.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service("PictureService")
@Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRED)
public class PictureServiceImpl extends AbstractServiceImpl<Picture> implements PictureService {

    public PictureServiceImpl(PictureRepository pictureRepository) {
        setRepository(pictureRepository, Picture.class);
    }

    @Override
    public Picture savePicture(MultipartFile picture) {
        try {
            Picture picToSave = createNew();

            Byte[] data = new Byte[picture.getBytes().length];

            int i = 0;
            for (byte b : picture.getBytes()){
                data[i++] = b;
            }

            picToSave.setData(data);
            picToSave.setContentType(picture.getContentType());

            save(picToSave);

            return picToSave;

        } catch (IOException e) {
            log.error("Failed to save the Picture.", e);
        }
        return null;
    }

    @Override
    public Picture createNew() {
        return new Picture();
    }

    @Override
    public void initData() {
        //Nothing to do
    }
}
