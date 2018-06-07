package com.nickteck.cus_prawnandcrab.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nickteck.cus_prawnandcrab.Adapter.ViewPagerAdapter;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.activity.MenuNavigationActivity;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.model.ItemListRequestAndResponseModel;
import com.nickteck.cus_prawnandcrab.model.ItemModel;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment implements View.OnClickListener,NetworkChangeReceiver.ConnectivityReceiverListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int [] sliderList = {R.drawable.cook2,R.drawable.cook3,R.drawable.cook4,R.drawable.cook5};
    private ArrayList<ItemListRequestAndResponseModel> imageModelArrayList;
    View mainView;
    LinearLayout ldtMenuList,ldtMyOrders,ldtHistoryList,ldtSpinner,ldtFav;
    TextView txtBrodgeIcon;
    boolean isNetworkConnected;
    boolean netWorkConnection;
    RelativeLayout rldMainView;
    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentFragment newInstance(String param1, String param2) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_content, container, false);

        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tootBarTextViewb = (TextView)toolbar.findViewById(R.id.txtHomeToolBar);
        String content_text = getResources().getString(R.string.content_fragment);
        tootBarTextViewb.setText(content_text);



        txtBrodgeIcon = (TextView)toolbar.findViewById(R.id.txtBrodgeIcon);
        ldtSpinner = (LinearLayout)toolbar.findViewById(R.id.ldtSpinner);
        ldtSpinner.setVisibility(View.VISIBLE);
        ItemModel itemModel = ItemModel.getInstance();
        if (itemModel.getListArrayList().size() == 0)
        {
            txtBrodgeIcon.setVisibility(View.GONE);
        }else
        {
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(itemModel.getListArrayList().size()));
        }

        init();
        rldMainView = (RelativeLayout)mainView.findViewById(R.id.rldMainView);
        return mainView;
    }


    private void init() {

        mPager = (ViewPager) mainView.findViewById(R.id.viewPager);
        imageModelArrayList = populateList();
        mPager.setAdapter(new ViewPagerAdapter(getActivity(),imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator) mainView.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
        NUM_PAGES =imageModelArrayList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        ldtMenuList = (LinearLayout) mainView.findViewById(R.id.ldtMenuList);
        ldtMenuList.setOnClickListener(this);

        ldtMyOrders = (LinearLayout)mainView.findViewById(R.id.ldtMyOrders);
        ldtMyOrders.setOnClickListener(this);

        ldtHistoryList = (LinearLayout) mainView.findViewById(R.id.ldtHistoryList);
        ldtHistoryList.setOnClickListener(this);

        ldtFav = (LinearLayout)mainView.findViewById(R.id.ldtFav);
        ldtFav.setOnClickListener(this);
    }

    private ArrayList<ItemListRequestAndResponseModel> populateList(){

        ArrayList<ItemListRequestAndResponseModel> list = new ArrayList<>();

        for(int i = 0; i <sliderList.length; i++){
            ItemListRequestAndResponseModel imageModel = new ItemListRequestAndResponseModel();
            imageModel.setImage_drawable(sliderList[i]);
            list.add(imageModel);
        }

        return list;
    }


    @Override
    public void onClick(View v) {
        if(isNetworkConnected) {
            switch (v.getId()) {
                case R.id.ldtMenuList:
                    OrderTakenScreenFragment catagoryFragment = new OrderTakenScreenFragment();
                    AdditionalClass.replaceFragment(catagoryFragment, Constants.ORDER_TAKEN_FRAGMENT, (AppCompatActivity) getActivity());
                    break;

                case R.id.ldtMyOrders:
                    OrderFragment orderFragment = new OrderFragment();
                    AdditionalClass.replaceFragment(orderFragment, Constants.ORDER_FRAGMENT, (AppCompatActivity) getActivity());
                    break;

                case R.id.ldtHistoryList:
                    HistoryFragment historyFragment = new HistoryFragment();
                    AdditionalClass.replaceFragment(historyFragment, Constants.HISTORY_FRAGMENT, (AppCompatActivity) getActivity());
                    break;

                case R.id.ldtFav:
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    AdditionalClass.replaceFragment(favouriteFragment, Constants.FAVOURITE_FRAGMENT, (AppCompatActivity) getActivity());
                    break;
            }
        }else {
        AdditionalClass.showSnackBar1(rldMainView,"Network not connected...");
    }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
       /* netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }*/

        if (isNetworkConnected != isConnected) {
            if (isConnected) {
            } else {
                HelperClass.showTopSnackBar(mainView,"Network not connected");
            }
        }
        isNetworkConnected = isConnected;
    }
}
