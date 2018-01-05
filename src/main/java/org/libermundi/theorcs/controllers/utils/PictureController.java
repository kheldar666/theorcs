package org.libermundi.theorcs.controllers.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.exceptions.ImageManipulationException;
import org.libermundi.theorcs.utils.PictureUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class PictureController {

    @GetMapping(value = {"/secure/chronicle/{chronicle}/picture/{picture}"})
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public void renderFullPicture(HttpServletResponse response,
                  @PathVariable Chronicle chronicle, @PathVariable Picture picture) throws ImageManipulationException, IOException {

        BufferedImage image = PictureUtils.loadOriginalImage(picture);

        sendResponse(image,response);
    }

    @GetMapping(value = {"/secure/chronicle/{chronicle}/picture/{picture}/thumbnail/{width}/{height}"})
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public void renderPicture(HttpServletResponse response,
                              @PathVariable Chronicle chronicle, @PathVariable Picture picture,
                              @PathVariable int width, @PathVariable int height) throws ImageManipulationException, IOException {

        BufferedImage image = PictureUtils.thumbnail(picture,width,height);

        sendResponse(image,response);
    }

    private void sendResponse(BufferedImage image, HttpServletResponse response) throws IOException {
        IOUtils.copy(PictureUtils.toInputStream(image), response.getOutputStream());
    }

}
