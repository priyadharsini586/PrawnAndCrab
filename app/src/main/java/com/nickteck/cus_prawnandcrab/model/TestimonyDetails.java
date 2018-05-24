package com.nickteck.cus_prawnandcrab.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/1/2017.
 */

public class TestimonyDetails {

    private Bitmap imageBitmap;
    private String Status_code,status_message,message,success,
            testimonyPic,profilePic,name,from,date,time,email,testimony_id,customer_id,image,phone;
    private int colorCode;
    private String status_code;
    private boolean myMessage,status;
    private ArrayList<TestimonyList> testimony_list = new ArrayList<>();
    private static TestimonyDetails ourInstance = new TestimonyDetails();

    public TestimonyDetails(){

    }

    public static TestimonyDetails getInstance() {
        return ourInstance;
    }



    public ArrayList<TestimonyList> getTestimony_list() {
        return testimony_list;
    }

    public void setTestimony_lists(ArrayList<TestimonyList> testimony_list) {
        this.testimony_list = testimony_list;
    }



    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTestimonyPic() {
        return testimonyPic;
    }

    public void setTestimonyPic(String testimonyPic) {
        this.testimonyPic = testimonyPic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public void setMyMessage(boolean myMessage) {
        this.myMessage = myMessage;
    }



    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTestimony_id() {
        return testimony_id;
    }

    public void setTestimony_id(String testimony_id) {
        this.testimony_id = testimony_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_codee) {
        status_code = status_codee;
    }
    public String getStatuscode() {
        return status_code;
    }

    public void setStatuscode(String status_code) {
        status_code = status_code;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public static class TestimonyList{
        private String testimony_id;
        private ArrayList<CustomerDetails> customer_details;

        private String message;
        private String image;
        private boolean status;
        private String date;

        public  TestimonyList(){}

        public String getTestimony_id() {
            return testimony_id;
        }

        public void setTestimony_id(String testimony_id) {
            this.testimony_id = testimony_id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<CustomerDetails> getCustomer_details() {
            return customer_details;
        }

        public void setCustomer_details(ArrayList<CustomerDetails> customer_details) {
            this.customer_details = customer_details;
        }


        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public static class CustomerDetails{

        private String customer_id;
        private String name;
        private String phone;


        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }


}
