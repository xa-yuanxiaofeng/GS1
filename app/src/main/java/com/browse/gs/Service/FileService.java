package com.browse.gs.Service;

import android.util.Log;

import com.browse.gs.util.ConfigConstant;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileService {
    boolean ret = false;

    public FileService() {

    }

    public boolean uploadFile(String fileName) {
        _uploadFile(fileName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("------:", "图片上传失败！");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("------:", "图片上传成功！");
                ret = true;
            }
        });
        return ret;
    }

    //上传文件
    private int _uploadFile(String fileName, Callback callback) {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        File file = new File(fileName);
        OkHttpClient client = new OkHttpClient();
        // 上传文件使用MultipartBody.Builder
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "yxf") // 提交普通字段
                .addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .build();
        // POST请求
        Request request = new Request.Builder()
                .url(ConfigConstant.serverPort + "/sign/upload")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
        return 1;
    }
}
