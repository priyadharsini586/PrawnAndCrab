package com.nickteck.cus_prawnandcrab.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nickteck.cus_prawnandcrab.Adapter.TestAdapter;
import com.nickteck.cus_prawnandcrab.Adapter.TestimonyAdapter;
import com.nickteck.cus_prawnandcrab.BuildConfig;
import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.activity.MenuNavigationActivity;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.animation_file.StaggeredAnimationGroup;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.LoginRequestAndResponse;
import com.nickteck.cus_prawnandcrab.model.TestimonyDetails;
import com.nickteck.cus_prawnandcrab.model.UserRegisterDetails;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;
import com.nickteck.cus_prawnandcrab.utils.UtilsClass;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestimonyFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener,View.OnClickListener {
    View view;
    ListView list_msg;
    LinearLayout ldtMenu;
    ImageView imgSendImage,imgCloseImg;
    public static Activity context;
    EditText edtContent;
    Button sendTestimony;
    ProgressBar progress;
    String isConnection = null,send = "";
    private TestimonyAdapter adapter;
    TestimonyDetails testimonyDetails = TestimonyDetails.getInstance();
    private List<TestimonyDetails> chatBubbles;
    private ArrayList<String> testMessage;
    NetworkChangeReceiver networkChangeReceiver;
    boolean netWorkConnection;
    RelativeLayout mainView;
    boolean isOpen = false;
    StaggeredAnimationGroup group;
    RelativeLayout rldReview,rldNoReview;
    LinearLayout ldtCamera,lstGallery;
    ImageView imgSendImg;
    RelativeLayout rldSendImage;
    Uri imageUri;
    ApiInterface apiInterface;
    UtilsClass utilsClass;
    int CAMERA_CODE =100;
    Bitmap bitmap = null;
    boolean myMessage = true;
    private String currentDateTime;
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    static final Integer CAMERA = 1;



    public static Activity TestimonyFragment()
    {
        return context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testimony, container, false);
        ldtMenu = (LinearLayout) view.findViewById(R.id.ldtMenu);
        imgSendImage = (ImageView) view.findViewById(R.id.imgSendImage);
        group = view.findViewById(R.id.group);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        rldNoReview = (RelativeLayout) view.findViewById(R.id.rldNoReview);
        rldNoReview.setVisibility(View.GONE);
        rldReview = (RelativeLayout) view.findViewById(R.id.rldReview);
        rldReview.setVisibility(View.GONE);
        group.hide();



        context = getActivity();
        imgSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    group.hide();
                    isOpen = false;
                } else {
                    group.show();
                    isOpen = true;
                }
            }
        });

        MyApplication.getInstance().setConnectivityListener(this);
        if (HelperClass.isNetworkAvailable(getActivity())) {
            netWorkConnection = true;
            displayTestimony();

        }else {
            netWorkConnection = false;
        }

        //------------------------//

        utilsClass = new UtilsClass();
        lstGallery = (LinearLayout) view.findViewById(R.id.lstGallery);
        lstGallery.setOnClickListener(this);
        ldtCamera  = (LinearLayout) view.findViewById(R.id.ldtCamera);
        ldtCamera.setOnClickListener(this);

        imgSendImg = (ImageView) view.findViewById(R.id.imgSendImg);
        rldSendImage = (RelativeLayout) view.findViewById(R.id.rldSendImage);
        rldSendImage.setVisibility(View.GONE);
        imgCloseImg = (ImageView) view.findViewById(R.id.imgCloseImg);
        imgCloseImg.setOnClickListener(this);

        list_msg = (ListView) view.findViewById(R.id.list_msg);
        chatBubbles = new ArrayList<>();

        adapter = new TestimonyAdapter(getActivity(), R.layout.left_bubble_chat, chatBubbles);
        list_msg.setAdapter(adapter);

        sendTestimony = (Button) view.findViewById(R.id.sendTestimony);
        edtContent = (EditText) view.findViewById(R.id.edtContent);

        sendTestimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtContent.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                      progress.setVisibility(View.VISIBLE);
                     sendTestimony();


                }
            }
        });
        return view;


    }

    private void getCameraPermission() {
        askForPermission(Manifest.permission.CAMERA,CAMERA);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                goToSettings();
            }
        } else {
            cameraIntent();
        }
    }

    private void goToSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", "com.nickteck.cus_prawnandcrab", null);
        intent.setData(uri);
        startActivity(intent);
    }
    private void testgoToSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("com.nickteck.cus_prawnandcrab", BuildConfig.APPLICATION_ID);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayTestimony(){

        if (netWorkConnection){
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<TestimonyDetails> call = apiInterface.getTestimonyDetails();
            call.enqueue(new Callback<TestimonyDetails>() {
                             @Override
                             public void onResponse(Call<TestimonyDetails> call, Response<TestimonyDetails> response) {
                                 if (response.isSuccessful()) {
                                     TestimonyDetails testimonyDetails = response.body();
                                     if (testimonyDetails.getStatus_code().equals(Constants.SUCCESS)) {
                                         if(testimonyDetails.getTestimony_list() != null){
                                              int va = testimonyDetails.getTestimony_list().size();
                                             Toast.makeText(getActivity(), ""+va, Toast.LENGTH_SHORT).show();

                                             for(int i=0; i<testimonyDetails.getTestimony_list().size(); i++){

                                                 TestimonyDetails.TestimonyList testimonyList= testimonyDetails.getTestimony_list().get(i);

                                                 int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                                                 int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

                                                 TestimonyDetails details = new TestimonyDetails();
                                                 details.setTestimony_id(testimonyList.getTestimony_id());
                                                 details.setMessage(testimonyList.getMessage());
                                                 details.setImage(testimonyList.getImage());
                                                 details.setStatus(testimonyList.isStatus());
                                                 details.setDate(testimonyList.getDate());



                                                 ArrayList<TestimonyDetails.CustomerDetails> customerDetails = testimonyList.getCustomer_details();
                                                 if(customerDetails!= null){
                                                     for (int j=0 ; j <customerDetails.size(); j++) {
                                                         TestimonyDetails.CustomerDetails customerDetails1 = customerDetails.get(j);

                                                         details.setCustomer_id(customerDetails1.getCustomer_id());
                                                         details.setName(customerDetails1.getName());
                                                         details.setPhone(customerDetails1.getPhone());
                                                     }
                                                 }

                                                 details.setFrom("server");
                                                 if (!testimonyList.getImage().equals(""))
                                                     details.setTestimonyPic(testimonyList.getImage());
                                                     details.setColorCode(randomAndroidColor);
                                                 if (!testimonyList.getMessage().equals("") )

                                                     if((testimonyList.getTestimony_id().equals("5"))){
                                                     // dont add to the list
                                                         Toast.makeText(getActivity(), "not added to the list", Toast.LENGTH_SHORT).show();

                                                     }else{
                                                         chatBubbles.add(details);
                                                     }



                                             }

                                         }
                                         send = "send";
                                         list_msg.post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 // Select the last row so it will scroll into view...
                                                 list_msg.setSelection(adapter.getCount() - 1);
                                             }
                                         });
                                        // Collections.reverse(chatBubbles);
                                         adapter.notifyDataSetChanged();
                                         progress.setVisibility(View.GONE);
                                         rldNoReview.setVisibility(View.GONE);
                                         rldReview.setVisibility(View.VISIBLE);
                                     }else {
                                         rldNoReview.setVisibility(View.VISIBLE);
                                         rldReview.setVisibility(View.GONE);
                                         progress.setVisibility(View.GONE);
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<TestimonyDetails> call, Throwable t) {
                                 Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();

                             }
                         });
        }
    }

    private void sendTestimony() {
        if (netWorkConnection){
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            final UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
            final UtilsClass utilsClass = new UtilsClass();
            try {
                Database database = new Database(getActivity());
                database.getCustomerName();
                jsonObject.put("customer_id",Database.customer_id);
                jsonObject.put("message",edtContent.getText().toString().trim());
                if (testimonyDetails.getImageBitmap() != null) {
                    jsonObject.put("image", utilsClass.BitMapToString(testimonyDetails.getImageBitmap()));
                    int imageSize = testimonyDetails.getImageBitmap().getByteCount();
                    Toast.makeText(getActivity(), ""+imageSize, Toast.LENGTH_SHORT).show();
                }else {
                    jsonObject.put("image","");
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<TestimonyDetails> call = apiInterface.sendTestimony(jsonObject);
            call.enqueue(new Callback<TestimonyDetails>() {


                @Override
                public void onResponse(Call<TestimonyDetails> call, Response<TestimonyDetails> response) {
                    TestimonyDetails testimonyDetail = response.body();

                    if (testimonyDetail.getStatuscode().equals(Constants.SUCCESS)){
                        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

                        TestimonyDetails details = new TestimonyDetails();

                        details.setMessage(edtContent.getText().toString().trim());
                        myMessage = true;
                        details.setMyMessage(myMessage);
                        details.setName(userRegisterDetails.getUserName());
                        details.setFrom("me");

                        Database database = new Database(getActivity());
                        details.setName(database.getCustomerName());
                        currentDateTime = getCurrentDate();
                        details.setDate(currentDateTime);
                        details.setPhone(Database.customer_phoneNo);
                        details.setProfilePic(Database.profile_img);

                        if (testimonyDetails.getImageBitmap() != null)
                            details.setTestimonyPic(utilsClass.BitMapToString(testimonyDetails.getImageBitmap()));
                        details.setColorCode(randomAndroidColor);

                        chatBubbles.add(details);

                        list_msg.post(new Runnable() {
                            @Override
                            public void run() {
                                // Select the last row so it will scroll into view...
                                list_msg.setSelection(adapter.getCount() - 1);
                            }
                        });
                        myMessage = false;
                        adapter.notifyDataSetChanged();
                        edtContent.setText("");
                        imgCloseImg.performClick();
                        progress.setVisibility(View.GONE);

                        rldNoReview.setVisibility(View.GONE);
                        rldReview.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<TestimonyDetails> call, Throwable t) {
                    t.fillInStackTrace();
                    Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();



                }
            });


        }

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lstGallery:
                Crop.pickImage((MenuNavigationActivity)getActivity());
                group.hide();
                break;
            case R.id.ldtCamera:
                getCameraPermission();
                group.hide();
                break;
            case R.id.imgCloseImg:
                rldSendImage.setVisibility(View.GONE);
                //-------------------------//
              //  testimonyDetails.setImageBitmap(null);
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {


        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK)
        {
            if (imageUri != null) {
                Bitmap thumbnail = null;
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(TestimonyFragment.context.getContentResolver(), imageUri);
                    Uri tempUri = getImageUri(TestimonyFragment.context, thumbnail);
                    beginCrop(tempUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                testimonyDetails.setImageBitmap(thumbnail);
            }

//                imgView.setImageBitmap(thumbnail);

              /*  Bundle extras = result.getExtr  as();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmap = utilsClass.getResizedBitmap(imageBitmap,500,500);
                testimonyDetails.setImageBitmap(bitmap);*/
              /*  Uri tempUri = getImageUri(TestimonyFragment.context, imageBitmap);
                beginCrop(tempUri);*/
        }

    }



    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(TestimonyFragment.context.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(TestimonyFragment.context);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {

            Uri imageUri = Crop.getOutput(result);
            setImage(imageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(TestimonyFragment.context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }else
        {
//            ic_profile.setImageBitmap(StringToBitMap(dateResponse.getPhoto()));
        }
    }

    public void setImage(Uri uri) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(TestimonyFragment.context.getContentResolver(), uri);
            UtilsClass utilsClass1 = new UtilsClass();
            bitmap = utilsClass1.getResizedBitmap(bitmap,500,500);
            testimonyDetails.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (testimonyDetails.getImageBitmap() != null) {
            imgSendImg.setImageBitmap(testimonyDetails.getImageBitmap());
            rldSendImage.setVisibility(View.VISIBLE);
            Log.e("onResume called", "on resume called in frag");
        }


    }
    @Override
    public void onStop() {
        super.onStop();
        if (networkChangeReceiver == null) {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            getActivity().unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    private void cameraIntent() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = TestimonyFragment.context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);*/
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMM.yyyy HH:mm");
        Date date = new Date();
        currentDateTime = simpleDateFormat.format(date);
        return currentDateTime;


    }
}
