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

        sendResponse(PictureUtils.loadOriginalImage(picture), picture.getContentType(),response);
    }

    @GetMapping(value = {"/secure/chronicle/{chronicle}/picture/{picture}/thumbnail/{width}/{height}"})
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public void renderThumbnailWxH(HttpServletResponse response,
                              @PathVariable Chronicle chronicle, @PathVariable Picture picture,
                              @PathVariable int width, @PathVariable int height) throws ImageManipulationException, IOException {

        sendResponse(PictureUtils.thumbnail(picture,width,height),picture.getContentType(),response);
    }

    @GetMapping(value = {"/secure/chronicle/{chronicle}/picture/{picture}/thumbnail/{width}"})
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public void renderThumbnailW(HttpServletResponse response,
                              @PathVariable Chronicle chronicle, @PathVariable Picture picture,
                              @PathVariable int width) throws ImageManipulationException, IOException {

        sendResponse(PictureUtils.resizeImageByWidth(picture,width),picture.getContentType(),response);
    }

    private void sendResponse(BufferedImage image, String contentType, HttpServletResponse response) throws IOException {
        IOUtils.copy(PictureUtils.toInputStream(image, contentType), response.getOutputStream());
    }

}
