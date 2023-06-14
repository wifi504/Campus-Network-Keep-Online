import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time: 2023/06/13_21:11
 */

// 网络请求类
public class Network {
    // 成员变量，已在 Main 中声明值
    private String ipLookupServer;
    private String host;
    private String authUrl;
    private String payload;

    public String getMyIP() { // 从服务器查询本机IP地址
        HttpURLConnection connection = null;
        try {
            // IP查询服务器
            URL url = new URL(ipLookupServer);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String ipAddress = reader.readLine();
                reader.close();
                return ipAddress;
            } else {
                return "[错误] 无法从服务器获取本机IP地址，请检查IP-API服务是否可用...";
            }
        } catch (IOException e) {
            return "[异常] 本机IP查询异常：" + e.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // ping 指定主机，判断网络通断
    public boolean pingTest() {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            return inetAddress.isReachable(3000);
        } catch (IOException e) {
            return false;
        }
    }

    // 发送 POST 请求以实现校园网认证
    public String postAuthPayload() {
        try {
            URL requestUrl = new URL(authUrl);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(payload.getBytes("UTF-8"));
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return "[信息] 发送认证请求成功，Response: " + response.toString();
            } else {
                return "[错误] 发送认证请求失败，Response code: " + responseCode;
            }
        } catch (Exception e) {
            return "[异常] 发送认证请求时出现网络错误：" + e.toString();
        }
    }

    // 以下，Setters & Getters

    public String getIpLookupServer() {
        return ipLookupServer;
    }

    public void setIpLookupServer(String ipLookupServer) {
        this.ipLookupServer = ipLookupServer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
