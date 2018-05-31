package com.nickteck.cus_prawnandcrab.model;

import java.util.ArrayList;

/**
 * Created by admin on 5/29/2018.
 */

public class VideoGalleryList {

    private String Status_code;
    private ArrayList<VideoGalleryListDetails> Video_Gallery_list;
    private String Success;

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

    public ArrayList<VideoGalleryListDetails> getVideo_Gallery_list() {
        return Video_Gallery_list;
    }

    public void setVideo_Gallery_list(ArrayList<VideoGalleryListDetails> video_Gallery_list) {
        Video_Gallery_list = video_Gallery_list;
    }

    public class VideoGalleryListDetails {

        private String video_gal_id;
        private String title;
        private String description;
        private String video;
        private String date;


        public String getVideo_gal_id() {
            return video_gal_id;
        }

        public void setVideo_gal_id(String video_gal_id) {
            this.video_gal_id = video_gal_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


}
