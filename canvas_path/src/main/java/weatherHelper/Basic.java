package weatherHelper;

import org.json.JSONObject;

import weatherHelper.sonClass.Update;

/**
 * Created by jierui on 2016/12/1.
 */

public class Basic {
    private String city;
    private String cnty;
    private String id;
    private String lat;
    private String lon;
    private Update update;

    public Basic(JSONObject basic) {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
