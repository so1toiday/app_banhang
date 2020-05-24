package com.quyet.banhang.app_banhang.model;

import android.widget.EditText;

public class User
{
    private String UID;

    public String getUID() {
        return UID;
    }

    public User(String UID, String email, String name, String phone, String birthday, boolean sex) {
        this.UID = UID;
        Email = email;
        Name = name;
        Phone = phone;
        Birthday = birthday;
        Sex = sex;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private  String Email,Name,Phone,Birthday;
    private  boolean Sex;


    public User() {
    }

    public User(String email, String name, String phone, String birthday, boolean sex) {
        Email = email;
        Name = name;
        Phone = phone;
        Birthday = birthday;
        Sex = sex;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }


    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }
}
