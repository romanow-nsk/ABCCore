package romanow.abc.core.entity;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import romanow.abc.core.I_ExcelRW;
import romanow.abc.core.I_XStream;
import romanow.abc.core.UniException;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.I_MongoRW;
import romanow.abc.core.mongo.RequestStatistic;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity extends DAO implements I_XStream, I_Name, I_MongoRW, I_ExcelRW {
    private long oid=0;
    private boolean valid=true;
    public String toFullString(){ return "["+oid+"] "; }
    public String toString(){ return "["+oid+"] "; }
    public String toShortString(){ return ""; }
    public String getTitle(){ return toString(); }
    public long getOid() {
        return oid;
        }
    public void setOid(long oid) {
        this.oid = oid;
        }
    public Entity(long id0){ oid=id0; }
    public Entity(){ oid=0; }
    public int getTitleLevel(){ return 0; }
    public String toJSON(){
        Gson gs = new Gson();
        return gs.toJson(this);
        }
    public boolean isValid(){ return valid; }
    public void setValid(boolean bb){ valid=bb; }
    //----------------- Операции с БД ----------------------------------------------------------------------------------
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo, HashMap<String,String> paths, RequestStatistic statistic) throws UniException {
        getDBValues(prefix,res,level,mongo,paths,statistic);
        }
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo,RequestStatistic statistic) throws UniException {
        getDBValues(prefix,res,level,mongo,null,statistic);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, String prefix, HashMap<String,Integer> colMap) throws UniException{
        getXMLValues(row, prefix,colMap);
        }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        }
    //-------------------------------------------------------------------------------------------------------------
    @Override
    public void setAliases(XStream xs){
        xs.alias("Entity", Entity.class);
        xs.useAttributeFor(Entity.class, "oid");
        }
    public String objectName() {            // ПОЛИМОРФНЫЙ ВЫЗОВ ДЛЯ ШАБЛОНА
        return getName(); }
    public String getName() {
        return ""; }
    //------------------------------------------------------------------------------------------------- Для мапов ------
    public long getKeyNum(){ return oid; }
}
