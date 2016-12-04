package weatherHelper.sonClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Tmp {
    private String max;
    private String min;

    public Tmp(JSONObject jsonObject) {
        try {
            this.max = jsonObject.isNull("max")? null : jsonObject.getString("max");
            this.min = jsonObject.isNull("min")? null : jsonObject.getString("min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
