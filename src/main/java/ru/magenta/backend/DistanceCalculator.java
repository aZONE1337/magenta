package ru.magenta.backend;

import ru.magenta.entity.CityEntity;
import ru.magenta.service.DistanceDataAccess;

public class DistanceCalculator {
    //all formulas can be found on https://en.wikipedia.org/wiki/Great-circle_distance
    //l is the length of the arc of 1degree (on Earth l = 111.1 kms)
    //In the case of calculations in radians rather than degrees
    //l is replaced by the Earth's radius (6371.0km average)
    //so l is 6371.0km (avg)
    private static final double l = 6371.008;

    public static double calculateCrowFlight(CityEntity from, CityEntity to) {
        double distance;

        double phi1 = Math.toRadians(from.getLatitude());
        double lambda1 = Math.toRadians(from.getLongitude());

        double phi2 = Math.toRadians(to.getLatitude());
        double lambda2 = Math.toRadians(to.getLongitude());

        double delta;

        delta = Math.acos(
                Math.sin(phi1) * Math.sin(phi2)
                        +
                        Math.cos(phi1) * Math.cos(phi2) * Math.cos(Math.abs(lambda2 - lambda1))
        );

        distance = l * delta;

        return distance;
    }

    public static double calculateDistanceMatrix(CityEntity from, CityEntity to) {
        return DistanceDataAccess.getInstance().getDistance(from, to);
    }
}
