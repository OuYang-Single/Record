package com.mmt.record.mvp.model.api;



import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * <p>
 * Created by MVPArmsTemplate on 09/06/2019 20:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface Api {
    String APP_DOMAIN = "http://202.202.64.4:82";

    @Headers({"Content-Type: application/json"})//需要添加
    @POST("/rest/login")
    Observable<Request<User>> login(@Body User mAuthorizationUser);

    @Headers({"Content-Type: application/json"})//需要添加
    @POST("/rest/gps/upload")
    Observable<Request> gpsUpload(@Body GpsEntity gpsEntity);


    @POST("/rest/file/upload")
    @Multipart
    Observable<Request> upload(@Part("device_id") RequestBody  params,@Part MultipartBody.Part  part);


}
