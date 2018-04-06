package com.romankarpov.leavebehindlayout.demo.model;

import java.util.Date;


public class Contact {
    private String name;
    private String surname;
    private String gender;
    private String region;
    private int age;
    private String title;
    private String phone;
    private Date birthday;
    private String email;
    private String photoUri;

    public Contact() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        surname = surname;
    }
    public String getFullName() {
        return String.format("%s %s", name, surname);
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        gender = gender;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        region = region;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        age = age;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        title = title;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        birthday = birthday;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        email = email;
    }

    public String getPhotoUri() {
        return photoUri;
    }
    public void setPhotoUri(String photoUri) {
        photoUri = photoUri;
    }
}
