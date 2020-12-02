package ru.magenta.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class DistanceEntityPK implements Serializable {
    private int id;
    private int cityFrom;
    private int cityTo;

    @Column(name = "id", nullable = false)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "city_from", nullable = false)
    @Id
    public int getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(int cityFrom) {
        this.cityFrom = cityFrom;
    }

    @Column(name = "city_to", nullable = false)
    @Id
    public int getCityTo() {
        return cityTo;
    }

    public void setCityTo(int cityTo) {
        this.cityTo = cityTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceEntityPK that = (DistanceEntityPK) o;

        if (id != that.id) return false;
        if (cityFrom != that.cityFrom) return false;
        if (cityTo != that.cityTo) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + cityFrom;
        result = 31 * result + cityTo;
        return result;
    }
}
