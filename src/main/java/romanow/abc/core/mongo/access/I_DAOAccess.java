package romanow.abc.core.mongo.access;

import org.apache.poi.ss.usermodel.Row;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.export.ExCellCounter;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoDB;
import romanow.abc.core.mongo.RequestStatistic;

import java.util.HashMap;

public interface I_DAOAccess {
    public void getField(EntityField ff, DAO dao) throws Exception;
    public void getDBValue(EntityField ff, DAO dao, String prefix, org.bson.Document out, int level, I_MongoDB mongo, HashMap<String,String> path, RequestStatistic statistic) throws UniException;
    public void updateField(EntityField ff, DAO dao, String value) throws Exception;
    public void getXMLValues(EntityField ff, DAO dao,Row row, int cnt,HashMap<String,Integer> colMap) throws Exception;
    public void putXMLValues(EntityField ff, DAO dao,Row row, ExCellCounter cnt) throws Exception;
    //----------------- При клонировании из кэша
    public void loadDBValues(EntityField ff, DAO dao, DAO src, int level, I_MongoDB mongo) throws Exception;
    //----------------- Просто клон без ссылок
    public void copyDBValues(EntityField ff, DAO dao, DAO src) throws Exception;
    public void putFieldValue(DAO dao,String prefix, org.bson.Document out, int level, I_MongoDB mongo,EntityField ff) throws Exception;
    public String createKotlinFieldDefine(EntityField ff);
    }