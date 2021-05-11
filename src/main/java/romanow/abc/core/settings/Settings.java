package romanow.abc.core.settings;

import romanow.abc.core.constants.ValuesBase;

public class Settings {
    private String dataServerIP = ValuesBase.dataServerIP;
    private int dataServerPort = ValuesBase.dataServerPort;
    private String dataServerFileDir = ValuesBase.dataServerFileDir;
    private String webServerWebLocation = ValuesBase.webServerWebLocation;
    private String mongoStartCmd = ValuesBase.mongoStartCmd;
    private String ClientVersion= ValuesBase.ClientVersion;
    private String ServerVersion= ValuesBase.ServerVersion;
    //-----------------------------------------------------------------------------------------
    public String clientVersion() { return ClientVersion; }
    public void clientVersion(String clientVersion) { ClientVersion = clientVersion; }
    public String getServerVersion() { return ServerVersion; }
    public void setServerVersion(String serverVersion) { ServerVersion = serverVersion; }
    public String mongoStartCmd() { return mongoStartCmd; }
    public void mongoStartCmd(String mongoStartCmd) { this.mongoStartCmd = mongoStartCmd; }
    public String dataServerIP() {
        return dataServerIP;
        }
    public void dataServerIP(String dataServerIP) {
        this.dataServerIP = dataServerIP;
        }
    public int dataServerPort() {
        return dataServerPort;
        }
    public void dataServerPort(int dataServerPort) {
        this.dataServerPort = dataServerPort;
        }
    public String dataServerFileDir() {
        return dataServerFileDir;
        }
    public void dataServerFileDir(String dataServerFileDir) {
        this.dataServerFileDir = dataServerFileDir;
        }
    public String webServerWebLocation() {
        return webServerWebLocation;
        }
    public void webServerWebLocation(String webServerWebLocation) {
        this.webServerWebLocation = webServerWebLocation;
        }
}
