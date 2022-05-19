package com.mmt.record.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import retrofit2.HttpException;
import retrofit2.Response;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * ================================================
 * 展示 {@link ResponseErrorListener} 的用法
 * <p>
 * Created by MVPArmsTemplate on 09/06/2019 20:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {
    EVENT_KEY EventName ;
    public static enum EVENT_KEY {
        Network_Unavailable(1),
        server_error(3),
        time_out(2);

        private int value;
        private EVENT_KEY(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
    @Override
    public void handleResponseError(Context context, Throwable throwable) {

        //Log.w("handleResponseError",throwable.getCause().getMessage()+"") ;
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        String msg = "未知错误";
        EventName=EVENT_KEY.server_error;

        if (throwable instanceof UnknownHostException) {
            msg = "网络不可用";
            EventName=EVENT_KEY.Network_Unavailable;
        } else if (throwable instanceof SocketTimeoutException) {
            msg = "请求网络超时";
            EventName=EVENT_KEY.time_out;
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            msg = convertStatusCode(httpException);
            EventName=EVENT_KEY.server_error;
        } else if (throwable instanceof JsonParseException || throwable instanceof ParseException || throwable instanceof JSONException || throwable instanceof JsonIOException) {
            msg = "数据解析错误";
            EventName=EVENT_KEY.server_error;
        }/*retrofit2.adapter.rxjava2.HttpException: HTTP 400*/
      //  ArmsUtils.snackbarText(msg);
        if (!isNetworkConnected(context)) {
            msg = "当前网络不可用，请检查网络设置";
            EventName=EVENT_KEY.Network_Unavailable;
        }

       // Toast.makeText(context, "Tiempo de espera de la red", Toast.LENGTH_SHORT).show();
        //EventBus.getDefault().post(new ErrorEntity(EventName,msg));

    }
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    private String convertStatusCode(HttpException httpException) {
        String msg="未知错误";
        Response response = httpException.response();
     //   response.errorBody
        msg= response.message();
      //  msg=    mRequest.getMessage();

       if (httpException.code() == 500) {
            msg = "服务器发生错误";

        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        }else if (httpException.code() == 400) {
            msg = "错误请求";
        }  else {
            response = httpException.response();
            response.message();
        }
        return msg;
    }
}
