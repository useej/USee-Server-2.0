package com.usee.utils;
import com.qiniu.api.auth.AuthException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;


/**
 * Created by WR on 2016.
 */
public class QiniuTest {
    private QiniuServiceImpl qiniuService = null;

    @Test
//    @Before
    public void init() {
        qiniuService = new QiniuServiceImpl();
        //设置AccessKey
        qiniuService.setAccessKey("W3VCxhIZCNSidAA-oYBOYiFHMMlN0i4BGdrJaIB5");
        //设置SecretKey
        qiniuService.setSecretKey("HrJr_Rq5DHT7Lp1WVKVVsf6nP-UADXIcSylRvyFP");
        //设置存储空间
        qiniuService.setBucketName("usee");
        //设置七牛域名
        qiniuService.setDomain("odok0w4o2.bkt.clouddn.com");
    }

 //   @Test
    public void testUpload() throws AuthException, JSONException {
        File file = new File("D:/qiche.jpg");
        String uptoken = qiniuService.getUpToken();
        System.out.println(uptoken);
//        qiniuService.uploadFile(file);
    }

 //   @Test
    public void testDownloadFileUrl() throws Exception {
        String filePath = qiniuService.getDownloadFileUrl("qiche.jpg");
        System.out.println("file path == " + filePath);
    }
}
