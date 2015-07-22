package com.esolz.aicafeapp.Datatype;

/**
 * Created by ltp on 21/07/15.
 */
public class ChatViewDataType {
    String chat_id, send_from, send_to, message, type, stickername, chat_time,
            chat_date, status, file_link, file_available, name, photo, photo_thumb;

    public ChatViewDataType(String chat_id, String send_from, String send_to, String message, String type,
                            String stickername, String chat_time, String chat_date, String status, String file_link,
                            String file_available, String name, String photo, String photo_thumb) {
        this.chat_id = chat_id;
        this.send_from = send_from;
        this.send_to = send_to;
        this.message = message;
        this.type = type;
        this.stickername = stickername;
        this.chat_time = chat_time;
        this.chat_date = chat_date;
        this.status = status;
        this.file_link = file_link;
        this.file_available = file_available;
        this.name = name;
        this.photo = photo;
        this.photo_thumb = photo_thumb;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSend_from() {
        return send_from;
    }

    public void setSend_from(String send_from) {
        this.send_from = send_from;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStickername() {
        return stickername;
    }

    public void setStickername(String stickername) {
        this.stickername = stickername;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public String getChat_date() {
        return chat_date;
    }

    public void setChat_date(String chat_date) {
        this.chat_date = chat_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getFile_available() {
        return file_available;
    }

    public void setFile_available(String file_available) {
        this.file_available = file_available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}