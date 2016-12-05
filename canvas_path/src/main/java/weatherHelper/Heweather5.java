package weatherHelper;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private String cityName;

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDailyForecast(List<DailyForecast> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public void setHourForeCast(List<HourForeCast> hourForeCast) {
        this.hourForeCast = hourForeCast;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public Heweather5(final Handler mHandler, String str){

        str = "https://free-api.heweather.com/v5/weather?key=89d4393f46ad47248f035a23dbf4c434&city=" + str;
        final String finalStr = str;
        new Thread(){
            public void run(){
                try {
                    URL url = new URL(finalStr);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    InputStreamReader in = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(in);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    String result = "";
                    String readLine = null;
                    while ((readLine = bufferedReader.readLine())!=null){
                        result += readLine;
                    }
                    in.close();
                    connection.disconnect();
                    try {
//                        System.out.println(result);
                        JSONObject JO = new JSONObject(result);
                        if (!JO.isNull("HeWeather5")) {
                            JSONArray jO = JO.getJSONArray("HeWeather5");
                            JO = (JSONObject) jO.get(0);
                            if (!JO.isNull("status")) {
                                if (JO.getString("status").equals("ok")) {
                                    aqi = JO.isNull("aqi") ? null : new Aqi(JO.getJSONObject("aqi"));
                                    basic = JO.isNull("basic") ? null : new Basic(JO.getJSONObject("basic"));
                                    if (basic!=null) {
                                        cityName = basic.getCity();
                                    }
                                    now = JO.isNull("now") ? null : new Now(JO.getJSONObject("now"));
                                    suggestion = JO.isNull("suggestion") ? null : new Suggestion(JO.getJSONObject("suggestion"));
                                    if (!JO.isNull("daily_forecast")) {
                                        dailyForecast = new ArrayList<DailyForecast>();
                                        JSONArray dfJArray = JO.getJSONArray("daily_forecast");
                                        for (int i = 0; i < dfJArray.length(); i++) {
                                            DailyForecast df = new DailyForecast((JSONObject)dfJArray.get(i));
                                            dailyForecast.add(df);
                                        }
                                    }
                                    if (!JO.isNull("hourly_forecast")) {
                                        hourForeCast = new ArrayList<HourForeCast>();
                                        JSONArray hfJArray = JO.getJSONArray("hourly_forecast");
                                        for (int i = 0; i < hfJArray.length(); i++) {
                                            HourForeCast hf = new HourForeCast((JSONObject)hfJArray.get(i));
                                            hourForeCast.add(hf);
                                        }
                                    }



                                    //需要数据传递，用下面方法；
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = Heweather5.this;//可以是基本类型，可以是对象，可以是List、map等；
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        str = "{\"HeWeather5\":[{\"aqi\":{\"city\":{\"aqi\":\"240\",\"co\":\"3\",\"no2\":\"108\",\"o3\":\"4\",\"pm10\":\"131\",\"pm25\":\"240\",\"qlty\":\"重度污染\",\"so2\":\"22\"}},\"basic\":{\"city\":\"北京\",\"cnty\":\"中国\",\"id\":\"CN101010100\",\"lat\":\"39.904000\",\"lon\":\"116.391000\",\"update\":{\"loc\":\"2016-12-02 23:51\",\"utc\":\"2016-12-02 15:51\"}},\"daily_forecast\":[{\"astro\":{\"mr\":\"09:12\",\"ms\":\"19:18\",\"sr\":\"07:17\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"502\",\"txt_d\":\"晴\",\"txt_n\":\"霾\"},\"date\":\"2016-12-02\",\"hum\":\"39\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1031\",\"tmp\":{\"max\":\"9\",\"min\":\"-3\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"157\",\"dir\":\"西南风\",\"sc\":\"微风\",\"spd\":\"10\"}},{\"astro\":{\"mr\":\"09:58\",\"ms\":\"20:11\",\"sr\":\"07:18\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"502\",\"code_n\":\"502\",\"txt_d\":\"霾\",\"txt_n\":\"霾\"},\"date\":\"2016-12-03\",\"hum\":\"49\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1022\",\"tmp\":{\"max\":\"9\",\"min\":\"-2\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"136\",\"dir\":\"无持续风向\",\"sc\":\"微风\",\"spd\":\"8\"}},{\"astro\":{\"mr\":\"10:41\",\"ms\":\"21:08\",\"sr\":\"07:19\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"502\",\"code_n\":\"104\",\"txt_d\":\"霾\",\"txt_n\":\"阴\"},\"date\":\"2016-12-04\",\"hum\":\"37\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1021\",\"tmp\":{\"max\":\"9\",\"min\":\"-2\"},\"uv\":\"1\",\"vis\":\"10\",\"wind\":{\"deg\":\"101\",\"dir\":\"北风\",\"sc\":\"微风\",\"spd\":\"6\"}},{\"astro\":{\"mr\":\"11:21\",\"ms\":\"22:07\",\"sr\":\"07:20\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"100\",\"txt_d\":\"晴\",\"txt_n\":\"晴\"},\"date\":\"2016-12-05\",\"hum\":\"27\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1032\",\"tmp\":{\"max\":\"6\",\"min\":\"-5\"},\"uv\":\"1\",\"vis\":\"10\",\"wind\":{\"deg\":\"318\",\"dir\":\"西南风\",\"sc\":\"微风\",\"spd\":\"0\"}},{\"astro\":{\"mr\":\"11:58\",\"ms\":\"23:09\",\"sr\":\"07:21\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"100\",\"txt_d\":\"晴\",\"txt_n\":\"晴\"},\"date\":\"2016-12-06\",\"hum\":\"35\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1023\",\"tmp\":{\"max\":\"5\",\"min\":\"-3\"},\"uv\":\"2\",\"vis\":\"10\",\"wind\":{\"deg\":\"150\",\"dir\":\"西风\",\"sc\":\"微风\",\"spd\":\"0\"}},{\"astro\":{\"mr\":\"12:32\",\"ms\":\"null\",\"sr\":\"07:22\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"100\",\"code_n\":\"101\",\"txt_d\":\"晴\",\"txt_n\":\"多云\"},\"date\":\"2016-12-07\",\"hum\":\"36\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1026\",\"tmp\":{\"max\":\"6\",\"min\":\"-4\"},\"uv\":\"-999\",\"vis\":\"10\",\"wind\":{\"deg\":\"307\",\"dir\":\"西风\",\"sc\":\"微风\",\"spd\":\"8\"}},{\"astro\":{\"mr\":\"13:07\",\"ms\":\"00:13\",\"sr\":\"07:23\",\"ss\":\"16:49\"},\"cond\":{\"code_d\":\"101\",\"code_n\":\"100\",\"txt_d\":\"多云\",\"txt_n\":\"晴\"},\"date\":\"2016-12-08\",\"hum\":\"32\",\"pcpn\":\"0.0\",\"pop\":\"0\",\"pres\":\"1021\",\"tmp\":{\"max\":\"4\",\"min\":\"-2\"},\"uv\":\"-999\",\"vis\":\"10\",\"wind\":{\"deg\":\"320\",\"dir\":\"西北风\",\"sc\":\"3-4\",\"spd\":\"16\"}}],\"hourly_forecast\":[{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 01:00\",\"hum\":\"50\",\"pop\":\"0\",\"pres\":\"1026\",\"tmp\":\"4\",\"wind\":{\"deg\":\"135\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"3\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 04:00\",\"hum\":\"60\",\"pop\":\"0\",\"pres\":\"1025\",\"tmp\":\"2\",\"wind\":{\"deg\":\"129\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"4\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 07:00\",\"hum\":\"56\",\"pop\":\"0\",\"pres\":\"1024\",\"tmp\":\"1\",\"wind\":{\"deg\":\"126\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"4\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 10:00\",\"hum\":\"39\",\"pop\":\"0\",\"pres\":\"1023\",\"tmp\":\"6\",\"wind\":{\"deg\":\"128\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"4\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 13:00\",\"hum\":\"31\",\"pop\":\"0\",\"pres\":\"1021\",\"tmp\":\"10\",\"wind\":{\"deg\":\"134\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"5\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 16:00\",\"hum\":\"46\",\"pop\":\"0\",\"pres\":\"1020\",\"tmp\":\"7\",\"wind\":{\"deg\":\"140\",\"dir\":\"东南风\",\"sc\":\"微风\",\"spd\":\"5\"}},{\"cond\":{\"code\":\"100\",\"txt\":\"晴\"},\"date\":\"2016-12-03 19:00\",\"hum\":\"56\",\"pop\":\"0\",\"pres\":\"1019\",\"tmp\":\"4\",\"wind\":{\"deg\":\"100\",\"dir\":\"东风\",\"sc\":\"微风\",\"spd\":\"3\"}},{\"cond\":{\"code\":\"103\",\"txt\":\"晴间多云\"},\"date\":\"2016-12-03 22:00\",\"hum\":\"54\",\"pop\":\"0\",\"pres\":\"1020\",\"tmp\":\"2\",\"wind\":{\"deg\":\"266\",\"dir\":\"西风\",\"sc\":\"微风\",\"spd\":\"5\"}}],\"now\":{\"cond\":{\"code\":\"502\",\"txt\":\"霾\"},\"fl\":\"-3\",\"hum\":\"75\",\"pcpn\":\"0\",\"pres\":\"1026\",\"tmp\":\"0\",\"vis\":\"5\",\"wind\":{\"deg\":\"50\",\"dir\":\"东风\",\"sc\":\"3-4\",\"spd\":\"10\"}},\"status\":\"ok\",\"suggestion\":{\"air\":{\"brf\":\"很差\",\"txt\":\"气象条件不利于空气污染物稀释、扩散和清除，请尽量避免在室外长时间活动。\"},\"comf\":{\"brf\":\"较舒适\",\"txt\":\"白天天气阴沉，会感到有点儿凉，但大部分人完全可以接受。\"},\"cw\":{\"brf\":\"不宜\",\"txt\":\"不宜洗车，未来24小时内有霾，如果在此期间洗车，会弄脏您的爱车。\"},\"drsg\":{\"brf\":\"较冷\",\"txt\":\"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。\"},\"flu\":{\"brf\":\"易发\",\"txt\":\"昼夜温差大，且空气湿度较大，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。\"},\"sport\":{\"brf\":\"较不宜\",\"txt\":\"有扬沙或浮尘，建议适当停止户外运动，选择在室内进行运动，以避免吸入更多沙尘，有损健康。\"},\"trav\":{\"brf\":\"较不宜\",\"txt\":\"空气质量差，不适宜旅游\"},\"uv\":{\"brf\":\"最弱\",\"txt\":\"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。\"}}}]}";
    }


//    public static void main(String[] args){
//        Heweather5 h = new Heweather5("beijing");
//    }


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
