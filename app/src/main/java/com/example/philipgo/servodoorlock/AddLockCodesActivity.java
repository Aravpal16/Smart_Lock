package com.example.philipgo.servodoorlock;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;


public class AddLockCodesActivity implements Serializable{

    private String mName, mNumber;


    public AddLockCodesActivity(String name, String number) {
        this.mName = name;
        this.mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

}
