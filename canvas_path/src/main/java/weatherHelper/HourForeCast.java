package weatherHelper;

import org.json.JSONException;
import org.json.JSONObject;

import weatherHelper.sonClass.Wind;

/**
 * Created by jierui on 2016/12/1.
 */

public class HourForeCast {
    private String condCode;
    private String condTxt;
    private String date;
    private String hum;
    private String pop;
    private String pres;
    private String tmp;
    private Wind wind;

    public HourForeCast(JSONObject jsonObject) {
        JSONObject jsonObjectCond = null;
        try {
            this.date = jsonObject.isNull("date")? null : jsonObject.getString("date");
            this.hum = jsonObject.isNull("hum")? null : jsonObject.getString("hum");
            this.pop = jsonObject.isNull("pop")? null : jsonObject.getString("pop");
            this.pres = jsonObject.isNull("pres")? null : jsonObject.getString("pres");
            this.tmp = jsonObject.isNull("tmp")? null : jsonObject.getString("tmp");
            this.wind = jsonObject.isNull("wind")? null : new Wind(jsonObject.getJSONObject("wind"));
            jsonObjectCond = jsonObject.isNull("cond")? null : jsonObject.getJSONObject("cond");
            if (jsonObjectCond != null) {
                this.condCode = jsonObjectCond.isNull("code") ? null : jsonObjectCond.getString("code");
                this.condTxt = jsonObjectCond.isNull("txt") ? null : jsonObjectCond.getString("txt");
            }
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
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

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
