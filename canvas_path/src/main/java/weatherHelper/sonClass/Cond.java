package weatherHelper.sonClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jierui on 2016/12/1.
 */

public class Cond {
    private String code_d;
    private String code_n;
    private String txt_d;
    private String txt_n;

    public Cond(JSONObject jsonObject) {
        try {
            this.code_d = jsonObject.isNull("code_d")? null : jsonObject.getString("code_d");
            this.code_n = jsonObject.isNull("code_n")? null : jsonObject.getString("code_n");
            this.txt_d = jsonObject.isNull("txt_d")? null : jsonObject.getString("txt_d");
            this.txt_n = jsonObject.isNull("txt_n")? null : jsonObject.getString("txt_n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getCode_n() {
        return code_n;
    }

    public void setCode_n(String code_n) {
        this.code_n = code_n;
    }

    public String getCode_d() {
        return code_d;
    }

    public void setCode_d(String code_d) {
        this.code_d = code_d;
    }

    public String getTxt_d() {
        return txt_d;
    }

    public void setTxt_d(String txt_d) {
        this.txt_d = txt_d;
    }

    public String getTxt_n() {
        return txt_n;
    }

    public void setTxt_n(String txt_n) {
        this.txt_n = txt_n;
    }
}
