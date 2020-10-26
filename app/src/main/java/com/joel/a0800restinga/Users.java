package com.joel.a0800restinga;

public class Users {
    private String Tel;
    private String End;

    public Users(String tel, String end) {
        Tel = tel;
        End = end;
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
