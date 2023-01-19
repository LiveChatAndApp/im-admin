package cn.wildfirechat.admin.service;

import cn.wildfirechat.common.utils.FileNameUtils;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
public class QINIUUploadTest {

    @NacosValue(value = "${qiniu.accessKey}", autoRefreshed = true)
    private String accessKey;

    @NacosValue(value = "${qiniu.secretKey}", autoRefreshed = true)
    private String secretKey;

    @NacosValue(value = "${qiniu.bucket}", autoRefreshed = true)
    private String bucket;

    @NacosValue(value = "${qiniu.bucket_url}", autoRefreshed = true)
    private String bucket_url;


    //一般上傳到本地
    public static String upload(MultipartFile file, String uploadRootPath, String subDirPath,
                                String fileOriginName, String fileNamePrefix){

        String newFileName = FileNameUtils.getFileName(fileOriginName,fileNamePrefix);// 生成新的文件名 ex. PCXXXXXXXX.jpg
        String uriPath = subDirPath.concat("/").concat(newFileName); //uri访问路徑(存入DB) ex. /paychannel/PCXXXXXXXX.jpg
        String realPath = uploadRootPath.concat(uriPath); //上传路径 ex. /root/picture/paychannel/PCXXXXXXXX.jpg

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
        log.info("檔案名:{}, 上传路径:{}, 挡案uri路徑:{}",newFileName,realPath,uriPath);
        try {
            //保存文件
            file.transferTo(dest);
            log.info("完成上传:{}",newFileName);
            return uriPath;
        } catch (IOException e) {
            log.error("檔案名:{} 上傳圖片至路徑:{} 失敗",fileOriginName,realPath);
            e.printStackTrace();
            return null;
        }
    }

    public void uploadQINIU(String localFilePath, String fileName){
        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.xinjiapo());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.resumableUploadMaxConcurrentTaskCount = 2; // 设置分片上传并发，1：采用同步上传；大于1：采用并发上传
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        //定义这个返回的JSON格式的内容
        {
//            StringMap putPolicy = new StringMap();
//            putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
//            long expireSeconds = 3600;
//            String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        }



        //上传自定义参数，自定义参数名称需要以 x:开头
        {
//            StringMap params = new StringMap();
//            params.put("x:fname","123.jpg");
//            params.put("x:age",20);
//            //上传策略
//            StringMap policy = new StringMap();
//            //自定义上传后返回内容，返回自定义参数，需要设置 x:参数名称，注意下面
//            policy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fname\":\"$(x:fname)\",\"age\",$(x:age)}");
//            //生成上传token
//            String upToken = auth.uploadToken(bucket, fileName, 3600, policy);
        }


        //带数据处理的凭证
        {
//        StringMap putPolicy = new StringMap();
//        //数据处理指令，支持多个指令
//        String saveMp4Entry = String.format("%s:avthumb_test_target.mp4", bucket);
//        String saveJpgEntry = String.format("%s:vframe_test_target.jpg", bucket);
//        String avthumbMp4Fop = String.format("avthumb/mp4|saveas/%s", UrlSafeBase64.encodeToString(saveMp4Entry));
//        String vframeJpgFop = String.format("vframe/jpg/offset/1|saveas/%s", UrlSafeBase64.encodeToString(saveJpgEntry));
//        //将多个数据处理指令拼接起来
//        String persistentOpfs = StringUtils.join(new String[]{
//                avthumbMp4Fop, vframeJpgFop
//        }, ";");
//        putPolicy.put("persistentOps", persistentOpfs);
//        //数据处理队列名称，必填
//        putPolicy.put("persistentPipeline", "mps-pipe1");
//        //数据处理完成结果通知地址
//        putPolicy.put("persistentNotifyUrl", "http://api.example.com/qiniu/pfop/notify");
//        long expireSeconds = 3600;
//        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
//        System.out.println(upToken);
        }





        try {
            File file = new File(localFilePath);
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
            Response response = uploadManager.put(byteInputStream, fileName, upToken, null,null);

//            Response response = uploadManager.put(localFilePath, fileName, upToken, params,null,false);//若允许添加额外参数，可以将参数设置为false
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
//            System.err.println(r.toString());
            try {
//                System.err.println(r.bodyString());
                log.error("檔案名:{} 七牛上傳圖片 response:[} 失敗原因:[}",fileName, r.toString(), r.bodyString());
            } catch (QiniuException ex2) {
                log.error("檔案名:{} 七牛上傳圖片 失敗原因解析失敗", fileName);
                //ignore
            }
        } catch (Exception ex) {
            log.error("檔案名:{} 七牛上傳圖片 失敗原因解析失敗", fileName);
        }
    }

    public void uploadQINIU2(String localFilePath, String fileName){
        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.xinjiapo());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.resumableUploadMaxConcurrentTaskCount = 2; // 设置分片上传并发，1：采用同步上传；大于1：采用并发上传

        try {
            String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            //...生成上传凭证，然后准备上传
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(localFilePath, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                try {
                    log.error("檔案名:{} 七牛上傳圖片 response:[} 失敗原因:[}",fileName, r.toString(), r.bodyString());
                } catch (QiniuException ex2) {
                    log.error("檔案名:{} 七牛上傳圖片 失敗原因解析失敗", fileName);
                    //ignore
                }
            } catch (Exception ex) {
                log.error("檔案名:{} 七牛上傳圖片 失敗原因解析失敗", fileName);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    @Ignore
    @Test
    public void test() {

//        uploadQINIU("C:/Users/ethan/Desktop/photo/avatar_taxi.jpg","upload/photo/avatar_taxi.jpg");
//        uploadQINIU("C:/Users/ethan/Desktop/video/Soft Lipa 蛋堡-回到過去.mp4","upload/video/Soft Lipa 蛋堡-回到過去.mp4");

//        uploadQINIU2("C:/Users/ethan/Desktop/photo/avatar_taxi.jpg","upload/photo/avatar_taxi.jpg");
//        uploadQINIU2("C:/Users/ethan/Desktop/video/Soft Lipa 蛋堡-回到過去.mp4","upload/video/Soft Lipa 蛋堡-回到過去.mp4");
    }
}
