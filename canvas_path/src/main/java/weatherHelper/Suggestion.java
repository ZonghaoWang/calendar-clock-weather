package weatherHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Suggestion {
    private String airBrf;
    private String airTxt;
    private String comfBrf;
    private String comfTxt;
    private String cwBrf;
    private String cwTxt;
    private String drsgBrf;
    private String drsgTxt;
    private String fluBrf;
    private String fluTxt;
    private String sportBrf;
    private String sportTxt;
    private String travBrf;
    private String travTxt;
    private String uvBrf;
    private String uvTxt;

    public Suggestion(JSONObject jsonObject) {
        try {
            JSONObject jsonObjectAir = jsonObject.isNull("air")? null : jsonObject.getJSONObject("air");
            JSONObject jsonObjectComf = jsonObject.isNull("comf")? null : jsonObject.getJSONObject("comf");
            JSONObject jsonObjectCw = jsonObject.isNull("cw")? null : jsonObject.getJSONObject("cw");
            JSONObject jsonObjectDrsg = jsonObject.isNull("drsg")? null : jsonObject.getJSONObject("drsg");
            JSONObject jsonObjectFlu = jsonObject.isNull("flu")? null : jsonObject.getJSONObject("flu");
            JSONObject jsonObjectSport = jsonObject.isNull("sport")? null : jsonObject.getJSONObject("sport");
            JSONObject jsonObjectTrav = jsonObject.isNull("trav")? null : jsonObject.getJSONObject("trav");
            JSONObject jsonObjectUv = jsonObject.isNull("uv")? null : jsonObject.getJSONObject("uv");

            if (jsonObjectAir != null){
                airBrf = jsonObjectAir.isNull("brf")? null : jsonObjectAir.getString("brf");
                airTxt = jsonObjectAir.isNull("txt")? null : jsonObjectAir.getString("txt");
            }
            if (jsonObjectComf != null){
                comfBrf = jsonObjectComf.isNull("brf")? null : jsonObjectComf.getString("brf");
                comfTxt = jsonObjectComf.isNull("txt")? null : jsonObjectComf.getString("txt");
            }
            if (jsonObjectCw != null){
                cwBrf = jsonObjectCw.isNull("brf")? null : jsonObjectCw.getString("brf");
                cwTxt = jsonObjectCw.isNull("txt")? null : jsonObjectCw.getString("txt");
            }
            if (jsonObjectDrsg != null){
                drsgBrf = jsonObjectDrsg.isNull("brf")? null : jsonObjectDrsg.getString("brf");
                drsgTxt = jsonObjectDrsg.isNull("txt")? null : jsonObjectDrsg.getString("txt");
            }
            if (jsonObjectFlu != null){
                fluBrf = jsonObjectFlu.isNull("brf")? null : jsonObjectFlu.getString("brf");
                fluTxt = jsonObjectFlu.isNull("txt")? null : jsonObjectFlu.getString("txt");
            }
            if (jsonObjectSport != null){
                sportBrf = jsonObjectSport.isNull("brf")? null : jsonObjectSport.getString("brf");
                sportTxt = jsonObjectSport.isNull("txt")? null : jsonObjectSport.getString("txt");
            }
            if (jsonObjectTrav != null){
                travBrf = jsonObjectTrav.isNull("brf")? null : jsonObjectTrav.getString("brf");
                travTxt = jsonObjectTrav.isNull("txt")? null : jsonObjectTrav.getString("txt");
            }
            if (jsonObjectUv != null){
                uvBrf = jsonObjectUv.isNull("brf")? null : jsonObjectUv.getString("brf");
                uvTxt = jsonObjectUv.isNull("txt")? null : jsonObjectUv.getString("txt");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getAirBrf() {
        return airBrf;
    }

    public void setAirBrf(String airBrf) {
        this.airBrf = airBrf;
    }

    public String getAirTxt() {
        return airTxt;
    }

    public void setAirTxt(String airTxt) {
        this.airTxt = airTxt;
    }

    public String getComfBrf() {
        return comfBrf;
    }

    public void setComfBrf(String comfBrf) {
        this.comfBrf = comfBrf;
    }

    public String getComfTxt() {
        return comfTxt;
    }

    public void setComfTxt(String comfTxt) {
        this.comfTxt = comfTxt;
    }

    public String getCwBrf() {
        return cwBrf;
    }

    public void setCwBrf(String cwBrf) {
        this.cwBrf = cwBrf;
    }

    public String getCwTxt() {
        return cwTxt;
    }

    public void setCwTxt(String cwTxt) {
        this.cwTxt = cwTxt;
    }

    public String getDrsgBrf() {
        return drsgBrf;
    }

    public void setDrsgBrf(String drsgBrf) {
        this.drsgBrf = drsgBrf;
    }

    public String getDrsgTxt() {
        return drsgTxt;
    }

    public void setDrsgTxt(String drsgTxt) {
        this.drsgTxt = drsgTxt;
    }

    public String getFluBrf() {
        return fluBrf;
    }

    public void setFluBrf(String fluBrf) {
        this.fluBrf = fluBrf;
    }

    public String getFluTxt() {
        return fluTxt;
    }

    public void setFluTxt(String fluTxt) {
        this.fluTxt = fluTxt;
    }

    public String getSportBrf() {
        return sportBrf;
    }

    public void setSportBrf(String sportBrf) {
        this.sportBrf = sportBrf;
    }

    public String getSportTxt() {
        return sportTxt;
    }

    public void setSportTxt(String sportTxt) {
        this.sportTxt = sportTxt;
    }

    public String getTravBrf() {
        return travBrf;
    }

    public void setTravBrf(String travBrf) {
        this.travBrf = travBrf;
    }

    public String getTravTxt() {
        return travTxt;
    }

    public void setTravTxt(String travTxt) {
        this.travTxt = travTxt;
    }

    public String getUvBrf() {
        return uvBrf;
    }

    public void setUvBrf(String uvBrf) {
        this.uvBrf = uvBrf;
    }

    public String getUvTxt() {
        return uvTxt;
    }

    public void setUvTxt(String uvTxt) {
        this.uvTxt = uvTxt;
    }
}
