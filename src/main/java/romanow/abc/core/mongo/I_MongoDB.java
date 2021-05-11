package romanow.abc.core.mongo;

import com.mongodb.BasicDBObject;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.EntityNamed;

import java.util.HashMap;
import java.util.StringTokenizer;

public abstract class I_MongoDB {
    public abstract String getDriverName();
    public abstract boolean openDB(int port) throws UniException;
    public abstract boolean isOpen();
    public abstract void closeDB();
    public abstract String clearDB() throws UniException;
    public abstract String clearTable(String table) throws UniException;
    public abstract void createIndex(Entity entity, String name) throws UniException;
    public abstract int getCountByQuery(Entity ent, BasicDBObject query) throws UniException;
    public abstract EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level,String pathList,RequestStatistic statistic) throws UniException;
    public abstract boolean delete(Entity entity, long id, boolean mode) throws UniException;
    public abstract boolean getByIdAndUpdate(Entity ent, long id, I_ChangeRecord todo) throws UniException;
    public abstract boolean getById(Entity ent, long id, int level, boolean mode, HashMap<String,String> path, RequestStatistic statistic) throws UniException;
    public abstract void dropTable(Entity ent) throws UniException;
    public abstract long add(Entity ent, int level,boolean ownOid) throws UniException;
    public abstract void update(Entity ent, int level) throws UniException;
    public abstract EntityList<Entity> getAllRecords(Entity ent, int level, String pathList,RequestStatistic statistic) throws UniException;
    public abstract EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException;
    public abstract long nextOid(Entity ent,boolean fromEntity) throws UniException;
    public abstract long lastOid(Entity ent) throws UniException;
    public abstract void remove(Entity entity, long id) throws UniException;
    //----------------------- Альтернативное query ---------------------------------------------------------
    public abstract EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query, int level,String pathList,RequestStatistic statistic) throws UniException;
    public abstract int getCountByQuery(Entity ent, I_DBQuery query) throws UniException;
    public EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query) throws UniException{
        return getAllByQuery(ent,query,0,"",null);
        }
    public EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query, int level) throws UniException{
        return getAllByQuery(ent,query,level,"",null);
        }
    public boolean isSQLDataBase(){
        return false;
        }
    public void execSQL(String sql) throws UniException{
        throw UniException.noFunc("SQL-запросы не поддерживаются");
        }
    //------------------------ Синхронизированное обновление поля ПО ВСЕЙ БД 628
    public abstract boolean updateField(Entity src, long id, String fname, String prefix) throws UniException;
    public boolean updateField(Entity src, String fname, String prefix) throws UniException{
        return updateField(src,src.getOid(),fname,prefix);
        }
    public boolean updateField(Entity src, String fname) throws UniException{
        return updateField(src,src.getOid(),fname,"");
    }
    //------------------------ Умолчания ----------------------------------
    //public boolean getById(Entity ent, long id, int level, boolean mode,  String pathList) throws UniException{
    //    return getById(ent,id,level,mode,pathList,null);
    //    }
    public void remove(Entity entity) throws UniException{
        remove(entity,entity.getOid());
        }
    public synchronized long nextOid(Entity ent) throws UniException{
        return nextOid(ent,false);
        }
    public EntityList<Entity> getAll(Entity ent, int mode, int level) throws UniException{
        return getAll(ent,mode,level,"",null);
        }
    public EntityList<Entity> getAll(Entity ent, int mode, int level,String pathList,RequestStatistic statistic) throws UniException{
        switch (mode){
            case ValuesBase.GetAllModeTotal:
                return getAllRecords(ent,level,pathList,statistic);
            case ValuesBase.GetAllModeActual:
                return getAllByQuery(ent,new DBQueryBoolean("valid", true),level,pathList,statistic);
            case ValuesBase.GetAllModeDeleted:
                return getAllByQuery(ent,new DBQueryBoolean("valid", false),level,pathList,statistic);
            default:
                throw UniException.bug("MongoDB:Illegal get mode="+mode);
            }
        }
    public long add(Entity ent, int level) throws UniException{
        return add(ent,level,false);
        }
    public long add(Entity ent) throws UniException{
        return add(ent,0);
        }
    public void update(Entity ent) throws UniException{
        update(ent,0);
        }
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query) throws UniException{
        return getAllByQuery(ent,query,0,"",null);
        }
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level) throws UniException{
        return getAllByQuery(ent,query,level,"",null);
        }
    public boolean delete(Entity entity) throws UniException{
        return delete(entity,entity.getOid(),false);
        }
    public boolean delete(Entity entity,long id) throws UniException{
        return delete(entity,id,false);
        }
    public EntityList<Entity> getAll(Entity ent) throws UniException{
        return getAll(ent, ValuesBase.GetAllModeActual,0,"",null);
        }
    public boolean getById(Entity ent, long id) throws UniException{
        return getById(ent,id,0,null);
        }
    public boolean getById(Entity ent, long id, int level) throws UniException{
        return  getById(ent, id, level, ValuesBase.DeleteMode,null,null);
        }
    //------------------------------------------------------- 660 ----------- пути БО ------------
    public boolean getById(Entity ent, long id,String pathList) throws UniException{
        return getById(ent,id,0,ValuesBase.DeleteMode,null,null);
        }
    public boolean getById(Entity ent, long id, int level,String pathList) throws UniException{
        return  getById(ent, id, level, ValuesBase.DeleteMode,null,null);
        }
    //-------------------------------------- КЭШ объектов ------------------------------------------
    private int totalGetCount=0;            // Общее количество чтений
    private int cashGetCount=0;             // Количество чтений из кэша
    private volatile boolean cashOn=false;  // Кэширование включено
    private HashMap<String,HashMap<Long,Entity>> cash=new HashMap<>();  // Двухуровневая хэш-таблица
    //-----------------------------------------------------------------------------------------------
    public int getTotalGetCount() {
        return totalGetCount; }
    public int getCashGetCount() {
        return cashGetCount; }
    public void incCashCount(boolean success){
        if (success)
            cashGetCount++;
        totalGetCount++;
        }
    public synchronized boolean isCashOn() {
        return cashOn; }
    public synchronized void setCashOn(boolean cashOn) {
        this.cashOn = cashOn;
        clearCash();
        }
    public synchronized void clearCash(){           // Полная очистка кэша
        totalGetCount=0;
        cashGetCount=0;
        cash = new HashMap<>();
        System.gc();                                // с вызовом сборщика мусора
        }
    public synchronized void clearCash(Entity ent){ // Очистка кэша класса
        cash.remove(ent.getClass().getSimpleName());
        System.gc();
    }
    public  synchronized Entity getCashedEntity(Entity proto, long id){
        if (!cashOn) return null;
        HashMap<Long,Entity> map = cash.get(proto.getClass().getSimpleName());
        if (map==null)
            return null;
        return map.get(id);                        // Извлечение из кэша
        }
    public  synchronized void removeCashedEntity(Entity proto){
        removeCashedEntity(proto,proto.getOid());
        }
    public  synchronized void removeCashedEntity(Entity proto, long oid){
        if (!cashOn) return;
        HashMap<Long,Entity> map = cash.get(proto.getClass().getSimpleName());
        if (map==null)
            return;
        map.remove(oid);                        // Удаление из кэша
        }
    public  synchronized void updateCashedEntity(Entity proto){
        if (!cashOn) return;
        String name = proto.getClass().getSimpleName();
        HashMap<Long,Entity> map = cash.get(name);
        if (map==null){
            map = new HashMap();
            cash.put(name,map);                     // Обновление в кэше
            }
        else
            map.remove(proto.getOid());
        map.put(proto.getOid(),proto);
        }
    //----------------------------------------------------------------------------------
    public static HashMap<String,String> parsePaths(String src){
        HashMap<String,String> out = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(src,",");
        int cc = tokenizer.countTokens();
        for(int i=0;i<cc;i++){
            String ss = tokenizer.nextToken();
            out.put(ss,ss);
            }
        return out;
        }
    public static void main(String ss[]){
        HashMap<String,String> xx = parsePaths("");
        System.out.println(xx);
    }
}
