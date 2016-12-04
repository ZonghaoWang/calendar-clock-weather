package weatherHelper.sonClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Astro {
    private String mr;
    private String ms;
    private String sr;
    private String ss;

    public Astro(JSONObject jsonObject) {
        try {
            this.mr = jsonObject.isNull("mr")? null : jsonObject.getString("mr");
            this.ms = jsonObject.isNull("ms")? null : jsonObject.getString("ms");
            this.sr = jsonObject.isNull("sr")? null : jsonObject.getString("sr");
            this.ss = jsonObject.isNull("ss")? null : jsonObject.getString("ss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }
}
