package com.salsabila.wanderapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

//extends FragmentActivity ubah jadi extends AppCompatActivity== biar headernya ada
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //memanggil map_style.json
        try {
            boolean success=mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(this, R.raw.map_style));
            if (!success){
                Log.e("Tag", "Style parsing failed.");
            }
        }catch (Resources.NotFoundException e){
            Log.e("Tag", "Can't find style. Error:",e);
        }

        // Add a marker in Sydney and move the camera
        LatLng jogja = new LatLng(-7.797, 110.370);
        mMap.addMarker(new MarkerOptions().position(jogja).title("Marker in Jogja")
                .snippet("indonesia"));
        float zoom=2; //tambahkan zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jogja,zoom));
        setMapLongClick(mMap); //manggil action setMapLongClick
        setPoiClick(mMap); //manggil point of interest
    }

    //menambahkan action, apabila ditekan lama akan menambahkan marker
    private void setMapLongClick(GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet=String.format(Locale.getDefault(),
                        "Lat:%1$.3f, Lng:%2$.3f",
                        latLng.latitude,latLng.longitude);
                map.addMarker(new MarkerOptions().snippet(snippet)
                    .position(latLng).title("Dropped Pin")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                          BitmapDescriptorFactory.HUE_BLUE)));
            }
        });
    }

    //menambahkan point of interest
    private void setPoiClick(GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                map.clear(); //untuk menghapus tag sebelumnya, jadi sebelum ditambahkan add market itu diclear dulu
                Marker poiMarker=map.addMarker(new MarkerOptions()
                .position(pointOfInterest.latLng)
                .title(pointOfInterest.name));
                poiMarker.showInfoWindow();
            }
        });
    }

    //menambahkan menu options ke header activity ini
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    //apabila menu diklik maka akan mengubah tampilan mapnya

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}