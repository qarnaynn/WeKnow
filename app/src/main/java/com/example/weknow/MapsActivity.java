package com.example.weknow;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    MarkerOptions marker;
    LatLng centerlocation;
    Vector<MarkerOptions> markerOptions;

    //CHANGE IP ADDRESS BASED ON WIFI CONNECTION
    final String URL = "http://192.168.0.101/Hazards/all.php";
    RequestQueue queue;
    Gson gson;
    Hazard[] Hazards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerlocation = new LatLng(3.0, 101);

        markerOptions = new Vector<>();

        gson = new GsonBuilder().create();
        markerOptions.add(new MarkerOptions().title("Putrajaya, Kuala Lumpur")
                .position(new LatLng(2.92931, 101.67418))
                .snippet("Banjir,20/1/2023,10:40AM,Zulqarnain")

        );
        markerOptions.add(new MarkerOptions().title("Alor Setar, Kedah")
                .position(new LatLng(6.14961, 100.36916))
                .snippet("Hujan Batu,13/1/2023,11:45AM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Ipoh, Perak")
                .position(new LatLng(4.60486, 101.09051))
                .snippet("Tsunami,11/11/2022,10:45AM,Zulqarnain")
        );


        markerOptions.add(new MarkerOptions().title("Bukit Mertajam, Pulau Pinang")
                .position(new LatLng(5.41581, 100.31208))
                .snippet("Gempa Bumi,2/12/2022,3:00AM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Raub, Pahang")
                .position(new LatLng(4.02328, 101.75915))
                .snippet("Puting Beliung,8/8/2022,12:00AM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Pekan, Pahang")
                .position(new LatLng(3.67251, 103.36315))
                .snippet("Kemarau,14/11/2022,10:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Kangar, Perlis")
                .position(new LatLng(6.45957, 100.19368))
                .snippet("Gempa Bumi,7/2/2023,5:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Jitra, Kedah")
                .position(new LatLng(6.27981, 100.41919))
                .snippet("Ribut Petir,6/9/2022,6:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Sungai Petani, Kedah")
                .position(new LatLng(5.668783816109874, 100.51626195362032))
                .snippet("Hujan Batu,1/1/2023,8:00AM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Gurun, Kedah")
                .position(new LatLng(6.15795, 100.40601))
                .snippet("Banjir Kilat,1/12/2022,9:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Kuala Lumpur, Wilayah Persekutuan")
                .position(new LatLng(3.17071, 101.70303))
                .snippet("Jerebu,10/11/2022,1:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Merbok, Kedah")
                .position(new LatLng(5.684813052911848, 100.49422913129347))
                .snippet("Ribut Petir,14/10/2022,4:30PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Gua Musang, Kelantan")
                .position(new LatLng(4.859711992118417, 101.95460294012736))
                .snippet("Tanah Runtuh,9/11/2022,2:00PM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Kota Bharu, Kelantan")
                .position(new LatLng(46.12555498511225, 102.24576564197703))
                .snippet("Banjir Kilat,20/12/2022,8:00AM,Zulqarnain")
        );

        markerOptions.add(new MarkerOptions().title("Alor Gajah, Melaka")
                .position(new LatLng(2.3956585874533705, 102.20207027081571))
                .snippet("Tanah Runtuh,21/1/2023,9:45AM,Zulqarnain")
        );


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for (MarkerOptions mark : markerOptions) {
            mMap.addMarker(mark);
        }

        enableMyLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation, 8));
        sendRequest();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            String perms[] = {"android.permission.ACCESS_FINE_LOCATION"};
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this, perms, 200);


        }
    }

    public void sendRequest() {
        queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, onSuccess, onError);
        queue.add(stringRequest);


    }

    public Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Hazards = gson.fromJson(response, Hazard[].class);
            Log.d("Server.Hazard", "number of maklumat data point" + Hazards.length);

            if(Hazards.length<1){
                Toast.makeText(getApplicationContext(),"Problem retrieving JSON data",Toast.LENGTH_LONG).show();
                return;

            }
            for (Hazard info : Hazards) {
                String title = info.location;
                String snippet = info.hazardDesc;
                Double lat = Double.parseDouble(info.lat);
                Double lng = Double.parseDouble(info.lng);


                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng))
                        .title(title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                mMap.addMarker(marker);


            }


        }

    };
    public Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}




