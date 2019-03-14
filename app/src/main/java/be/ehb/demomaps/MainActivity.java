package be.ehb.demomaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import be.ehb.demomaps.model.HoofdStadDAO;
import be.ehb.demomaps.model.Hoofdstad;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    //magic numbers are bad mkay
    private final int REQUEST_LOCATION = 42;

    private final LatLng BRUSSEL = new LatLng(50.858712, 4.347446);
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frag_container, supportMapFragment)
                .commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnPolygonClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);

        //lelijke code, eerst zien dat het werkt en daarna opkuisen
        //early optimalisation is the root of all evil
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View myContentView = getLayoutInflater().inflate(R.layout.info_window, null, false);

                Hoofdstad current = (Hoofdstad) marker.getTag();

                TextView tvTitle = myContentView.findViewById(R.id.tv_info_window);
                tvTitle.setText( current.getCityName() );

                ImageView iv = myContentView.findViewById(R.id.iv_info_window);
                iv.setImageResource( current.getDrawableId());

                return  myContentView;
            }
        });


        setupCamera();
        addMarkers();
        startLocationUpdates();
    }

    private void setupCamera() {
        //update 1, show map
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(BRUSSEL, 6);
        mGoogleMap.animateCamera(update);
        //update 2, show map, zoom in to see 3D buildings, change angle
        /*
        CameraPosition.Builder positionBuilder = new CameraPosition.Builder();
        CameraPosition position = positionBuilder.target(BRUSSEL).zoom(18).tilt(60).build();
        CameraUpdate secondUpdate = CameraUpdateFactory.newCameraPosition(position);
        mGoogleMap.animateCamera(secondUpdate);
        */
    }

    private void addMarkers() {

        //extra voor Jorn
        mGoogleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(50.810094, 4.934319), new LatLng(50.993622, 4.834812), new LatLng(50.986376, 5.050556))
                .strokeColor(0xff000000)
                .fillColor(0xff75315A)
                .clickable(true)
                //ARGB
                //A =  alpha -> doorzichtigheid, ff is vol en 00 is doorzichtig
                //RGB -> zie kleurenwiel
        );

        //vanuit datasource
        for(Hoofdstad stad : HoofdStadDAO.getInstance().getHoofdsteden()){

            float hue = 0;

            switch (stad.getContinent()){
                case EUROPA: hue = BitmapDescriptorFactory.HUE_YELLOW;
                break;
                case AFRIKA:hue = BitmapDescriptorFactory.HUE_GREEN;
                break;
                case OCEANIE: hue = 200;
                break;
            }

            Marker m = mGoogleMap.addMarker(
                    new MarkerOptions()
                    .title(stad.getCityName())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
                    .position(stad.getCoord())
            );
            m.setTag(stad);
        }
    }


    private void startLocationUpdates() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_LOCATION);

            }else{
                mGoogleMap.setMyLocationEnabled(true);
            }
        }else{
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    //android studio probeert slimmer te doen dan het is
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_LOCATION){
            for(int result : grantResults)
                if(result == PackageManager.PERMISSION_GRANTED)
                    mGoogleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(getApplicationContext(), "Marginaaaaaaaaal", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent detailsIntent = new Intent(getApplicationContext(), HoofdstadDetailActivity.class);
        detailsIntent.putExtra("stad", (Hoofdstad)marker.getTag());
        startActivity(detailsIntent);
    }
}
