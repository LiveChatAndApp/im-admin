package cn.wildfirechat.admin.utils;

import org.springframework.stereotype.Component;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class QiniuUtils {
	@NacosValue(value = "${qiniu.accessKey}", autoRefreshed = true)
	private String accessKey;

	@NacosValue(value = "${qiniu.secretKey}", autoRefreshed = true)
	private String secretKey;

	@NacosValue(value = "${qiniu.bucket}", autoRefreshed = true)
	private String bucket;

	@NacosValue(value = "${qiniu.bucket_url}", autoRefreshed = true)
	private String bucketUrl;

	public String uploadFile(MultipartFile file, String fileName) {
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket, fileName);
		try {
			Response response = getUploadManager().put(file.getBytes(), fileName, upToken);
			if (response.isOK()) {
				return fileName;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private UploadManager getUploadManager() {
		com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.regionAs0());
		cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;
		return new UploadManager(cfg);
	}
}
