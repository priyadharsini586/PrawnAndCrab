package com.nickteck.cus_prawnandcrab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nickteck.cus_prawnandcrab.Adapter.TestimonyAdapter;
import com.nickteck.cus_prawnandcrab.Adapter.VipGalleryAdapter;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.TestimonyDetails;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VipGalleryFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {
    View view;
    private RecyclerView recycler_view_vip_gallery;
    boolean netWorkConnection;
    RelativeLayout mainView;
    ApiInterface apiInterface;
    ProgressBar progress;
    private VipGalleryAdapter vipGalleryAdapter;
    ArrayList<VipGalleryDetails> vipGallerydataDetails;
    boolean isNetworkConnected;



    public VipGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vip_gallery, container, false);

        init(view);


        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            progress.setVisibility(View.VISIBLE);
            getVipListData();
        }else {
            isNetworkConnected = false;
        }
        return  view;
    }

    private void init(View view) {
        vipGallerydataDetails = new ArrayList<>();
        recycler_view_vip_gallery = (RecyclerView)view.findViewById(R.id.recycler_view_vip_gallery);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        vipGalleryAdapter = new VipGalleryAdapter(getActivity(),vipGallerydataDetails,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_view_vip_gallery.setLayoutManager(mLayoutManager);
        recycler_view_vip_gallery.setAdapter(vipGalleryAdapter);
    }

    private void getVipListData() {
        if (isNetworkConnected){
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<VipGalleryDetails> call = apiInterface.getVipGalleryList();
            call.enqueue(new Callback<VipGalleryDetails>() {

                @Override
                public void onResponse(Call<VipGalleryDetails> call, Response<VipGalleryDetails> response) {
                    if (response.isSuccessful()) {
                        VipGalleryDetails vipGalleryDetails = response.body();
                        if (vipGalleryDetails.getStatus_code().equals(Constants.SUCCESS)) {
                            if(vipGalleryDetails.getVip_gallery_lists() != null){
                                for(int i=0; i<vipGalleryDetails.getVip_gallery_lists().size(); i++){
                                    vipGalleryDetails.getVip_gallery_lists().get(i).getVip_gal_id();
                                    vipGalleryDetails.getVip_gallery_lists().get(i).getTitle();
                                    vipGalleryDetails.getVip_gallery_lists().get(i).getDescription();
                                    vipGalleryDetails.getVip_gallery_lists().get(i).getImage();
                                    vipGalleryDetails.getVip_gallery_lists().get(i).getDate();
                                    vipGallerydataDetails.add(vipGalleryDetails);
                                }
                                vipGalleryAdapter.notifyDataSetChanged();
                                progress.setVisibility(View.GONE);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<VipGalleryDetails> call, Throwable t) {
                    Toast.makeText(getActivity(),"Sever Error",Toast.LENGTH_LONG).show();

                }
            });

        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        /*netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }*/
        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                AdditionalClass.showSnackBar(getActivity());
                progress.setVisibility(View.GONE);
            }
        }
        isNetworkConnected = isConnected;

    }

    public  void getImageSelection(){

    }


}
