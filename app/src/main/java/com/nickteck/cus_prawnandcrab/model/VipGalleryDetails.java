package com.nickteck.cus_prawnandcrab.model;

import java.util.ArrayList;

/**
 * Created by admin on 5/28/2018.
 */

public class VipGalleryDetails {

    private String Status_code;
    private ArrayList<VIP_Gallery_list> VIP_Gallery_list = new ArrayList<>();
    private String Success;

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public ArrayList<VIP_Gallery_list> getVip_gallery_lists() {
        return VIP_Gallery_list;
    }

    public void setVip_gallery_lists(ArrayList<VIP_Gallery_list> vip_gallery_lists) {
        this.VIP_Gallery_list = vip_gallery_lists;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public class VIP_Gallery_list{
        private String vip_gal_id;
        private String title;
        private String description;
        private String image;
        private String date;

        public VIP_Gallery_list(String vip_gal_id, String title, String description, String image, String date) {
            this.vip_gal_id = vip_gal_id;
            this.title = title;
            this.description = description;
            this.image = image;
            this.date = date;
        }

        public String getVip_gal_id() {
            return vip_gal_id;
        }

        public void setVip_gal_id(String vip_gal_id) {
            this.vip_gal_id = vip_gal_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
    }



}


