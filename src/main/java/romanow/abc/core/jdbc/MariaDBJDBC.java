package romanow.abc.core.jdbc;

import romanow.abc.core.ParamList;
import romanow.abc.core.UniException;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MariaDBJDBC extends MySQLJDBC{
    @Override
    public String getDriverName() {
        return "MariaDB";
    }

    //---------------------------------------------------------------------------
    private int loginTimeOut=15;                // Тайм-аут логина (библиотечный)
    private int queryTimeOut=15;                // Тайм-аут логина (библиотечный)

    private Properties setProp(){
        Properties properties=new Properties();
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");
        properties.setProperty("encrypt","false");
        properties.setProperty("trustServerCertificate","true");
        properties.setProperty("integratedSecurity","true");
        properties.setProperty("loginTimeout",""+loginTimeOut);
        properties.setProperty("elideSetAutoCommits","false");
        return properties;
        }
    @Override
    public synchronized boolean isConnected(){
        return dbConn!=null;
    }
    @Override
    public void connect(ParamList paramList0) throws UniException {
        paramList = paramList0;
        connect(paramList.getParam("DBName")+".s3db");
        }
    @Override
    public void connect(String file) throws UniException {
        dbConn=null;
        state=0;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            } catch(ClassNotFoundException ee){ throw UniException.sql("Нет драйвера БД"); }
        try {
            String connectionUrl = "jdbc:mariadb://"+paramList.getParam("DBIP")+":"+paramList.getParam("DBPort2")+"/"+paramList.getParam("DBName");
            System.out.println(connectionUrl);
            dbConn = DriverManager.getConnection(connectionUrl,paramList.getParam("DBUser"),paramList.getParam("DBPass"));
            if (dbConn == null) throw UniException.sql("Нет соединения с БД(1)");
            DatabaseMetaData dm = (DatabaseMetaData) dbConn.getMetaData();
            System.out.println("Driver name: " + dm.getDriverName());
            System.out.println("Driver version: " + dm.getDriverVersion());
            System.out.println("Product name: " + dm.getDatabaseProductName());
            System.out.println("Product version: " + dm.getDatabaseProductVersion());
            stm = dbConn.createStatement();
            stm.setQueryTimeout(queryTimeOut);
            state=1;
            } catch (SQLException ee) {
                throw UniException.sql("Нет соединения с БД(2):"+ee.toString());
                }
        }
    @Override
    public String getText1() {
        return "INT NOT NULL AUTO_INCREMENT";
        }
    @Override
    public String getText2() {
        return ",PRIMARY KEY (oid)";
        }
    @Override
    public boolean canGenerateKey() {
        return true;
        }
    }


