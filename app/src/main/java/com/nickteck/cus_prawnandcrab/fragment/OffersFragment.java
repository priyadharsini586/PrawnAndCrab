package com.nickteck.cus_prawnandcrab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nickteck.cus_prawnandcrab.Adapter.OffersAdapter;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.NotificationModel;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {

    View mainView;
    boolean netWorkConnection;
    ProgressBar progressOffers;
    OffersAdapter offersAdapter;
    RecyclerView recyclerViewOffers;
    TextView txtNoOffer;
    TextView txtBrodgeIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.offers_fragment, container, false);
        MyApplication.getInstance().setConnectivityListener(this);
        progressOffers= (ProgressBar) mainView.findViewById(R.id.progressOffers);
        progressOffers.setVisibility(View.VISIBLE);
        recyclerViewOffers = (RecyclerView) mainView.findViewById(R.id.recyclerViewOffers);
        if (HelperClass.isNetworkAvailable(getActivity()))
            netWorkConnection = true;
        else
            netWorkConnection = false;

        getOffers();
        txtNoOffer = (TextView) mainView.findViewById(R.id.txtNoOffer);
        txtNoOffer.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        tootBarTextViewb.setText("Offers");
        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);
        return mainView;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }
    }

    public void getOffers()
    {
        if (netWorkConnection)
        {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<NotificationModel> notificationModelCall = apiInterface.getNotificationData();
            notificationModelCall.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful())
                    {
                        NotificationModel notificationModel = response.body();
                        if (notificationModel.getStatus_code().equals(Constants.SUCCESS))
                        {
                            progressOffers.setVisibility(View.GONE);


                            offersAdapter = new OffersAdapter(getActivity(),notificationModel.getNotification_list());
                            recyclerViewOffers.setAdapter(offersAdapter);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            recyclerViewOffers.setLayoutManager(linearLayoutManager);
                            offersAdapter.notifyDataSetChanged();
                        }else
                        {
                            progressOffers.setVisibility(View.GONE);
                            txtNoOffer.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }else
        {
            HelperClass.showTopSnackBar(mainView,"Network not connected");
        }
    }
}
