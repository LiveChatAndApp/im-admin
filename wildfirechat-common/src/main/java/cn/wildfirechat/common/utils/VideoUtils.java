package cn.wildfirechat.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;

import cn.wildfirechat.common.model.dto.VideoInfoDTO;

public class VideoUtils {

	public static VideoInfoDTO getVideoInfo(MultipartFile file) {
		VideoInfoDTO videoInfo = null;
		try {
			InputStream in = new ByteArrayInputStream(file.getBytes());
			FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(in);
			fFmpegFrameGrabber.start();

			String base64 = Base64.getEncoder().encodeToString(generateThumb(fFmpegFrameGrabber).toByteArray());
			videoInfo = VideoInfoDTO.builder().duration(fFmpegFrameGrabber.getLengthInTime() / 1000).thumb(base64)
					.fileSize(getFileSize(file)).build();

			try {
				fFmpegFrameGrabber.stop();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videoInfo;
	}

	public static Long getFileSize(MultipartFile file) {
		try {
			return file.getSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static String getThumbBase64(MultipartFile file) {
		String base64;
		try {
			InputStream in = new ByteArrayInputStream(file.getBytes());
			FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(in);
			base64 = Base64.getEncoder().encodeToString(generateThumb(fFmpegFrameGrabber).toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return base64;
	}

	public static String getThumbBase64(String path) {
		String base64;
		try {
			FFmpegFrameGrabber fFmpegFrameGrabber = FFmpegFrameGrabber.createDefault(path);
			base64 = Base64.getEncoder().encodeToString(generateThumb(fFmpegFrameGrabber).toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return base64;
	}

	private static ByteArrayOutputStream generateThumb(FFmpegFrameGrabber fmpegFrameGrabber) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// 获取影片帧数
		int length = fmpegFrameGrabber.getLengthInFrames();
		int i = 0;

		Frame frame = null;

		while (i < length) {
			if (i > 10) {
				frame = fmpegFrameGrabber.grabImage();
				if (frame.image != null) {
					break;
				}
			}
			i++;
		}

		if (frame != null) {
			BufferedImage bufferedImage = zoomImage(new Java2DFrameConverter().getBufferedImage(frame), 0.5F);
			ImageIO.write(bufferedImage, "jpg", outputStream);
		}

		return outputStream;
	}

	private static IplImage rotate(IplImage src, int angle) {
		IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
		opencv_core.cvTranspose(src, img);
		opencv_core.cvFlip(img, img, angle);
		return img;
	}

	private static BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
		int width = im.getWidth();
		int height = im.getHeight();

		int targetWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
		int targetHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);
		BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(im.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}
}
