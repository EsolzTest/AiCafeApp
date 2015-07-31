package com.esolz.aicafeapp.Datatype;

/**
 * Created by ltp on 31/07/15.
 */
public class SettingsDataType {
    String notification, sound, visible;

    public SettingsDataType(String notification, String sound, String visible) {
        this.notification = notification;
        this.sound = sound;
        this.visible = visible;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}