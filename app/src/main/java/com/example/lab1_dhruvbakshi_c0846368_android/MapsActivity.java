package com.example.lab1_dhruvbakshi_c0846368_android;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lab1_dhruvbakshi_c0846368_android.databinding.ActivityMapsBinding;
import com.google.android.material.shape.MarkerEdgeTreatment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener  , GoogleMap.OnMarkerDragListener{

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;

    private ActivityMapsBinding binding;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        geocoder = new Geocoder(this);
       try {
           List<Address> addresses = geocoder.getFromLocationName("Toronto", 1);
           if (addresses.size() > 0) {
               Address address = addresses.get(0);
               LatLng toronto = new LatLng(address.getLatitude(), address.getLongitude());
               MarkerOptions markerOptions = new MarkerOptions()
                       .position(toronto)
                       .title(address.getLocality());
               mMap.addMarker(markerOptions);
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 16));
               Log.d(TAG, "OnMapReady" + address.toString());
           }
           }catch(IOException e){
               e.printStackTrace();

           }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Log.d(TAG , "onMapLongClick:" + latLng.toString());
        try{
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude , latLng.longitude, 1);
            if (addresses.size() > 0){
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                .draggable(true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        Log.d(TAG , "onMarkerDrag:");
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        Log.d(TAG , "onMarkerDragEnd:");
        LatLng latLng = marker.getPosition();
        try{
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude , latLng.longitude, 1);
            if (addresses.size() > 0){
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        Log.d(TAG , "onMarkerDragStart:");
    }
}