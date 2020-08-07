package com.example.food;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class CurrentActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;

    TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);

        txtAddress = findViewById(R.id.txtAddress);
        getAddress(new LatLng(latitude,longitude));
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CurrentActivity.this,AddActivity.class);
                intent1.putExtra("latitude",latitude);
                intent1.putExtra("longitude",longitude);
                intent1.putExtra("address",txtAddress.getText().toString());
                startActivity(intent1);

            }
        });

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

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("현재위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude=latLng.latitude;
                longitude=latLng.longitude;

                mMap.clear();
                MarkerOptions mOptions=new MarkerOptions();
                mOptions.title("위치");
                mOptions.position(latLng);
                mOptions.snippet(latitude + "/" + longitude);
                mMap.addMarker(mOptions);

                /*
                Geocoder geocoder=new Geocoder(CurrentActivity.this, Locale.KOREAN);
                try{
                    List<Address> list=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(!list.isEmpty()){
                        String strAddress=list.get(0).getAddressLine(0).toString();
                        txtAddress.setText(strAddress);
                    }
                }catch(Exception e){ }
                */

                getAddress(latLng);
            }
        });
    }

    //주소구하기
    public void getAddress(LatLng latLng){
        Geocoder geocoder=new Geocoder(CurrentActivity.this, Locale.KOREAN);
        try{
            List<Address> list=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(!list.isEmpty()){
                String strAddress=list.get(0).getAddressLine(0).toString();
                txtAddress.setText(strAddress);
            }
        }catch(Exception e){ }
    }
}