package weatherHelper.sonClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Update {
    private String loc;
    private String utc;

    public Update(JSONObject jsonObject) {
        try {
            this.loc = jsonObject.isNull("loc")? null : jsonObject.getString("loc");
            this.utc = jsonObject.isNull("utc")? null : jsonObject.getString("utc");
            
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}
