package com.nickteck.cus_prawnandcrab.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.LoginRequestAndResponse;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener{

    EditText edtPhone,edtName,edtMailId;
    Button btnSubmitLogin;
    String TAG= LoginActivity.class.getName();
    ProgressBar progressLogin;
    RelativeLayout mainView;
    boolean netWorkConnection;
    boolean isEmail,isPhone;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    boolean isPermission ;
    String isConnection = null;
    TSnackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getPermission();

        mainView = (RelativeLayout) findViewById(R.id.sclMainView);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtName = (EditText) findViewById(R.id.edtName);
        edtMailId = (EditText) findViewById(R.id.edtMailId);

        btnSubmitLogin= (Button) findViewById(R.id.btnSubmitLogin);
        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmail = HelperClass.isValidMail(edtMailId.getText().toString().trim());
                isPhone = HelperClass.isValidMobile(edtPhone.getText().toString().trim());
                if (isEmail && isPhone && !edtName.getText().toString().isEmpty())
                    checkLogin();
                else
                    validation();
            }
        });
        progressLogin = (ProgressBar) findViewById(R.id.progressLogin);
        progressLogin.setVisibility(View.GONE);
        MyApplication.getInstance().setConnectivityListener(this);
        if (HelperClass.isNetworkAvailable(getApplicationContext()))
            netWorkConnection = true;
        else
            netWorkConnection = false;



    }

    private void getPermission() {
      //  permissions.add(Manifest.permission.GET_ACCOUNTS);
     // permissions.add(Manifest.permission.READ_SMS);
     // permissions.add(Manifest.permission.RECEIVE_SMS);
     // permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                if (isConnection != null) {
                    if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                        if (snackbar != null) {
                            snackbar.dismiss();
                        }

                    }
                }
                isPermission =false;

//                Toast.makeText(getApplicationContext(),"Permissions not granted.", Toast.LENGTH_LONG).show();

            } else {
//                Toast.makeText(getApplicationContext(),"Permissions already granted.", Toast.LENGTH_LONG).show();
                isPermission = true;
              //  ldtSplash.startAnimation(animBounce);
            }

            if (!canAskPermission())
                isPermission =true;
            else
                Log.e("check","false");



        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d("request", "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            String msg = "These permissions are mandatory for the application. Please allow access.";
                            showMessageOKCancel(msg,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);


                                                isPermission = true;
                                                //ldtSplash.startAnimation(animBounce);
//
                                            }
                                        }
                                    });
                            Log.e("check","inpermission");
                            return;
                        }
                    }
                } else {
                    isPermission = true;
                    Log.e("check","inpermission");
                   /* getE_mail();
                    ldtSplash.startAnimation(animBounce);
                    checkEmail();*/
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",okListener )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .create()
                .show();
    }

    public void validation()
    {
         if (!isEmail) {

             edtMailId.setError("Invalid Email id");
         }


        if (!isPhone) {

            edtPhone.setError("Invalid Phone number");
        }

        if (edtName.getText().toString().isEmpty())
            edtName.setError("Invalid Name");
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }
    }
    public void checkLogin()
    {
        if (netWorkConnection)
        {
            progressLogin.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject  jsonObject = new JSONObject();
            try {
                jsonObject.put("phone",edtPhone.getText().toString().trim());
                jsonObject.put("name",edtName.getText().toString().trim());
                jsonObject.put("email",edtMailId.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<LoginRequestAndResponse> loginRequestAndResponseCall = apiInterface.getLoginResponse(jsonObject);
            loginRequestAndResponseCall.enqueue(new Callback<LoginRequestAndResponse>() {
                @Override
                public void onResponse(Call<LoginRequestAndResponse> call, Response<LoginRequestAndResponse> response) {
                    if (response.isSuccessful())
                    {
                        LoginRequestAndResponse loginRequestAndResponse = response.body();
                        progressLogin.setVisibility(View.GONE);
                        if (loginRequestAndResponse.getStatus_code().equals(Constants.SUCCESS))
                        {
                            Database database = new Database(getApplicationContext());

                            database.insertCustomerTable(loginRequestAndResponse.getCustomer_id(),edtName.getText().toString(),edtPhone.getText().toString(),edtMailId.getText().toString());
                            Intent intent = new Intent(getApplicationContext(),MenuNavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }else
                        {
                            Toast.makeText(getApplicationContext(),loginRequestAndResponse.getStatus_message(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginRequestAndResponse> call, Throwable t) {
                    progressLogin.setVisibility(View.GONE);
                }
            });
        }else
        {
            HelperClass.showTopSnackBar(mainView,"Network not connected");
        }
    }
}
