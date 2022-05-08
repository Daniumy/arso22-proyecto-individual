package utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Utils {

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts decimal degrees to radians : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts radians to decimal degrees : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static int getNewId() {
        File dir = new File("ciudades/");
        File[] files = dir.listFiles();

        if (files == null) {
            return 1;
        }

        return Arrays.stream(files)
                .map(f -> Integer.parseInt(
                        f.getName().substring(0, f.getName().length() - 4)
                ))
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }

}
