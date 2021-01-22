package com.joel.a0800restinga.Model;

public class Users {
    private String Tel;
    private String End;
    private String Whatsapp;

    public Users(String tel, String end, String whatsapp) {
        Tel = tel;
        End = end;
        Whatsapp = whatsapp;
    }

    public String getWhatsapp() {
        return Whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        Whatsapp = whatsapp;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public Users() {

    }
}
