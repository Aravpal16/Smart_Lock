package com.example.philipgo.servodoorlock;

public class Register {
    public String name,email,password,secert_code,token_id;

    public Register()
    {

    }

    public Register(String name,String email,String password,String secert_code,String token_id)
    {
        this.name=name;
        this.email=email;
        this.password=password;
        this.secert_code=secert_code;
        this.token_id=token_id;
    }
    public Register(String name,String email,String password,String token_id)
    {
        this.name=name;
        this.email=email;
        this.password=password;
        this.token_id=token_id;
    }


}
