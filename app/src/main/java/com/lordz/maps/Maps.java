package com.lordz.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnPoiClickListener {

    Location loc;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    String lok1, lok2;
    TextInputEditText tempat1, tempat2;
    LatLng mDestination, mOrigin;
    SwipeRefreshLayout swipeRefreshLayout;
    Button amikom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        tempat1 = (TextInputEditText) findViewById(R.id.placeori);
        tempat2 = (TextInputEditText) findViewById(R.id.placedes);


        //Menentukan sebuah lokasi yang tersedia yaitu amikom purwokerto
        amikom = (Button) findViewById(R.id.amikom);
        amikom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempat2.setText("Universitas Amikom Purwokerto");
                searchLocationdes();
                amikom.setVisibility(View.GONE);
            }
        });

        tempat1.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchLocationori();
                    handle = true;
                }
                return handle;
            }
        });

        tempat2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchLocationdes();
                    handle = true;
                }
                return handle;
            }
        });


    }

    //mencari lokasi terbaru
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    loc = location;
                    Toast.makeText(getApplicationContext(), loc.getLatitude() + "" + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync((OnMapReadyCallback) Maps.this);

                    String lalt = String.valueOf(loc.getLatitude());
                    String lalang = String.valueOf(loc.getLongitude());
                    tempat1.setText(lalt+" , "+lalang);

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //memilih lokasi yang sudah di tentukan
        LatLng smkbuk = new LatLng(-7.4267165, 109.4230204);
        MarkerOptions samsak = new MarkerOptions().position(smkbuk).title("SMK N 1 Bukateja");
        mMap.addMarker(samsak);

        //setting tampilan pada fragment = zoom butn , mylocation,poi
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnPoiClickListener(this);


        // Sets the map type to be "hybrid"
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

    }

    //pengecekan permision
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    //pengecekan lokasi awal
    public void searchLocationori() {

        String location1 = tempat1.getText().toString();
        List<Address> addressList = null;
        if (location1 != null || !location1.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location1, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }


            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location1));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
        }
    }

    //pengecekan lokasi tujuan
    public void searchLocationdes() {

        String location2 = tempat2.getText().toString();
        List<Address> addressList = null;
        if (location2 != null || !location2.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location2, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }


            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location2));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
        }
    }

    //ktika menclik my lokasion akan membersiihkan mark dan mencari lokasi terbaru
    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        fetchLocation();
        amikom.setVisibility(View.VISIBLE);
        return false;
    }

    //ktika menclik my lokasion akan memberitahu lokasi terbaru kita
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(getApplicationContext(), "Clicked: " +
                        pointOfInterest.name + "\nPlace ID:" + pointOfInterest.placeId +
                        "\nLatitude:" + pointOfInterest.latLng.latitude +
                        " Longitude:" + pointOfInterest.latLng.longitude,
                Toast.LENGTH_SHORT).show();

    }
}