package com.nickteck.cus_prawnandcrab.api;

import com.nickteck.cus_prawnandcrab.model.AddWhislist;
import com.nickteck.cus_prawnandcrab.model.HistoryModel;
import com.nickteck.cus_prawnandcrab.model.ItemListRequestAndResponseModel;
import com.nickteck.cus_prawnandcrab.model.LoginRequestAndResponse;
import com.nickteck.cus_prawnandcrab.model.NotificationModel;
import com.nickteck.cus_prawnandcrab.model.TableModel;
import com.nickteck.cus_prawnandcrab.model.TestimonyDetails;
import com.nickteck.cus_prawnandcrab.model.VideoGalleryList;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 3/7/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("check_client_details.php")
    Call<LoginRequestAndResponse> getLoginResponse(@Field("x") JSONObject jsonObject);


    @POST("category_list.php")
    Call<ItemListRequestAndResponseModel> getCatagoryList();

    @POST("sub_category_list.php")
    Call<ItemListRequestAndResponseModel> getSubCatagoryList();

    @POST("veraity_list.php")
    Call<ItemListRequestAndResponseModel> getVarietyList();

    @FormUrlEncoded
    @POST("subcat_item_list.php")
    Call<ItemListRequestAndResponseModel> getItemList(@Field("x") JSONObject Itemobject);

    @FormUrlEncoded
    @POST("favourite_list.php")
    Call<AddWhislist> getWhishlist(@Field("x") JSONObject Itemobject);

    @POST("table_list.php")
    Call<TableModel> getTableData();

    @POST("item_list.php")
    Call<ItemListRequestAndResponseModel> getItemData();

    @FormUrlEncoded
    @POST("current_ip.php")
    Call<LoginRequestAndResponse> getIp(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("customer_history.php")
    Call<HistoryModel> getHistoryDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("client_details_by_phone.php")
    Call<LoginRequestAndResponse> clientDetailsByNum(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("cat_subcat_item_list.php")
    Call<ItemListRequestAndResponseModel> getItemBasedOnCat(@Field("x") JSONObject object);

    @POST("notification_get.php")
    Call<NotificationModel> getNotificationData();

    @POST("testimony_list.php")
    Call<TestimonyDetails> getTestimonyDetails();

    @FormUrlEncoded
    @POST("testimony_add.php")
    Call<TestimonyDetails> sendTestimony(@Field("x") JSONObject object);


    @POST("vip_gallery_list.php")
    Call<VipGalleryDetails> getVipGalleryList();

    @POST("video_gallery_list.php")
    Call<VideoGalleryList> getVideoGalleryList();


}
