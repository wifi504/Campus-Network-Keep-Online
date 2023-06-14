/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time: 2023/06/13_21:09
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ---------------------参数配置区----------------------- //

        // IP查询服务器（一般不用改，查询本机IP地址信息的服务器）
        String ipLookupServer = "https://ip.useragentinfo.com/json";
        // IP查询键名筛选（一般不用改，显示本机IP地址的哪些信息）
        String[] selectedKeys = {"ip", "country", "province", "city", "area", "isp"};
        // 网络通断测试的被 ping 主机
        String host = "114.114.114.114";
        // 重连失败重试次数，-1 为无限次
        int maxRetry = 3;

        // 校园网认证地址（认证API接口地址，F12查看）
        String authUrl = "";
        // 认证请求体（POST请求的载荷，F12查看）
        String payload = "";

        // ---------------------------------------------------- //

        // 初始化实例
        Network network = new Network();
        Tools tools = new Tools();

        // 初始化参数
        if (args.length == 3) {
            maxRetry = Integer.parseInt(args[0]);
            authUrl = args[1];
            payload = args[2];
        }
        network.setIpLookupServer(ipLookupServer);
        network.setHost(host);
        network.setAuthUrl(authUrl);
        network.setPayload(payload);

        System.out.println(
                "  _  __                    ___          _  _              \n" +
                        " | |/ / ___   ___  _ __   / _ \\  _ __  | |(_) _ __    ___ \n" +
                        " | ' / / _ \\ / _ \\| '_ \\ | | | || '_ \\ | || || '_ \\  / _ \\\n" +
                        " | . \\|  __/|  __/| |_) || |_| || | | || || || | | ||  __/\n" +
                        " |_|\\_\\\\___| \\___|| .__/  \\___/ |_| |_||_||_||_| |_| \\___|\n" +
                        "                  |_|     F**k Campus Network!  \n" +
                        " Powered By WIFI连接超时\n https://github.com/wifi504/Campus-Network-Keep-Online"
        );
        System.out.println("------------------------------------------------------");
        System.out.println(tools.getCurrentTime() + "[信息] 开始校园网自动断线重连");

        // 程序主体，算法见流程图 flowsheet.png
        while (true) {
            if (network.pingTest()) {
                String ipInfo = tools.jsonToLine(network.getMyIP(), selectedKeys);
                System.out.println(tools.getCurrentTime() + ipInfo);
                if (ipInfo.startsWith("[信息]")) {
                    System.out.println(tools.getCurrentTime() + "[信息] 开始持续 ping " + host + " 监测网络情况...");
                    while (network.pingTest()) {
                        Thread.sleep(1000);
                    }
                }
            }
            System.out.println(tools.getCurrentTime() + "[错误] 网络已断开，开始尝试重新认证...  (╬▔皿▔)╯ 屮！");
            System.out.println("------------------------------------------------------");
            int retryCount = 1;
            while (maxRetry == -1 || retryCount <= maxRetry) {
                System.out.println(tools.getCurrentTime() + network.postAuthPayload());
                Thread.sleep(3000);
                if (tools.jsonToLine(network.getMyIP(), selectedKeys).startsWith("[信息]")) {
                    System.out.println(tools.getCurrentTime() + "[信息] 认证成功！");
                    break;
                }
                if (maxRetry != -1) {
                    System.out.println(tools.getCurrentTime() +
                            "[错误] 认证失败，重试次数 " + retryCount + " ，最大 " + maxRetry + " ！");
                    if (retryCount == maxRetry) return;
                    ++retryCount;
                } else {
                    System.out.println(tools.getCurrentTime() + "[错误] 认证失败，重试次数 NaN ，最大 Infinity ！");
                }
            }
        }
    }
}
