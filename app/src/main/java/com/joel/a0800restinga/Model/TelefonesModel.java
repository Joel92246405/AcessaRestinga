package com.joel.a0800restinga.Model;

public class TelefonesModel {
    private String Tel;
    private String End;
    private String Whatsapp;
    private String email;
    private String Cat;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    public TelefonesModel(String Tel, String End, String Whatsapp, String email, String Cat) {
        this.Tel = Tel;
        this.End = End;
        this.Whatsapp = Whatsapp;
        this.email = email;
        this.Cat = Cat;
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

    public TelefonesModel() {

    }
}
