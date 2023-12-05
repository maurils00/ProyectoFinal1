package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button volver;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        volver = findViewById(R.id.btnZoom);
        volver.setVisibility(View.INVISIBLE);
        home = findViewById(R.id.btnInicio);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 9;

        LatLng hospitalSanca = new LatLng(-36.4298877, -71.9597810775311);
        mMap.addMarker(new MarkerOptions().position(hospitalSanca).title("Hospital de San Carlos"));

        LatLng hospitalYungay = new LatLng(-37.12185, -72.02406);//
        mMap.addMarker(new MarkerOptions().position(hospitalYungay).title("Hospital de Yungay"));

        LatLng hospitalQuirihue = new LatLng(-36.28669, -72.54749);//
        mMap.addMarker(new MarkerOptions().position(hospitalQuirihue).title("Hospital de Quirihue"));

        LatLng hospitalElCarmen = new LatLng(-36.8981, -72.0349);//
        mMap.addMarker(new MarkerOptions().position(hospitalElCarmen).title("Hospital de El Carmen"));

        LatLng hospitalBulnes = new LatLng(-36.73924, -72.29629);
        mMap.addMarker(new MarkerOptions().position(hospitalBulnes).title("Hospital de Bulnes"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalBulnes, zoomLevel));

        LatLng hospitalCoelemu = new LatLng(-36.49052,  -72.70545);//
        mMap.addMarker(new MarkerOptions().position(hospitalCoelemu).title("Hospital de Coelemu"));

        LatLng hospitalChillán = new LatLng(-36.60963, -72.09123);//
        mMap.addMarker(new MarkerOptions().position(hospitalChillán).title("Hospital de Chillán"));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalBulnes, zoomLevel));
                volver.setVisibility(View.INVISIBLE);
                home.setVisibility(View.VISIBLE);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                volver.setVisibility(View.VISIBLE);
                home.setVisibility(View.INVISIBLE);
                // Obtén la posición del marcador
                LatLng markerPosition = marker.getPosition();

                // Define el nivel de zoom que deseas aplicar al hacer clic en un marcador
                float zoom = 18f; // Puedes ajustar este valor según tus preferencias

                // Anima la cámara al marcador con el nivel de zoom deseado
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoom));

                // Devuelve true para indicar que has manejado el evento de clic en el marcador
                return true;
            }
        });
    }

}