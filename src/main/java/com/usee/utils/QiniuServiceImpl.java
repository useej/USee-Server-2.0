package com.usee.utils;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rs.URLUtils;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by WR on 2016.
 *
 */
public class QiniuServiceImpl {

    private String bucketName;
    private String domain;

    public void setDomain(String domain) {

        this.domain = domain;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    //设置AccessKey
    public void setAccessKey(String key) {
        Config.ACCESS_KEY = key;
    }
    //设置SecretKey
    public void setSecretKey(String key) {
        Config.SECRET_KEY = key;
    }
    //通过文件路径上传文件
    public boolean uploadFile(String localFile) throws AuthException, JSONException {
        File file = new File(localFile);
        return uploadFile(file);
    }

    //通过File上传
    public boolean uploadFile(File file) throws AuthException, JSONException {
        String uptoken = getUpToken();

        // 可选的上传选项，具体说明请参见使用手册。
        PutExtra extra = new PutExtra();

        // 上传文件
        PutRet ret = IoApi.putFile(uptoken, file.getName(), file.getAbsolutePath(), extra);

        if (ret.ok()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 从 inputstream 中写入七牛
     * @param key 文件名
     * @param content 要写入的内容
     * @return
     * @throws AuthException
     * @throws JSONException
     */
    public boolean uploadFile(String key, String content) throws AuthException, JSONException {
        // 读取的时候按的二进制，所以这里要同一
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        String uptoken = getUpToken();

        // 可选的上传选项，具体说明请参见使用手册。
        PutExtra extra = new PutExtra();

        // 上传文件
        PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);

        if (ret.ok()) {
            return true;
        } else {
            return false;
        }
    }

    //获得下载地址
    public String getDownloadFileUrl(String filename) throws Exception {
        Mac mac = getMac();
        String baseUrl = URLUtils.makeBaseUrl(domain, filename);
        GetPolicy getPolicy = new GetPolicy();
        String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
        return downloadUrl;
    }
  //删除文件
    public void deleteFile(String filename) {
        Mac mac = getMac();
        RSClient client = new RSClient(mac);
        client.delete(domain, filename);
    }

    //获取凭证
    public String getUpToken() throws AuthException, JSONException {
        Mac mac = getMac();
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = putPolicy.token(mac);
        return uptoken;
    }

    private Mac getMac() {
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        return mac;
    }

}
