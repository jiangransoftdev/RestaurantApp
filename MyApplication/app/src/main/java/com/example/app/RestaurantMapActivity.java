package com.example.app;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class RestaurantMapActivity extends FragmentActivity implements OnMapReadyCallback {
    public final static String EXTRA_LATLNG = "EXTRA_LATLNG";
    private LatLng toMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.restaurant_map);
        mapFragment.getMapAsync(this);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            toMark = bundle.getParcelable(EXTRA_LATLNG);
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(30, -120)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(toMark));
        map.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
    }

}
