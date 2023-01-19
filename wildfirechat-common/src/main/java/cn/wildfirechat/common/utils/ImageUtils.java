package cn.wildfirechat.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {

	public static final String IMAGE_PNG = "png";
	public static final String IMAGE_JPG = "jpg";

	public static BufferedImage zoomImage(File file, float resizeTimes) throws IOException {
		return zoomImage(ImageIO.read(file), resizeTimes);
	}

	public static BufferedImage zoomImage(MultipartFile file, float resizeTimes) throws IOException {
		BufferedImage srcImage = null;
		try {
			InputStream in = new ByteArrayInputStream(file.getBytes());
			srcImage = ImageIO.read(in);
		} catch (IOException e) {
			log.error("ZoomImage convert MultiPartFile to InputStream failed", e);
		}
		return zoomImage(srcImage, resizeTimes);
	}

	public static BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
		int width = im.getWidth();
		int height = im.getHeight();

		int targetWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
		int targetHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);
		BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(im.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	public static String convertToBase64(BufferedImage bufferedImage, String formatName) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, formatName, outputStream);
		return Base64.getEncoder().encodeToString(outputStream.toByteArray());
	}
}
