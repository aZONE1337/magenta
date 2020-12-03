package ru.magenta.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.magenta.entity.CityEntity;
import ru.magenta.service.CityDataAccess;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {
    List<CityEntity> cities;
    GraphLoader graphLoader = GraphLoader.getInstance(true);

    @BeforeEach
    void setUp() {
        cities = CityDataAccess.getInstance().getAll();
    }

    @Test
    void calculateStraight() {
        double expected = 101.318;
        double actual = DistanceCalculator.calculateStraight(cities.get(0), cities.get(1));

        assertTrue(Math.abs(expected - actual) <= 5.0659, "Actual and expected difference <= 5%");
    }

    @Test
    void calculateByMatrix() {
        //path is [1-2, 2-5, 5-6, 6-7] * 100 each
        double expected = 400.0;
        double actual = DistanceCalculator.calculateByMatrix(cities.get(0), cities.get(6), graphLoader.getGraph());

        assertEquals(expected, actual);
    }
}