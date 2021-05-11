package romanow.abc.core.jdbc;

import romanow.abc.core.ParamList;
import romanow.abc.core.UniException;

import java.sql.*;
import java.util.Properties;
//--------------- Параметры соединения
//  DBUser - пользователь БД для коннекта
//  DBPass - пароль БД для коннекта
//  DBProxyOn
//  DBProxyIP
//  DBProxyPort
//  DBIP
//  DBPort
//  DBName
public class MySQLJDBC implements I_JDBCConnector {
    public final static boolean TestMode=false;
    @Override
    public void connect(String file) throws UniException {
        throw UniException.noFunc();
        }
    public long newRecord(String tname) throws UniException {
        testConnect();
        long id=1;
        try {
            dbConn.setAutoCommit (false);
            ResultSet ss = selectOne("SELECT MAX(oid) FROM "+tname+";");
            if (ss!=null)
                id=ss.getLong("oid")+1;
            String sql="INSERT INTO "+tname+" (oid)  VALUES ("+id+");";
            execSQL(sql);
            dbConn.commit();
            dbConn.setAutoCommit(true);
            } catch (Exception ee){
                try {
                    dbConn.rollback ();
                    dbConn.setAutoCommit(true);
                    } catch (SQLException e2){
                        procException(null,e2);
                        }
                    }
        return id;
        }

    class AliveThread extends Thread{
        AliveThread(){ start(); }
        public void run(){
            while(true){
                try { sleep(connectLiveTime*1000); } catch (Exception ex){}
                synchronized(MySQLJDBC.this){
                    //--------- Поток другой или прерван или соединение закрыто
                    if(state==0 || this!=aliveThread || this.isInterrupted()) {
                        state=0; return;
                    }
                    if (state==1) {
                        System.out.println("Отсчет тайм-аута");
                        state=2;
                    }
                    else
                    if (state==2){
                        try { dbConn.close();} catch (Exception ex) {}
                        dbConn=null;
                        System.out.println("Заснул");
                        state=3;
                    }
                }
            }
        }
    }
    //---------------------------------------------------------------------------
    private static int loginTimeOut=30;        // Тайм-аут логина (библиотечный)
    private static int queryTimeOut=30;        // Тайм-аут логина (библиотечный)
    private static int connectLiveTime=240;	// Время "жизни" соединения
    protected int state=0;                // 0 - отключено, 1-соединение, 2-тайм-аут паузы 3- спит
    protected Thread aliveThread=null;    // Поток keepAlive
    private String dbName="";
    public String dbName(){ return dbName; }
    protected Connection dbConn=null;
    protected Statement stm=null;
    protected ParamList paramList;

    private Properties setProp(ParamList pars) throws UniException{
        Properties properties=new Properties();
        properties.setProperty("user",pars.getParam("DBUser"));
        properties.setProperty("password",pars.getParam("DBPass"));
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");
        properties.setProperty("encrypt","false");
        properties.setProperty("trustServerCertificate","true");
        properties.setProperty("integratedSecurity","true");
        properties.setProperty("loginTimeout",""+loginTimeOut);
        properties.setProperty("elideSetAutoCommits","false");
        if (pars.testParam("DBProxyOn") && pars.getParamBoolean("DBProxyOn")){
            properties.put("proxy_host", pars.getParam("DBProxyIP"));
            properties.put("proxy_port", ""+pars.getParam("DBProxyPort"));
            properties.put("proxy_user", "[proxy user]");
            properties.put("proxy_password", "[proxy password]");
            }
        return properties;
        }

    public MySQLJDBC(){ dbConn=null; state=0;}

    @Override
    public String getDriverName() {
        return "MySQL";
        }

    public synchronized boolean isConnected(){
        boolean bb =  (state!=0);
        return bb;
        }
    public void reconnect() throws UniException{
        if (paramList==null)
            throw UniException.sql("Повторное соединение без регистрации");
        close();
        connect();
        }

