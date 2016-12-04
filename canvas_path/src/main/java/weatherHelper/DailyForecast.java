package weatherHelper;

import org.json.JSONException;
import org.json.JSONObject;

import weatherHelper.sonClass.Astro;
import weatherHelper.sonClass.Cond;
import weatherHelper.sonClass.Tmp;
import weatherHelper.sonClass.Wind;

/**
 * Created by jierui on 2016/12/1.
 */

public class DailyForecast {
    private Astro astro;
    private Cond cond;
    private String date;
    private String hum;
    private String pcpn;
    private String pop;
    private String pres;
    private Tmp tmp;
    private String uv;
    private String vis;
    private Wind wind;

    public DailyForecast(JSONObject jsonObject) {
        try {
            this.date = jsonObject.isNull("date")? null : jsonObject.getString("date");
            this.hum = jsonObject.isNull("hum")? null : jsonObject.getString("hum");
            this.pcpn = jsonObject.isNull("pcpn")? null : jsonObject.getString("pcpn");
            this.pop = jsonObject.isNull("pop")? null : jsonObject.getString("pop");
            this.pres = jsonObject.isNull("pres")? null : jsonObject.getString("pres");
            this.uv = jsonObject.isNull("uv")? null : jsonObject.getString("uv");
            this.vis = jsonObject.isNull("vis")? null : jsonObject.getString("vis");
            this.astro = jsonObject.isNull("astro")? null : new Astro(jsonObject.getJSONObject("astro"));
            this.cond = jsonObject.isNull("cond")? null : new Cond(jsonObject.getJSONObject("cond"));
            this.wind = jsonObject.isNull("wind")? null : new Wind(jsonObject.getJSONObject("wind"));
            this.tmp = jsonObject.isNull("tmp")? null : new Tmp(jsonObject.getJSONObject("tmp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
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

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
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

    public Tmp getTmp() {
        return tmp;
    }

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
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
