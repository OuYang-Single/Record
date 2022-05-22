package com.mmt.record.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Request<T> implements Parcelable {
    private T data;
    private String code;
    private String key;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Request(){

    }

    protected Request(Parcel in) {
        code = in.readString();
        key = in.readString();
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(key);
        dest.writeString(msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public String toString() {
        return "Request{" +
                "data=" + data +
                ", code=" + code +
                ", key='" + key + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