    public void connect(ParamList paramList0)throws UniException{
        paramList = paramList0;
        aliveThread=new AliveThread();
        connect();
        System.out.println("Присоединился");
        }
    public void connect() throws UniException{
        dbConn=null;
        state=0;
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            } catch(ClassNotFoundException ee){ throw UniException.sql("Нет драйвера БД com.mysql.jdbc.Driver"); }
        try {
            String connectionUrl = "jdbc:mysql://"+paramList.getParam("DBIP")+":"+paramList.getParam("DBPort")+"/"+paramList.getParam("DBName");
            DriverManager.setLoginTimeout(loginTimeOut);
            if (TestMode)
                System.out.println(connectionUrl);
            dbConn = DriverManager.getConnection(connectionUrl,setProp(paramList));
            if (dbConn == null) throw UniException.sql("Нет соединения с БД");
            stm=dbConn.createStatement();
            stm.setQueryTimeout(queryTimeOut);
            state=1;
            } catch (SQLException ee){
                procException(null,ee);
                }
        }
    //---- Вызывается только из синхронизированного кода ---------------------------
    public void testConnect() throws UniException {
        switch (state){
            case 0:
                throw UniException.sql("Нет соединения с БД");
            case 2:
                System.out.println("Новый тайм-аут "+connectLiveTime+" сек.");
                state=1;
                break;
            case 3:
                connect();
                System.out.println("Проснулся");
                break;
        }}

    public synchronized void open() throws UniException{
        testConnect();
        }

    public synchronized void close() throws UniException{
        //----------------- ЗАСАДА С СОСТОЯНИЯМИ В Android----------------------
        try {
            if (state==1 || state==2){
                stm.close();
                dbConn.close();
                dbConn=null;
                }
            if (state!=0 && aliveThread!=null){
                aliveThread.interrupt();
                aliveThread=null;
                }
            System.out.println("Отсоединился");
            state=0;
            } catch (SQLException ee){ throw UniException.sql(ee); }
        }

    public synchronized void execSQL(String sql) throws UniException{
        try{
            testConnect();
            if(TestMode)
                System.out.println(sql);
            stm.execute(sql);
            } catch (SQLException ee){ throw UniException.sql(ee); }
        }
    public synchronized long insert(String sql, boolean ownId) throws UniException{
        try {
            testConnect();
            stm.executeUpdate(sql,!ownId ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
            if (ownId)
                return 0;
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next())
                return rs.getLong("oid");
            else
                throw UniException.sql("Не получен id записи");
                } catch (Exception ee){
                    System.out.println("Insert:"+sql);
                    throw UniException.sql(ee);
                    }
            }
    public synchronized ResultSet selectOne(String sql) throws UniException{
        testConnect();
        if (TestMode)
            System.out.println(sql);
        ResultSet rs = null;
        try {
            rs = stm.executeQuery(sql);
            if (!rs.next()) { rs.close(); return null; }
            return rs;
            } catch (SQLException e){
                procException(rs, e);
                return null;
                }
            }
    protected void procException(ResultSet rs, SQLException ee) throws UniException{
        if (rs != null){
            try {
                rs.close();
                } catch (SQLException e2){
                    throw UniException.sql(ee.toString()+"\n"+e2.toString());
                    }
                }
            throw UniException.sql(ee);
        }
    public synchronized void selectOne(String sql, I_OnRecord back) throws UniException{
        testConnect();
        if (TestMode)
            System.out.println(sql);
        ResultSet rs = null;
        try {
            rs = stm.executeQuery(sql);
            if (!rs.next()) { rs.close(); return; }
            back.onRecord(rs);
            rs.close();
            } catch (SQLException e){
                procException(rs,e);
                }
        }
    public synchronized void selectMany(String sql, I_OnRecord back)throws UniException{
        testConnect();
        if (TestMode)
            System.out.println(sql);
        ResultSet rs = null;
        try {
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                back.onRecord(rs);
                }
            rs.close();
        }
        catch (SQLException e){
            procException(rs,e);
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
