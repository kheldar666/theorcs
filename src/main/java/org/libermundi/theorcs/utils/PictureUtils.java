package org.libermundi.theorcs.utils;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.exceptions.ImageManipulationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class PictureUtils {

	public static BufferedImage resizeImageByWidth(Picture picture, int width) throws ImageManipulationException {
		return resizeImageByWidth(loadOriginalImage(picture),width);
	}

	public static BufferedImage resizeImageByHeight(Picture picture, int height) throws ImageManipulationException {
		return resizeImageByHeight(loadOriginalImage(picture),height);
	}

	public static BufferedImage resizeImage(Picture picture, int width, int height) throws ImageManipulationException {
		return resizeImage(loadOriginalImage(picture), width, height);
	}
	
	public static BufferedImage cropImage(Picture picture, int targetWidth, int targetHeight) throws ImageManipulationException {
		return cropImage(loadOriginalImage(picture), targetWidth, targetHeight);
	}
	
	public static BufferedImage thumbnail(Picture picture, int width, int height) throws ImageManipulationException {
		return thumbnail(loadOriginalImage(picture), width, height);
	}

	public static BufferedImage fitIn(Picture picture, int width, int height) throws ImageManipulationException {
		return fitIn(loadOriginalImage(picture),width,height);
	}

	public static BufferedImage loadOriginalImage(Picture picture) throws ImageManipulationException {
		try {
			byte[] byteArray = new byte[picture.getData().length];
			int i = 0;
			for (Byte wrappedByte : picture.getData()){
				byteArray[i++] = wrappedByte; //auto unboxing
			}
			InputStream is = new ByteArrayInputStream(byteArray);
			return ImageIO.read(is);
		} catch (IOException ioe) {
			log.error("Failed to load image : " + picture.toString(), ioe);
			throw new ImageManipulationException(ioe);
		}
	}

	public static InputStream toInputStream(BufferedImage image, String contentType) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, toFormat(contentType), os);
		return new ByteArrayInputStream(os.toByteArray());
	}


	/********************** PRIVATE METHODS **********************/

	private PictureUtils() {}


	private static BufferedImage fitIn(BufferedImage image, int width, int height) {
		BufferedImage fitIn;
		double ratioTmb = (double)height / (double)width;
		if(getRatio(image) > ratioTmb) {
			fitIn = resizeImageByWidth(image, width);
		} else {
			fitIn = resizeImageByHeight(image, height);
		}
		return fitIn;
	}

	private static BufferedImage resizeImageByWidth(BufferedImage originalImage, int width){
		int height = (int)Math.round(width * getRatio(originalImage));
		return resizeImage(originalImage, width, height);
	}


	private static BufferedImage thumbnail(BufferedImage image, int width, int height) {
		BufferedImage thumb;
		thumb = fitIn(image, width, height);
		thumb = cropImage(thumb, width, height);
		return thumb;
	}

	private static double getRatio(BufferedImage image){
		double originalWidth = image.getWidth();
		double originalHeight = image.getHeight();
		double ratio = originalHeight / originalWidth;
		return ratio;
	}

	private static BufferedImage cropImage(BufferedImage image, int targetWidth, int targetHeight) {
		double avatarWidth = image.getWidth();
		double avatarHeight = image.getHeight();

		double finalWidth = targetWidth;
		double finalHeight = targetHeight;
		int cropX = 0;
		int cropY = 0;
		if (avatarWidth > avatarHeight) {
			finalWidth = Math.floor((targetHeight / avatarHeight)
					* avatarWidth);
			cropX = (int) Math.floor((finalWidth - targetWidth) / 2);
		} else if (avatarWidth < avatarHeight) {
			finalHeight = Math.floor((finalWidth / avatarWidth)
					* avatarHeight);
			cropY = (int) Math.floor((finalHeight - targetHeight) / 2);
		}

		return Scalr.crop(image, cropX, cropY, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
	}

	private static BufferedImage resizeImageByHeight(BufferedImage originalImage, int height){
		int width = (int)Math.round(height / getRatio(originalImage));

		return resizeImage(originalImage, width, height);
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height){
		return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, height, Scalr.OP_ANTIALIAS);
	}

	private static String toFormat(String contentType){
		switch (contentType) {
			case "image/jpeg":
				return "jpg";
			case "image/png":
				return "png";
			case "image/gif":
				return "gif";
			default:
				return "jpg";
		}
	}

}
