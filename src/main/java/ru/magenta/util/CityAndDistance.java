package ru.magenta.util;

import ru.magenta.entity.CityEntity;
import ru.magenta.entity.DistanceEntity;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(name = "CitiesAndDistances", propOrder = {"cities", "distances"})
@XmlRootElement()
public class CityAndDistance {

    private List<CityEntity> cities;

    private List<DistanceEntity> distances;

    public List<CityEntity> getCities() {
        return cities;
    }

    @XmlElement(name = "city")
    public void setCities(List<CityEntity> cities) {
        this.cities = cities;
    }

    public List<DistanceEntity> getDistances() {
        return distances;
    }

    @XmlElement(name = "distance")
    public void setDistances(List<DistanceEntity> distances) {
        this.distances = distances;
    }
}
