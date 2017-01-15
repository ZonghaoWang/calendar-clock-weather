package calendar;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import weatherHelper.Heweather5;

/**
 * Created by jierui on 2016/12/26.
 */

public class HistoryTodayHelper {
    public ArrayList<HashMap<String, String>> getList() {
        return list;
    }

    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    HashMap<String,String> map=null;
    public HistoryTodayHelper(final Handler handler) {
        final String finalStr = "http://www.ipip5.com/today/api.php?type=txt";
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(finalStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(in);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    String result = "";
                    String readLine = null;
                    int i = 0;
                    while ((readLine = bufferedReader.readLine()) != null) {
                        result += readLine;
                        if (i > 0){
                            String [] strings = readLine.split(" ");
                            String year = strings[0];
                            String event;
                            if (strings.length == 1){
                                event = bufferedReader.readLine();
                            }else {
                                event = readLine.split(" ")[1];
                            }
                            map = new HashMap<String, String>();
                            map.put("year", year + "年");
                            map.put("event", event);
                            list.add(map);
                        }
                        i += 1;
                    }
                    in.close();
                    connection.disconnect();
                    list.remove(list.size() - 1);
                    //需要数据传递，用下面方法；
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = HistoryTodayHelper.this;//可以是基本类型，可以是对象，可以是List、map等；
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
