package ru.magenta.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlType(name = "Distance")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name = "distance", schema = "distance-calculator")
@IdClass(DistanceEntityPK.class)
public class DistanceEntity {
    @XmlAttribute(name = "id")
    private int id;
    @XmlAttribute
    private double distance;
    @XmlElement
    private int cityFrom;
    @XmlElement
    private int cityTo;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "distance", nullable = false, precision = 10)
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Id
    @Column(name = "city_from", nullable = false)
    public int getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(int cityFrom) {
        this.cityFrom = cityFrom;
    }

    @Id
    @Column(name = "city_to", nullable = false)
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

        DistanceEntity that = (DistanceEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.distance, distance) != 0) return false;
        if (cityFrom != that.cityFrom) return false;
        if (cityTo != that.cityTo) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + cityFrom;
        result = 31 * result + cityTo;
        return result;
    }
}
