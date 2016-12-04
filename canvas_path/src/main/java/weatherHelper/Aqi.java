package weatherHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Aqi {
    private String aqi;
    private String co;
    private String no2;
    private String o3;
    private String pm10;
    private String pm25;
    private String qlty;
    private String so2;

    public Aqi(JSONObject jsonObject) {
        if (!jsonObject.isNull("city")){
            try {
                jsonObject = jsonObject.getJSONObject("city");
                this.aqi = jsonObject.isNull("aqi")? null : jsonObject.getString("aqi");
                this.co = jsonObject.isNull("co")? null : jsonObject.getString("co");
                this.no2 = jsonObject.isNull("no2")? null : jsonObject.getString("no2");
                this.o3 = jsonObject.isNull("o3")? null : jsonObject.getString("o3");
                this.pm10 = jsonObject.isNull("pm10")? null : jsonObject.getString("pm10");
                this.pm25 = jsonObject.isNull("pm25")? null : jsonObject.getString("pm25");
                this.qlty = jsonObject.isNull("qlty")? null : jsonObject.getString("qlty");
                this.so2 = jsonObject.isNull("so2")? null : jsonObject.getString("so2");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }
}
