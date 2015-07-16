package com.esolz.aicafeapp.Datatype;

/**
 * Created by ltp on 16/07/15.
 */
public class RegistrationDataType {
    String id, name, sex, email, about, business, dob, photo, photo_thumb, registerDate, facebookid, last_sync, fb_pic_url, status, visible;

    public RegistrationDataType(String id, String name, String sex, String email,
                                String about, String business, String dob, String photo,
                                String photo_thumb, String registerDate, String facebookid,
                                String last_sync, String fb_pic_url, String status, String visible) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.about = about;
        this.business = business;
        this.dob = dob;
        this.photo = photo;
        this.photo_thumb = photo_thumb;
        this.registerDate = registerDate;
        this.facebookid = facebookid;
        this.last_sync = last_sync;
        this.fb_pic_url = fb_pic_url;
        this.status = status;
        this.visible = visible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto_thumb() {
        return photo_thumb;
    }

    public void setPhoto_thumb(String photo_thumb) {
        this.photo_thumb = photo_thumb;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getLast_sync() {
        return last_sync;
    }

    public void setLast_sync(String last_sync) {
        this.last_sync = last_sync;
    }

    public String getFb_pic_url() {
        return fb_pic_url;
    }

    public void setFb_pic_url(String fb_pic_url) {
        this.fb_pic_url = fb_pic_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}