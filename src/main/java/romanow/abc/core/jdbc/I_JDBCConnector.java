package romanow.abc.core.jdbc;

import romanow.abc.core.ParamList;
import romanow.abc.core.UniException;

import java.sql.ResultSet;

public interface I_JDBCConnector {
    public String getDriverName();
    public boolean isConnected();
    public void connect(ParamList params)throws UniException;
    public void reconnect()throws UniException;
    public void connect(String file)throws UniException;
    //---- Вызывается только из синхронизированного кода ---------------------------
    public void testConnect() throws UniException;
    public void open() throws UniException;
    public void close() throws UniException;
    public void execSQL(String sql) throws UniException;
    public long insert(String sql, boolean ownId) throws UniException;
    public ResultSet selectOne(String sql) throws UniException;
    public void selectOne(String sql,I_OnRecord back) throws UniException;
    public void selectMany(String sql,I_OnRecord back)throws UniException;
    public long newRecord(String tname)  throws UniException;
    public String getText1();
    public String getText2();
    public boolean canGenerateKey();
}
