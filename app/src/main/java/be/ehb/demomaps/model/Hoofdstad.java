package be.ehb.demomaps.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Banaan on 20/01/2038. ;)
 */
public class Hoofdstad implements Serializable {

    public enum Continent {EUROPA, NOORD_AMERIKA, ZUID_AMERIKA, AFRIKA, AZIE, OCEANIE, ANTARTICA}

    transient private LatLng coord;
    private String cityName;
    private Continent continent;
    private int drawableId;

    public Hoofdstad() {
    }

    public Hoofdstad(LatLng coord, String cityName, Continent continent, int drawableId) {
        this.coord = coord;
        this.cityName = cityName;
        this.continent = continent;
        this.drawableId = drawableId;
    }

    public LatLng getCoord() {
        return coord;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
