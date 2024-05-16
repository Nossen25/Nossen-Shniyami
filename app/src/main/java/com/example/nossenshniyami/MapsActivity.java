package com.example.nossenshniyami;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.nossenshniyami.BusinessModel.Business;
import com.example.nossenshniyami.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int markerindex=0;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ImageButton btnBackH, btnCirclzition; // corrected comma
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnBackH = findViewById(R.id.btnBackH);
        btnCirclzition = findViewById(R.id.btnCirclzition);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnBackH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        btnCirclzition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Move the camera to the home marker
//                LatLng homeLatLng = new LatLng(31.953770, 34.920349);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 16.7f));
//
//            }
//        });
    }

//זה אמור להיות למצוא את המיקום שלי לשאול את יואל וגרטי
    //+ לשאול את אביתר בנוגע לדף הראשי והפרגמנטים ולהסביר לו על הפרוייקט ולשאול לדעתו
    //לתקן את איך שההום פייג נראה בטלפון רגיל ולא בdp
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng home = new LatLng(31.953770, 34.920349);
        mMap.addMarker(new MarkerOptions().position(home).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        LatLng sy = new LatLng(31, 34);
        mMap.addMarker(new MarkerOptions().position(sy).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        EZUI();







//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
//                    mMap.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .title("Your Location"));
//                }
//            }
//        });

        btnCirclzition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permissions
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request location permissions if not granted
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    return;
                }

                // Get last known location
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Move the camera to the current location
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Your Location"));
                        }
                    }
                });
            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform action that requires permission
                // You can repeat the code inside onClick method here to handle the button click
            } else {
                // Permission denied
                // Handle accordingly, maybe show a message or disable functionality
            }
        }
    }
    private void addMarkerForBusiness(String businessName, String businessAddress) {
        Geocoder geocoder = new Geocoder(this);

        try {
            // Geocode the business address to obtain coordinates
            List<Address> addresses = geocoder.getFromLocationName(businessAddress, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Add a marker to the map
                LatLng businessLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(businessLocation).title(businessName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(businessLocation, 15)); // Adjust zoom level as needed
            } else {
                // Handle case where address could not be geocoded
                Toast.makeText(this, "Failed to geocode address"+ " "+ businessName, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException
            Toast.makeText(this, "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void EZUI()
    {
        db.collection("Business")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {

                            LinkedList<Business> tempL = new LinkedList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Business bus = new Business();

                                addMarkerForBusiness(document.getData().get("BusinessName").toString(),document.getData().get("BusinessAddress").toString());
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
