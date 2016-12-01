package weatherHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jierui on 2016/12/1.
 */

public class Heweather5 {
    private Aqi aqi;
    private Basic basic;
    private List<DailyForecast> dailyForecast;
    private List<HourForeCast> hourForeCast;
    private Now now;
    private String status;
    private Suggestion suggestion;

    public Heweather5(String str){
        str = "{\"HeWeather5\":[{\"aqi\":{\"city\":{\"aqi\":\"121\",\"co\":\"2\",\"no2\":\"91\",\"o3\":\"7\",\"pm10\":\"87\",\"pm25\":\"121\",\"qlty\":\"轻度污染\",\"so2\":\"18\"}},\"basic\":{\"city\":\"北京\",\"cnty\":\"中国\",\"id\":\"CN101010100\",\"lat\":\"39.904000\",\"lon\":\"116.391000\",\"update\":{\"loc\":\"2016-12-01 20:51\",\"utc\":\"2016-12-01 12:51\"}},\"daily_forecast\":[{\"astro\":{\"mr\":\"08:22\",\"ms\":\"18:28\",\"sr\":\"07:17\",\"ss\":\"16:50\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"100\",\"txt_d\":\"晴\",\"txt_n\":\"晴\"},\"date\":\"2016-12-01\",\"hum\":\"36\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1034\",\"tmp\":{\"max\":\"9\",\"min\":\"-4\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"272\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"6\"}},{\"astro\":{\"mr\":\"09:12\",\"ms\":\"19:18\",\"sr\":\"07:17\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"502\",\"txt_d\":\"晴\",\"txt_n\":\"霾\"},\"date\":\"2016-12-02\",\"hum\":\"38\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1031\",\"tmp\":{\"max\":\"9\",\"min\":\"-4\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"149\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"2\"}},{\"astro\":{\"mr\":\"09:58\",\"ms\":\"20:11\",\"sr\":\"07:18\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"502\",\"code_n\":\"502\",\"txt_d\":\"霾\",\"txt_n\":\"霾\"},\"date\":\"2016-12-03\",\"hum\":\"51\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1023\",\"tmp\":{\"max\":\"9\",\"min\":\"-1\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"142\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"7\"}},{\"astro\":{\"mr\":\"10:41\",\"ms\":\"21:08\",\"sr\":\"07:19\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"502\",\"code_n\":\"502\",\"txt_d\":\"霾\",\"txt_n\":\"霾\"},\"date\":\"2016-12-04\",\"hum\":\"35\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1022\",\"tmp\":{\"max\":\"9\",\"min\":\"-1\"},\"uv\":\"1\",\"vis\":\"10\",\"wind\":{\"deg\":\"352\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"0\"}},{\"astro\":{\"mr\":\"11:21\",\"ms\":\"22:07\",\"sr\":\"07:20\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"100\",\"txt_d\":\"晴\",\"txt_n\":\"晴\"},\"date\":\"2016-12-05\",\"hum\":\"38\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1032\",\"tmp\":{\"max\":\"7\",\"min\":\"-4\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"217\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"5\"}},{\"astro\":{\"mr\":\"11:58\",\"ms\":\"23:09\",\"sr\":\"07:21\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"100\",\"txt_d\":\"晴\",\"txt_n\":\"晴\"},\"date\":\"2016-12-06\",\"hum\":\"48\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1021\",\"tmp\":{\"max\":\"5\",\"min\":\"-3\"},\"uv\":\"-999\",\"vis\":\"10\",\"wind\":{\"deg\":\"157\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"1\"}},{\"astro\":{\"mr\":\"12:32\",\"ms\":\"null\",\"sr\":\"07:22\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"101\",\"code_n\":\"101\",\"txt_d\":\"多云\",\"txt_n\":\"多云\"},\"date\":\"2016-12-07\",\"hum\":\"43\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1022\",\"tmp\":{\"max\":\"5\",\"min\":\"-4\"},\"uv\":\"-999\",\"vis\":\"10\",\"wind\":{\"deg\":\"318\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"0\"}}],\"hourly_forecast\":[{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-01 22:00\",\"hum\":\"40\",\"pop\":\"0\",\"pres\":\"1035\",\"tmp\":\"1\",\"wind\":{\"deg\":\"297\",\"dir\":\"西南风\",\"sc\":\"微风\",\"spd\":\"4\"}}],\"now\":{\"cond\":{\"code\":\"101\",\"txt\":\"多云\"},\"fl\":\"-1\",\"hum\":\"43\",\"pcpn\":\"0\",\"pres\":\"1033\",\"tmp\":\"4\",\"vis\":\"10\",\"wind\":{\"deg\":\"0\",\"dir\":\"西南风\",\"sc\":\"3-4\",\"spd\":\"15\"}},\"status\":\"ok\",\"suggestion\":{\"air\":{\"brf\":\"较差\",\"txt\":\"气象条件较不利于空气污染物稀释、扩散和清除，请适当减少室外活动时间。\"},\"comf\":{\"brf\":\"较舒适\",\"txt\":\"白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。\"},\"cw\":{\"brf\":\"较适宜\",\"txt\":\"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。\"},\"drsg\":{\"brf\":\"较冷\",\"txt\":\"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。\"},\"flu\":{\"brf\":\"极易发\",\"txt\":\"昼夜温差极大，且空气湿度较大，寒冷潮湿，极易发生感冒，请特别注意增减衣服保暖防寒。\"},\"sport\":{\"brf\":\"较适宜\",\"txt\":\"天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。\"},\"trav\":{\"brf\":\"适宜\",\"txt\":\"天气较好，气温稍低，会感觉稍微有点凉，不过也是个好天气哦。适宜旅游，可不要错过机会呦！\"},\"uv\":{\"brf\":\"弱\",\"txt\":\"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。\"}}}]}";
        System.out.println(str);
        try {
            JSONObject JO = new JSONObject(str);
            if (!JO.isNull("HeWeather5")) {
                JO = JO.getJSONObject("HeWeather5");
                if (!JO.isNull("status")) {
                    if (JO.getString("status").equals("ok")) {
                        aqi = JO.isNull("aqi") ? null : new Aqi(JO.getJSONObject("aqi"));
                        basic = JO.isNull("basic") ? null : new Basic(JO.getJSONObject("basic"));
                        now = JO.isNull("now") ? null : new Now(JO.getJSONObject("now"));
                        suggestion = JO.isNull("suggestion") ? null : new Suggestion(JO.getJSONObject("suggestion"));
                        if (!JO.isNull("daily_forecast")) {
                            List<DailyForecast> dfArray = new ArrayList<DailyForecast>();
                            JSONArray dfJArray = JO.getJSONArray("daily_forecast");
                            for (int i = 0; i < dfJArray.length(); i++) {
                                DailyForecast df = new DailyForecast(dfJArray.get(i));
                                dfArray.add(df);
                            }
                        }
                        if (!JO.isNull("hourly_forecast")) {
                            List<HourForeCast> hfArray = new ArrayList<HourForeCast>();
                            JSONArray hfJArray = JO.getJSONArray("hourly_forecast");
                            for (int i = 0; i < hfJArray.length(); i++) {
                                HourForeCast hf = new HourForeCast(hfJArray.get(i));
                                hfArray.add(hf);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        Heweather5 h = new Heweather5("wangzonghao");
    }


    public Heweather5() {
    }

    public Aqi getAqi() {
        return aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public List<DailyForecast> getDailyForecast() {
        return dailyForecast;
    }

    public List<HourForeCast> getHourForeCast() {
        return hourForeCast;
    }

    public Now getNow() {
        return now;
    }

    public String getStatus() {
        return status;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }
}
