package com.nickteck.cus_prawnandcrab.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.DataParser;
import com.nickteck.cus_prawnandcrab.model.LatLog;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindLocationFragment extends Fragment implements OnMapReadyCallback,LocationListener,NetworkChangeReceiver.ConnectivityReceiverListener {

    GoogleMap mGoogleMap;
    View mainView;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ArrayList<LatLng> current_Lat_lng_Value = new ArrayList<>();

    LatLng currentLatLng;
    private LatLng min_latlag_value_distance;
    ArrayList<LatLng> addLocation1;
    boolean isNetworkConnected;


    public FindLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_find_location, container, false);

        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
        }else {
            isNetworkConnected = false;
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.find_location_map);
        mapFragment.getMapAsync(this);

        return mainView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }



    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }


                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                current_Lat_lng_Value.add(latLng);
                currentLatLng = latLng;
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                // set static location method
                setStaticLocation();



            }


        }
    };

    private void setStaticLocation() {

        staticAddressLocation();

        /*LatLng location;
        MarkerOptions markerOptions;
        ArrayList<LatLng> addLocation = new ArrayList<>();

        location = new LatLng(11.9518508,79.8169407);
        markerOptions = new MarkerOptions();
        markerOptions.position(location);
        addLocation.add(location);
        markerOptions.title("Location2");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mGoogleMap.addMarker(markerOptions);



        location = new LatLng(12.99241108,80.21055926);
        markerOptions = new MarkerOptions();
        markerOptions.position(location);
        addLocation.add(location);
        markerOptions.title("Location2");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mGoogleMap.addMarker(markerOptions);

        location = new LatLng(12.98526797,80.20933697);
        markerOptions = new MarkerOptions();
        markerOptions.position(location);
        addLocation.add(location);
        markerOptions.title("Location1");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mGoogleMap.addMarker(markerOptions);*/

      //  checkForDistance(addLocation,current_Lat_lng_Value);

    }

    private void staticAddressLocation() {
        ArrayList<String> address = new ArrayList<>();

        address.add("181/9 East Coast Road, Opp. to Shankara Vidyalaya School, Maduvu Pet, Lawspet, Vallalar Nagar, Ashok Nagar, Lawspet, Puducherry, 605008");
        address.add("VRISA Apartments, 18, 4th Cross St, Lakshmi Hayagreeva Nagar, " +
                "V V Colony, Mahalakshmi Nagar, Adambakkam, Chennai, Tamil Nadu 600088, India");
        address.add("Little Mount, Kotturpuram, Chennai, Tamil Nadu, India");

        getGeoLocationFromAddress(address);


    }

    private void getGeoLocationFromAddress(ArrayList<String> address1) {
        addLocation1 = new ArrayList<>();
        Geocoder coder = new Geocoder(getActivity());
        List<Address> addresses;
        LatLng p1 = null;
        if(address1.size()>0){
            for(int i=0; i<address1.size();i++){
                try {
                    // May throw an IOException
                    addresses = coder.getFromLocationName(address1.get(i), 5);
                    Address location = addresses.get(0);
                    p1 = new LatLng(location.getLatitude(), location.getLongitude());
                    addLocation1.add(p1);

                } catch (IOException ex) {

                    ex.printStackTrace();
                }
            }
        }
        fixMarker(addLocation1);


    }

    private void fixMarker(ArrayList<LatLng> addLocation1) {
        ArrayList<LatLng> addLocation = new ArrayList<>();
        LatLng location_obtained;
        MarkerOptions markerOptions_obtained;

        for(int i=0; i<addLocation1.size();i++){
            location_obtained = new LatLng(addLocation1.get(i).latitude,addLocation1.get(i).longitude);
            markerOptions_obtained = new MarkerOptions();
            markerOptions_obtained.position(location_obtained);

            addLocation.add(location_obtained);

            markerOptions_obtained.title("Location"+i);
            markerOptions_obtained.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mGoogleMap.addMarker(markerOptions_obtained);

        }
        checkForDistance(addLocation,current_Lat_lng_Value);

    }

    private void checkForDistance(ArrayList<LatLng> addLocation, ArrayList<LatLng> latLng) {
         ArrayList<LatLog> latLogs_model = new ArrayList<>();


        if(addLocation != null){
            for(int i=0;i<addLocation.size();i++){
                // location1
                 Location location1 = new Location("");
                 location1.setAltitude(latLng.get(0).latitude);
                 location1.setLongitude(latLng.get(0).longitude);

                // location2 static
                Location location2 = new Location("");
                location2.setAltitude(addLocation.get(i).latitude);
                location2.setLongitude(addLocation.get(i).longitude);

                float distanceInMeters = location1.distanceTo(location2);
                String dist = distanceInMeters + " M";
                LatLog latLog = new LatLog(distanceInMeters,addLocation.get(i));
                latLogs_model.add(latLog);
            }
            checkForMinimumValue(latLogs_model);

        }


    }

    private void checkForMinimumValue(ArrayList<LatLog> latLogs_model) {

        Float value = Float.MAX_VALUE;
        for (int  j = 0 ; j < latLogs_model.size() ; j ++)
        {
            LatLog latLog = latLogs_model.get(j);
            if (latLog.getLatLagDistance() < value)
            {
                value = latLog.getLatLagDistance();
                min_latlag_value_distance = latLog.getLatLng();
            }
        }
        // start for drawing polygon line
        String url = getUrl(currentLatLng, min_latlag_value_distance);

        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
    }




    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mGoogleMap.addPolyline(lineOptions);

            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "mode=driving&" +
                "transit_routing_preferences=less_driving&origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + ","+ dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        parameters = parameters +"&key=AIzaSyDdUbA2ss34nDeWO9twfd_FTOHcumz3sgg";
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
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
