package cn.wildfirechat.admin.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.common.net.MediaType;

import cn.wildfirechat.common.i18n.I18nBase;
import cn.wildfirechat.common.model.enums.MediaUploadEnum;
import cn.wildfirechat.common.support.Assert;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.common.utils.FileNameUtils;
import cn.wildfirechat.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UploadFileUtils {

	@NacosValue(value = "${media.upload_target}", autoRefreshed = true)
	private Integer uploadTarget;

	@NacosValue(value = "${http.file.path}", autoRefreshed = true)
	private String httpPath;

	@NacosValue(value = "${upload.real.path}", autoRefreshed = true)
	private String uploadPath;

	@NacosValue(value = "${http.file.path.domain.variable}", autoRefreshed = true)
	private String domainReplaceString;

	@NacosValue(value = "${qiniu.bucket_url}", autoRefreshed = true)
	private String qiniuBucketUrl;

	@Autowired
	private QiniuUtils qiniuUtils;

	@Resource
	protected SpringMessage message;

	public String uploadFile(MultipartFile file, String subDirPath, String fileNamePrefix) {
		return uploadFile(file, subDirPath, fileNamePrefix, null);
	}

	public String uploadFile(MultipartFile file, String subDirPath, String fileNamePrefix,
			List<MediaType> allowExtension) {
		String urlPath = "";
		MediaUploadEnum mediaUploadEnum = MediaUploadEnum.parse(uploadTarget);
		if (file != null && !file.isEmpty()) {
			if (allowExtension == null) {
				allowExtension = new ArrayList<>();
			}
			boolean allowUpload = allowExtension.isEmpty();

			String fileMediaType = Arrays.stream(file.getContentType().split("/")).findFirst().orElse("");

			for (MediaType mediaType : allowExtension) {
				if (Objects.equals(fileMediaType, mediaType.type())) {
					allowUpload = true;
					break;
				}
			}

			Assert.isTrue(allowUpload, message.get(I18nBase.UPLOAD_DENIED_FILE));
			try {
				if (mediaUploadEnum == MediaUploadEnum.LOCAL) {
					urlPath = FileUtils.upload(file, uploadPath, subDirPath, file.getOriginalFilename(),
							fileNamePrefix);
					log.info("[Upload Local] path: {}", urlPath);
				} else if (mediaUploadEnum == MediaUploadEnum.QINIU) {
					String fileName = FileNameUtils.getFileName(file.getOriginalFilename(), fileNamePrefix);
					urlPath = qiniuUtils.uploadFile(file, subDirPath.substring(1).concat("/" + fileName));
					log.info("[Upload Qiniu] path: {}", urlPath);
					urlPath = "/" + urlPath;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return StringUtils.isEmpty(urlPath) ? "" : domainReplaceString + urlPath;
	}

	/**
	 * 将档案路径转换为Url {{@param source}} -> {{domain}}/{{@param source}}
	 *
	 */
	public String parseFilePathToUrl(String source) {
		return source.replace(domainReplaceString, getDomain());
	}

	/**
	 * 将档案Url转换为Path {{domain}}/{{@param source}} ->
	 * {@value httpPath}/{{@param source}}
	 *
	 */
	public String parseFileUrlToPath(String source) {
		return source.replace(getDomain(), domainReplaceString);
	}

	public String getDomain() {
		MediaUploadEnum mediaUploadEnum = MediaUploadEnum.parse(uploadTarget);
		String host = "";
		if (mediaUploadEnum == MediaUploadEnum.LOCAL) {
			host = httpPath;
		} else if (mediaUploadEnum == MediaUploadEnum.QINIU) {
			host = qiniuBucketUrl;
		}
		return host;
	}
}
