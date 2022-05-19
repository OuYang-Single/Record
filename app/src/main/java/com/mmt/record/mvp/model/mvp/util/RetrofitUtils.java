package com.mmt.record.mvp.model.mvp.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitUtils {
 
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
 
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }


    public static MultipartBody.Part createFilePart(String partName, File file) {
 
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
 
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
 
    public static MultipartBody createMultipartBody(Map<String, String> params, List<String> pic, String keyName) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        if (pic.size() > 0) {
            for (String image : pic) {
                File file = new File(image);
                if (file.exists()) {
                    String fileName = (file.getName().endsWith(".cnt") ? file.getName() + ".jpg" : file.getName());
                    builder.addFormDataPart(keyName, fileName, RequestBody.create(MediaType.parse("image/png"), file));
                }
            }
        }
        return builder.build();
    }
 
}