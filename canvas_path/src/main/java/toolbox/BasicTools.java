package toolbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jierui on 2016/12/6.
 */

public class BasicTools {
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
}
