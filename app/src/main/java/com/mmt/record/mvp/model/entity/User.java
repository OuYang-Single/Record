package com.mmt.record.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class User implements Parcelable {
    @Id
    private  Long id;

    private String userId;
    private String userName;

    private String passWord;

    private String email;

    private String phone;

    private String question;

    private String avatar;

    private String answer;
    private long expireTime;

    private Integer role;

    private long createTime;

    private long updateTime;
    private String token;


    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userId = in.readString();
        userName = in.readString();
        passWord = in.readString();
        email = in.readString();
        phone = in.readString();
        question = in.readString();
        avatar = in.readString();
        answer = in.readString();
        expireTime = in.readLong();
        if (in.readByte() == 0) {
            role = null;
        } else {
            role = in.readInt();
        }
        createTime = in.readLong();
        updateTime = in.readLong();
        token = in.readString();
    }

    @Generated(hash = 366782162)
    public User(Long id, String userId, String userName, String passWord,
            String email, String phone, String question, String avatar,
            String answer, long expireTime, Integer role, long createTime,
            long updateTime, String token) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.avatar = avatar;
        this.answer = answer;
        this.expireTime = expireTime;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.token = token;
    }

    @Generated(hash = 586692638)
    public User() {
    }





    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(passWord);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(question);
        parcel.writeString(avatar);
        parcel.writeString(answer);
        parcel.writeLong(expireTime);
        if (role == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(role);
        }
        parcel.writeLong(createTime);
        parcel.writeLong(updateTime);
        parcel.writeString(token);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

  
}
