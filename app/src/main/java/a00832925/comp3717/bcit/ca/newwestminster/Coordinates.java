package a00832925.comp3717.bcit.ca.newwestminster;

/**
 * Created by Michael Chen on 2017-03-23.
 */

public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(double lat, double longi) {
        latitude = lat;
        longitude = longi;
    }

    public double getLat() {
        return latitude;
    }

    public double getLongi() {
        return longitude;
    }
}
