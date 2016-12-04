package weatherHelper.sonClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Wind {
    private String deg;
    private String dir;
    private String sc;
    private String spd;

    public Wind(JSONObject jsonObject) {
        try {
            this.deg = jsonObject.isNull("deg")? null : jsonObject.getString("deg");
            this.dir = jsonObject.isNull("dir")? null : jsonObject.getString("dir");
            this.sc = jsonObject.isNull("sc")? null : jsonObject.getString("sc");
            this.spd = jsonObject.isNull("spd")? null : jsonObject.getString("spd");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSpd() {
        return spd;
    }

    public void setSpd(String spd) {
        this.spd = spd;
    }
}
