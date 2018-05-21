package leif.com.atmfinder.model;

import java.io.Serializable;

/**
 * Created by Leif on 3/14/2018.
 */

public class ATMModel implements Serializable{
    private String operName;
    private String address;
    private String openHour;
    private String city;
    private String country;
    private String place;
    private String distance;
    private Double latitude;
    private Double longitude;

    public void setOperName(String value) {
        this.operName = value;
    }

    public String getOperName() {
        return operName;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getAddress() {
        return address;
    }

    public void setOpenHour(String value) {
        this.openHour = value;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setCity(String value) {
        this.city = value;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String value) {
        this.country = value;
    }

    public String getCountry() {
        return country;
    }

    public void setPlace(String value) {
        this.place = value;
    }

    public String getPlace() {
        return place;
    }

    public void setDistance(String value) {
        this.distance = value;
    }

    public String getDistance() {
        return distance;
    }

    public void setLatitude(double value) {
        this.latitude = value;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double value) {
        this.longitude = value;
    }

    public double getLongitude() {
        return longitude;
    }
}
