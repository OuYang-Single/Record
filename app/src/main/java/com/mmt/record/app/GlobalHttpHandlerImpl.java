package com.mmt.record.app;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.http.GlobalHttpHandler;
import com.jess.arms.utils.DataHelper;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.api.Api;
import com.mmt.record.mvp.model.entity.User;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * ================================================
 * 展示 {@link GlobalHttpHandler} 的用法
 * <p>
 * Created by MVPArmsTemplate on 09/06/2019 20:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalHttpHandlerImpl implements GlobalHttpHandler {
    private Context context;
    List<User> users;

    Gson mGson;
    public GlobalHttpHandlerImpl(Context context) {
        this.context = context;
    }

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link Interceptor.Chain}
     * @param response   {@link Response}
     * @return
     */
    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
      try {
          if (mGson==null){
              mGson=new Gson();
          }
          com.mmt.record.mvp.model.entity.Request request=   mGson.fromJson(httpResult,com.mmt.record.mvp.model.entity.Request.class);
          if ("-999".equals(request.getCode())){
              OkHttpClient okHttpClient = new OkHttpClient();
              MediaType JSON = MediaType.parse("application/json; charset=utf-8");
              RequestBody body = RequestBody.create(JSON, new Gson().toJson(users.get(0)));
              Request requests = new Request.Builder()
                      .url(Api.APP_DOMAIN+"/rest/login")
                      .header("Content-Type","application/json")
                      .post(body)
                      .build();
              final Call call = okHttpClient.newCall(requests);

              Response execute=null;
              try {
                  execute=  call.execute();
                  try {
                      Gson gson=new Gson();
                      com.mmt.record.mvp.model.entity.Request<User> userRequest=  gson.fromJson(execute.body().string(), new TypeToken< com.mmt.record.mvp.model.entity.Request<User>>() {}.getType());
                      users.get(0).setToken(userRequest.getData().getToken());
                      ManagerFactory.getInstance().getStudentManager(context).update(users);

                  } catch (IOException e) {
                      e.printStackTrace();
                  }

              } catch (IOException e) {
                  e.printStackTrace();
              }
              Request request1=  chain.request();
              request1 .newBuilder().header("authToken", users.get(0).getToken());

              return chain.proceed(request1);
          }
          CrashReport.postCatchedException(new Throwable("日志内容:  请求回来"+httpResult));
      } catch (Exception e){}

        return response;
    }

    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link Interceptor.Chain}
     * @param request {@link Request}
     * @return
     */
    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {

        Request.Builder builder= chain.request().newBuilder();
        users= ManagerFactory.getInstance().getStudentManager(context).queryAll();
        if (users.size()>0){

            if (  users.get(0).getExpireTime()-System.currentTimeMillis()<=0) {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, new Gson().toJson(users.get(0)));
                Request requests = new Request.Builder()
                        .url(Api.APP_DOMAIN+"/rest/login")
                        .header("Content-Type","application/json")
                        .post(body)
                        .build();
                final Call call = okHttpClient.newCall(requests);

                Response execute=null;
                try {
                    execute=  call.execute();
                    try {
                        Gson gson=new Gson();
                        com.mmt.record.mvp.model.entity.Request<User> userRequest=  gson.fromJson(execute.body().string(), new TypeToken< com.mmt.record.mvp.model.entity.Request<User>>() {}.getType());
                        users.get(0).setToken(userRequest.getData().getToken());
                        ManagerFactory.getInstance().getStudentManager(context).update(users);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            builder  .header("authToken", users.get(0).getToken());
        }


        return  builder   .build();
    }
}
