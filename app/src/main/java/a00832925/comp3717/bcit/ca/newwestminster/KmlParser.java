package a00832925.comp3717.bcit.ca.newwestminster;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2017-03-09.
 */

public class KmlParser {

    public static final String TAG_START = "<coordinates>";
    public static final String TAG_END   = "</coordinates>";

    public KmlParser() {

    }

//    public static Polygon getPolygon(BufferedReader reader) {
//        String line;
//        String coordinates = "";
//        List<LatLng> list = new ArrayList<>();
//        try {
//            line = reader.readLine().trim();
//            while (!line.equalsIgnoreCase(TAG_START)) {
//                line = reader.readLine().trim();
//            }
//            while (!(line = reader.readLine().trim()).equalsIgnoreCase(TAG_END)) {
//                String[] coords = line.split(",| ");
//                Log.i("coord number", coords.length + "");
//                for (int i = 0; i < coords.length; i++) {
//                    if (Double.parseDouble(coords[i]) != 0) {
//                        coordinates = coordinates + coords[i] + " ";
//                        Log.i("each coord", coords[i]);
//                    }
//                }
//
//                double lat = 0;
//                double longi = 0;
//                for (int j = 0; j < coords.length; j++) {
//                    if (j % 2 == 0) {
//                        longi = Double.parseDouble(coords[j]);
//                    } else {
//                        lat = Double.parseDouble(coords[j]);
//                        list.add(new LatLng(lat, longi));
//                    }
//                }
//            }
//            return new Polygon().getPoints(list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
