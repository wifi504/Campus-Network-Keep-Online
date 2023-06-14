import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time: 2023/06/13_21:12
 */

// 工具集
public class Tools {

    // 获取当前系统时间
    public String getCurrentTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");
        return dateFormat.format(currentDate);
    }

    // 传入json格式数据和筛选键名，输出一行值，如果异常，则直接输出原始值
    public String jsonToLine(String json, String[] keys) {
        StringBuilder output = new StringBuilder();

        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

            for (String key : keys) {
                if (jsonObject.has(key)) {
                    JsonElement value = jsonObject.get(key);
                    String info = value.getAsString();
                    info = info.replaceAll("^\"|\"$", "");
                    if ("".equals(info)) continue;
                    output.append(info).append(" ");
                }
            }
        } catch (JsonSyntaxException e) {
            if (json.startsWith("[异常]") || json.startsWith("[错误]")) {
                return json;
            } else {
                return "[异常] IP查询得到了错误的JSON数据：" + e.toString();
            }
        }

        return "[信息] 网络已连接！获取到的本机IP信息：" + output.toString();
    }
}
