package org.libermundi.theorcs.controllers.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    @GetMapping({"/secure/chronicle/{chronicle}/image/{picture}"})
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public void renderImage(HttpServletResponse response,
                  @PathVariable Chronicle chronicle, @PathVariable Picture picture) throws IOException {
        byte[] byteArray = new byte[picture.getData().length];
        int i = 0;

        for (Byte wrappedByte : picture.getData()){
            byteArray[i++] = wrappedByte; //auto unboxing
        }

        response.setContentType(picture.getContentType());
        InputStream is = new ByteArrayInputStream(byteArray);
        IOUtils.copy(is, response.getOutputStream());
    }

}
