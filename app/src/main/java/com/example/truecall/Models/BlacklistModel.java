package com.example.truecall.Models;

public class BlacklistModel {
    private String phone;
    private String name;
    private String note;

    public BlacklistModel() {
    }

    public BlacklistModel(String phone, String name, String note) {
        this.phone = phone;
        this.name = name;
        this.note = note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
