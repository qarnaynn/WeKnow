package com.example.hospitaltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



import com.google.android.gms.tasks.OnSuccessListener;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    GoogleSignInClient mGoogleSignInClient;
    TextView tvName,tvEmail;

    TextView tvCoordinate, tvAddress;
    private LocationCallback locationCallback;
    LocationRequest locationRequest;

    String[] perms = {"android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.INTERNET",
            "android.permission.ACCESS_NETWORK_STATE"};

    private FusedLocationProviderClient fusedLocationClient;

    RequestQueue queue;
    final String URL = "http://172.20.10.3/signindemo/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        queue = Volley.newRequestQueue(getApplicationContext());//server
        tvCoordinate = (TextView) findViewById(R.id.tvCoordinate);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //check request permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, 200);
            return;
        }

        tvName = (TextView) findViewById(R.id.tvname);
        tvEmail = (TextView) findViewById(R.id.tvemail);


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Unable to detect location", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    tvCoordinate.setText("" + lat + " , " + lon);

                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocation(lat,lon,1);
                        Address address = addressList.get(0);
                        String line = address.getAddressLine(0);

                        tvAddress.setText(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                tvCoordinate.setText("" + lat + " , " + lon);
                String coords = "Latitude: "+lat+"  Longitude: "+lon;
                makeRequest(coords);


            }
        });

        // String name = getIntent().getStringExtra("Name");
        // String email = getIntent().getStringExtra("Email");

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            String name = acct.getDisplayName();
            String email = acct.getEmail();
            tvName.setText(name);
            tvEmail.setText(email);
        }

        //ni mana pi dia punya function ?
        //Button signout = findViewById(R.id.signout);
        // signout.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Button button1, button2, button3;

        button1 = (Button) findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }


        } );

        //ni signout lagi , okay okay . jangan tengok . neves
        button2 = (Button) findViewById(R.id.signout);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.signout:
                        signOut();
                        break;

                }
            }

        } );

        button3 = (Button) findViewById(R.id.btn2);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Server.class);
                startActivity(intent);
            }


        } );

    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, 200);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signout:
                signOut();
                break;

        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext(), " You have been signed out" , Toast.LENGTH_SHORT).show();
                        finish();
                        // ...
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.home:
                Toast.makeText(this, "This is home page",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                break;
            case R.id.apps:
                Toast.makeText(this, "This is about app page",Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(this, Apps.class);
                startActivity(intent3);
                break;



            case R.id.developer:
                Toast.makeText(this, "This is developer page",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, Developer.class);
                startActivity(intent1);
                break;

            case R.id.designer:
                Toast.makeText(this, "This is designer page",Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, Designer.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //put into database
    public void makeRequest(String coords) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

            }
        }, errorListener){
            @Override
            protected Map<String,String> getParams (){
                Map <String, String> params = new HashMap<>();

                params.put("name", tvName.getText().toString());
                params.put("email", tvEmail.getText().toString());
                params.put("coords", coords);

                return params;
            }
        };
        queue.add(stringRequest);

    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

        }
    };
}