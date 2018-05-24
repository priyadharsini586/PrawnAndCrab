package com.nickteck.cus_prawnandcrab.model;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2018.
 */

public class NotificationModel {
    String Status_code,Success;
    ArrayList<Notification_list> Notification_list = new ArrayList<>();

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public ArrayList<NotificationModel.Notification_list> getNotification_list() {
        return Notification_list;
    }

    public void setNotification_list(ArrayList<NotificationModel.Notification_list> notification_list) {
        Notification_list = notification_list;
    }

    public class Notification_list
    {
        String title,notification;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }
    }
}
