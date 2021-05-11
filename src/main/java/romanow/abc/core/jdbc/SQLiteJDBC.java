package romanow.abc.core.jdbc;

import romanow.abc.core.ParamList;
import romanow.abc.core.UniException;

import java.sql.*;
import java.util.Properties;

public class SQLiteJDBC  extends MySQLJDBC{
    @Override
    public String getDriverName() {
        return "SQLite";
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
            Class.forName("org.sqlite.JDBC");
            } catch(ClassNotFoundException ee){ throw UniException.sql("Нет драйвера БД"); }
        try {
            dbConn = DriverManager.getConnection("jdbc:sqlite:"+file,setProp());
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
        return "INTEGER PRIMARY KEY AUTOINCREMENT";
        }
    @Override
    public String getText2() {
        return "";
        }
    public synchronized long insert(String sql, boolean ownId) throws UniException{
        try {
            testConnect();
            stm.executeQuery(sql);
            return 0;
            } catch (SQLException ee){ throw UniException.sql(ee); }
        }
    @Override
    public boolean canGenerateKey() {
        return false;
        }
    }


