package weatherHelper;

import org.json.JSONException;
import org.json.JSONObject;

import weatherHelper.sonClass.Wind;

/**
 * Created by jierui on 2016/12/1.
 */

public class Now {
    private String condCode;
    private String condTxt;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
    private Wind wind;

    public Now(JSONObject jsonObject) {
        try {
            this.fl = jsonObject.isNull("fl")? null : jsonObject.getString("fl");
            this.hum = jsonObject.isNull("hum")? null : jsonObject.getString("hum");
            this.pcpn = jsonObject.isNull("pcpn")? null : jsonObject.getString("pcpn");
            this.pres = jsonObject.isNull("pres")? null : jsonObject.getString("pres");
            this.tmp = jsonObject.isNull("tmp")? null : jsonObject.getString("tmp");
            this.vis = jsonObject.isNull("vis")? null : jsonObject.getString("vis");
            JSONObject jsonObjectCond = jsonObject.isNull("cond")? null : jsonObject.getJSONObject("cond");
            if (jsonObjectCond != null) {
                this.condCode = jsonObjectCond.isNull("code") ? null : jsonObjectCond.getString("code");
                this.condTxt = jsonObjectCond.isNull("txt") ? null : jsonObjectCond.getString("txt");
            }
            this.wind = jsonObject.isNull("wind")? null : new Wind(jsonObject.getJSONObject("wind"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String f1) {
        this.fl = f1;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
