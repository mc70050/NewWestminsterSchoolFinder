package a00832925.comp3717.bcit.ca.newwestminster;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class Map extends FragmentActivity implements OnMapReadyCallback, OnConnectionFailedListener {

    private GoogleMap mMap;
    private HashMap<String, Integer> map;
    private GoogleApiClient mGoogleApiClient;

    public static final String TAG_START = "<coordinates>";
    public static final String TAG_END   = "</coordinates>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createMap();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                search(place);
                Log.i("test Place info", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("test Place error", "An error occurred: " + status);
            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY).setCountry("CA")
                .build();

        autocompleteFragment.setFilter(typeFilter);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device.
     * This method will only be triggered once the user has installed
     Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bun = getIntent().getExtras();
        double longitude = bun.getDouble("longitude");
        double latitude = bun.getDouble("latitude");
        String name = bun.getString("name");
        Log.i("longitude", longitude + "");
        // Add a marker in New Westminster and move the camera
        LatLng NewWestminster = new LatLng(longitude, latitude);
        mMap.addMarker(new
                MarkerOptions().position(NewWestminster).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(NewWestminster));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        boolean search = bun.getBoolean("searchSchool");
        // Create the KML layer for school catchment zone if needed
        if (search) {
            String fullName = bun.getString("name").toLowerCase();
            String[] tokens = fullName.split(" ");
            String fileName = tokens[0] + "_" + tokens[1];
            Log.d("file name", fileName);
            showKmlLayer(fileName);
        }
    }

    private void createMap() {
        map = new HashMap<>();
        map.put("connaught_heights", R.raw.connaught_heights);
        map.put("ecole_fraser", R.raw.ecole_fraser);
        map.put("fw_howay", R.raw.fw_howay);
        map.put("glenbrook_middle", R.raw.glenbrook_middle);
        map.put("herbert_spencer", R.raw.herbert_spencer);
        map.put("lord_kelvin", R.raw.lord_kelvin);
        map.put("lord_tweedsmuir", R.raw.lord_tweedsmuir);
        map.put("qayqayt_elementary", R.raw.qayqayt_elementary);
        map.put("queen_elizabeth", R.raw.queen_elizabeth);
        map.put("queensborough_middle", R.raw.queensborough_middle);
        map.put("richard_mcbride", R.raw.richard_mcbride);
    }

    private void search(Place place) {
        LatLng latLng = place.getLatLng();
        mMap.clear();
        mMap.addMarker(new
                MarkerOptions().position(latLng).title(place.getName().toString()));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        findBoundaries(latLng);
    }

    public void showKmlLayer(String fileName) {
        try {
            InputStream in = getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));
            KmlLayer layer = new KmlLayer(mMap, in, getApplicationContext());
            if (mMap == null) {
                Log.i("Error", "map is null");
            }
            layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            Log.i("Error", "Something wrong with kml");
        } catch (IOException e) {
            Log.i("Error", "Something wrong with IO");
        } catch (Exception e) {
            // This is for post secondary schools because there are no boundaries for them
            // A resource not found exception will be thrown
            Log.i("Post Secondary", "post secondary has no boundary");
        }

    }

    public void findBoundaries(LatLng latlng) {
        Set<String> list = map.keySet();
        list.remove("ecole_fraser");
        list.remove("glenbrook_middle");
        list.remove("queensborough_middle");
        List<LatLng> coordList = new ArrayList<>();
        for (String file : list) {
            InputStream in = getResources().openRawResource(getResources().getIdentifier(file, "raw", getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String coordinates = "";
            try {
                line = reader.readLine().trim();
                while (!line.equalsIgnoreCase(TAG_START)) {
                    line = reader.readLine().trim();
                }
                String[] coords = new String[1];    // Initialized for avoiding warning
                while (!(line = reader.readLine().trim()).equalsIgnoreCase(TAG_END)) {
                    coords = line.split(",| ");
                    for (int i = 0; i < coords.length; i++) {
                        if (Double.parseDouble(coords[i]) != 0) {
                            coordinates = coordinates + coords[i] + " ";
                        }
                    }
                    coords = coordinates.split(" ");
                }   // All coordinates extracted into array coords
                double lat;
                double longi = 0;
                for (int j = 0; j < coords.length; j++) {
                    if (j % 2 == 0) {
                        longi = Double.parseDouble(coords[j]);
                        Log.i("longitude for coord", longi + "");
                    } else {
                        lat = Double.parseDouble(coords[j]);
                        coordList.add(new LatLng(lat, longi));
                        Log.i("latitude for coord", lat + "");
                    }
                }
                if (PolyUtil.containsLocation(latlng, coordList, false)) {
                    Log.i("Point", file + " is in the polygon");
                    showKmlLayer(file);
                    findMiddle(file);
                } else {
                    Log.i("Point", file + " is not in the polygon");
                }

                coordList.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findMiddle(String schoolName) {
        if (schoolName.equalsIgnoreCase("queen_elizabeth")) {
            showKmlLayer("queensborough_middle");
        } else if (schoolName.equalsIgnoreCase("richard_mcbride") || schoolName.equalsIgnoreCase("fw_howay")) {
            showKmlLayer("glenbrook_middle");
        } else {
            showKmlLayer("ecole_fraser");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
