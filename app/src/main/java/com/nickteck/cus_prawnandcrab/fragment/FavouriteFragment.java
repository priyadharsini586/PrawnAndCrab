package com.nickteck.cus_prawnandcrab.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.cus_prawnandcrab.Adapter.FavouriteAdapter;
import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.FavouriteCustomList;
import com.nickteck.cus_prawnandcrab.model.FavouriteListData;
import com.nickteck.cus_prawnandcrab.model.ItemListRequestAndResponseModel;
import com.nickteck.cus_prawnandcrab.model.LoginRequestAndResponse;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nickteck.cus_prawnandcrab.additional_class.Constants.ITEM_BASE_URL;

/**
 * Created by admin on 4/7/2018.
 */

public class FavouriteFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener{
    View mainView;
    RecyclerView recyclerView;
    FavouriteAdapter favouriteAdapter;
    TextView txtBrodgeIcon;
    boolean netWorkConnection;
    ApiInterface apiInterface;
    private ProgressBar progress;
    Database database;
    FavouriteCustomList favouriteCustomList;
    ArrayList<FavouriteCustomList> final_arrayList;
    private  ArrayList<ItemListRequestAndResponseModel.item_list> gridImageList;
    ArrayList<FavouriteListData.FavouriteListDetails> favouriteListDetails_adapter;
    private TextView txtfavouriteList;
    boolean isNetworkConnected;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.favourite_frag, container, false);
        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        database = new Database(getActivity());
        gridImageList = new ArrayList<>();
        favouriteListDetails_adapter = new ArrayList<>();
        final_arrayList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        progress = (ProgressBar) mainView.findViewById(R.id.progress);
        tootBarTextViewb.setText("Favourite");
        txtfavouriteList = (TextView) mainView.findViewById(R.id.txtfavouriteList);
        txtfavouriteList.setVisibility(View.GONE);
        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);
        recyclerView=(RecyclerView)mainView.findViewById(R.id.recycler_view);

        /*MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            netWorkConnection = true;
            progress.setVisibility(View.VISIBLE);
            getItemList();
        }else {
            netWorkConnection = false;
        }*/

        if(isNetworkConnected){
            progress.setVisibility(View.VISIBLE);
            getItemList();
        }else {
            AdditionalClass.showSnackBar(getActivity());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        favouriteAdapter=new FavouriteAdapter(getActivity(),final_arrayList,FavouriteFragment.this);
        recyclerView.setAdapter(favouriteAdapter);



        return mainView;
    }


    private void getItemList() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ItemListRequestAndResponseModel> getCatageoryList = apiInterface.getItemData();
        getCatageoryList.enqueue(new Callback<ItemListRequestAndResponseModel>() {

            @Override
            public void onResponse(Call<ItemListRequestAndResponseModel> call, Response<ItemListRequestAndResponseModel> response) {
                ItemListRequestAndResponseModel itemListRequestAndResponseModel = response.body();
                if (itemListRequestAndResponseModel.getStatus_code().equals(Constants.Success)) {
                    List<ItemListRequestAndResponseModel.item_list> getItemDetils = itemListRequestAndResponseModel.getItem_list();
                    for (int i = 0; i < getItemDetils.size(); i++) {
                        ItemListRequestAndResponseModel.item_list items = getItemDetils.get(i);
                        items.setItem_name(items.getItem_name());
                        items.setDescription(items.getDescription());
                        items.setPrice(items.getPrice());
                        items.setImage(items.getImage());
                        String url = ITEM_BASE_URL + items.getImage();
                        Log.e("sub catagory url", url);
                        items.setFavourite("0");
                        items.setImage(url);
                        gridImageList.add(items);
                    }
                    send_Customer_id_api();
                }
            }

            @Override
            public void onFailure(Call<ItemListRequestAndResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void send_Customer_id_api() {
        database = new Database(getActivity());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            database.getCustomerName();
            jsonObject.put("customer_id", Database.customer_id);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final Call<FavouriteListData> favouriteListDataCall = apiInterface.FavouriteListDetails(jsonObject);
        favouriteListDataCall.enqueue(new Callback<FavouriteListData>() {
            @Override
            public void onResponse(Call<FavouriteListData> call, Response<FavouriteListData> response) {
                if (response.isSuccessful()){
                    if(response.body().getStatus_code().equals("1")){
                        FavouriteListData favouriteListData = response.body();

                        for(int i=0;i<favouriteListData.getList().size();i++){
                            favouriteListData.getList().get(i).getSno();
                              favouriteListData.getList().get(i).getItem_id();
                            FavouriteListData.FavouriteListDetails favouriteListDetails =  favouriteListData.getList().get(i);
                            favouriteListDetails_adapter.add(favouriteListDetails);
                        }
                        checkForMatchingData();
                    }else {
                        progress.setVisibility(View.GONE);
                        txtfavouriteList.setVisibility(View.VISIBLE);
                    }
                    }

            }

            @Override
            public void onFailure(Call<FavouriteListData> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkForMatchingData() {
        for(int i=0; i<favouriteListDetails_adapter.size() ;i++){
            for(ItemListRequestAndResponseModel.item_list s : gridImageList){
                if(favouriteListDetails_adapter.get(i).getItem_id().equals(s.getItem_id())){

                    // getting sno only from the favouriteList Array
                    String  getSno = favouriteListDetails_adapter.get(i).getSno();

                    String getItemId = s.getItem_id();
                    String ItemName = s.getItem_name();
                    String getDescription = s.getDescription();
                    String getItemImage = s.getImage();
                    String getItemPrice = s.getPrice();

                    favouriteCustomList = new FavouriteCustomList(getSno,getItemId,ItemName,getDescription,getItemImage,getItemPrice);
                    final_arrayList.add(favouriteCustomList);

                }else {
                    // Toast.makeText(getActivity(), "Item id not matched", Toast.LENGTH_SHORT).show();
                }
            }
        }
        favouriteAdapter.notifyDataSetChanged();
        progress.setVisibility(View.GONE);
    }

    public void removeFavouriteItemApi(String delete_favourite_item_id,final int pos){
        progress.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", Database.customer_id);
            jsonObject.put("item_id", delete_favourite_item_id);

        }catch (JSONException e){
            e.printStackTrace();
        }
        final Call<LoginRequestAndResponse> listDataCall = apiInterface.FavouriteDelete(jsonObject);
        listDataCall.enqueue(new Callback<LoginRequestAndResponse>() {
            @Override
            public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatusCode().equals("1")) {
                        Toast.makeText(getActivity(), "Favorite Deleted Successfully", Toast.LENGTH_SHORT).show();


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final_arrayList.remove(pos);
                                favouriteAdapter.notifyDataSetChanged();
                                Log.e("list size", String.valueOf(final_arrayList.size()));
                                progress.setVisibility(View.GONE);
                                if(final_arrayList.size()==0){
                                    txtfavouriteList.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }else {
                        Toast.makeText(getActivity(), "Favorite Not Exists", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
       /* netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }

    }*/

        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                AdditionalClass.showSnackBar(getActivity());

            }
        }
        isNetworkConnected = isConnected;
    }
}
