package romanow.abc.core.mongo;

import com.mongodb.*;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.I_Environment;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.EntityNamed;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MongoDB extends I_MongoDB {
    public MongoDB(){
        }
    private MongoClient mongo = null;
    //private MongoDatabase mongoDB=null;
    private DB mongoDB=null;
    public DB dbase(){ return mongoDB; }
    private boolean testDB(int port){
        try {
            List<String> ss = mongo.getDatabaseNames();
            for (String zz : ss)
                if (zz.equals(ValuesBase.env().applicationName(ValuesBase.AppNameDBName)+port))
                    return true;
            return false;
            } catch (Exception ee){ System.out.println(ee); return false; }
        }

    @Override
    public String getDriverName() {
        return "MongoDB 3.0";
        }

    @Override
    public boolean openDB(int port) throws UniException {
        clearCash();
        try {
            boolean auth=false;
            connect();
            if (testDB(port)){
                //MongoDatabase mongoDB = mongo.getDatabase(ValuesBase.mongoDBName+port);
                I_Environment env = ValuesBase.env();
                mongoDB = mongo.getDB(env.applicationName(ValuesBase.AppNameDBName)+port);
                mongoDB.addUser(env.applicationName(ValuesBase.AppNameDBUser), env.applicationName(ValuesBase.AppNameDBPass).toCharArray());
                }
            else{
                return false;
                }
            } catch (Exception ee){ System.out.println(ee); return false; }
        return isOpen();
        }
    /*
    public boolean openDB(int port) throws UniException {
        try {
            boolean auth=false;
            connect();
            if (testDB()){
                mongoDB = mongo.getDB(ValuesBase.env().mongoDBName+port);
                mongoDB.addUser(ValuesBase.env().mongoDBUser,ValuesBase.env().mongoDBPassword.toCharArray());
                }
            else{
                mongoDB = mongo.getDB(ValuesBase.env().mongoDBName+port);
                auth = mongoDB.authenticate(ValuesBase.env().mongoDBUser,ValuesBase.env().mongoDBPassword.toCharArray());
                }
            } catch (Exception ee){ System.out.println(ee); return false; }
        return isOpen();
        }
    */
    private void connect() throws UniException {
        try {
            mongo = new MongoClient(ValuesBase.mongoServerIP, ValuesBase.mongoServerPort);
            } catch (Exception e) {
                throw UniException.sql(e);
                }
        }
    @Override
    public boolean isOpen(){ return  mongo!=null && mongoDB!=null; }
    @Override
    public void closeDB(){
        clearCash();
        if (!isOpen()) return;
        mongo.close();
        mongoDB=null;
        mongo=null;
        }
    public void showTables(){
        Set<String> tables = mongoDB.getCollectionNames();
        for(String coll : tables){
            System.out.println(coll);
            }
        }
    @Override
    public void createIndex(Entity entity, String name) throws UniException {
        DBCollection coll = table(entity);
        coll.createIndex(name);
        }
    private DBCollection table(Entity ent) throws UniException {
        if (!isOpen())
            throw UniException.sql("No DB-connection");
        return mongoDB.getCollection(ent.getClass().getSimpleName());
        }
    @Override
    public int getCountByQuery(Entity ent, BasicDBObject query) throws UniException{
        DBCollection table = table(ent);
        DBCursor cursor = table.find(query);
        return cursor.count();
        }
    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level, String pathsList,RequestStatistic statistic) throws UniException{
        //System.out.println(query.toString());
        DBCollection table = table(ent);
        HashMap path = pathsList.length()!=0 ? parsePaths(pathsList) : null;
        DBCursor cursor = table.find(query);
        EntityList<Entity> out = new EntityList<>();
        while(cursor.hasNext()) {
            if (statistic!=null)
                statistic.entityCount++;
            DBObject obj = cursor.next();
            Entity xx = null;
            try {
                xx = (Entity) ent.getClass().newInstance();
                } catch (Exception e) {
                    throw UniException.bug("Illegal class " + ent.getClass().getSimpleName());
                    }
            //xx.setOid(((Long)obj.get("oid")).longValue());  // Читается в getData
            xx.getData("", new DocumentWrap(obj), level, this,path,statistic);
            out.add(xx);
            }
        return out;
        }
    //----------------------------------------------------------------------------------------
    @Override
    public boolean delete(Entity entity, long id, boolean mode) throws UniException{
        if (!getById(entity,id,0,mode,null,null))
            return false;
        entity.setValid(mode);
        update(entity,0);
        return true;
        }
    @Override
    synchronized public boolean getByIdAndUpdate(Entity ent, long id, I_ChangeRecord todo) throws UniException{
        boolean bb = getById(ent,id,0,null);
        if (!bb) return false;
        bb = todo.changeRecord(ent);
        if (!bb) return false;
        update(ent,0);
        return true;
        }
    @Override
    public boolean getById(Entity ent, long id, int level, boolean mode, HashMap<String,String> path,RequestStatistic statistic) throws UniException{
        if (isCashOn()){
            Entity src = getCashedEntity(ent,id);
            if (src!=null) {
                //System.out.println(ent.getClass().getSimpleName()+" "+id);
                ent.loadDBValues(src,level,this);
                incCashCount(true);
                return ent.isValid()!=mode;
                }
            incCashCount(false);
            }
        DBCollection table = table(ent);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", id);
        DBObject result = table.findOne(query);
        if (result==null)
            return false;
        ent.getData("", new DocumentWrap(result),level,this, path,statistic);
        updateCashedEntity(ent);
        if (statistic!=null)
            statistic.entityCount++;
        return ent.isValid()!=mode;
        }
    @Override
    public synchronized long nextOid(Entity ent,boolean fromEntity) throws UniException {
        DBCollection table = table(ent);
        DBObject result = table.findOne();
        if (result==null){
            throw UniException.bug("Ошибка генерации ключа в "+ent.getClass().getSimpleName());
            }
        long oid = ((Long) result.get("oid")).longValue();
        result.put("oid",fromEntity ? ent.getOid()+1 : oid+1);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", oid);
        table.update(query,result);
        return oid;
        //return table(ent).count()+1;
        }

    @Override
    public synchronized long lastOid(Entity ent) throws UniException {
        DBCollection table = table(ent);
        DBObject result = table.findOne();
        if (result==null){
            throw UniException.bug("Ошибка генерации ключа в "+ent.getClass().getSimpleName());
            }
        long oid = ((Long) result.get("oid")).longValue();
        return oid;
        }
    @Override
    public void remove(Entity entity, long id) throws UniException {
        DBCollection table = table(entity);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", id);
        table.remove(query);
        removeCashedEntity(entity);
        }

    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query, int level, String pathList, RequestStatistic statistic) throws UniException {
        return getAllByQuery(ent,query.getQuery(),level,pathList,statistic);
        }
    @Override
    public int getCountByQuery(Entity ent, I_DBQuery query) throws UniException {
        return getCountByQuery(ent,query.getQuery());
        }

    @Override
    public void dropTable(Entity ent) throws UniException {
        clearCash(ent);
        table(ent).drop();
        }

    @Override
    public long add(Entity ent, int level,boolean ownOid) throws UniException {
        long id;
        if (!ownOid){
            id = nextOid(ent);
            ent.setOid(id);
            }
        else
            id = ent.getOid();
        BasicDBObject document = new BasicDBObject();
        //document.put("oid", id);
        //document.put("valid",true); // Пишется в putData
        ent.putData("", new DocumentWrap(document),level,this);
        table(ent).insert(document);
        return id;
        }

    @Override
    public void update(Entity ent, int level) throws UniException {
        DBCollection table = table(ent);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", ent.getOid());
        BasicDBObject document = new BasicDBObject();
        document.put("oid", ent.getOid());
        document.put("valid", ent.isValid());
        ent.putData("", new DocumentWrap(document),level,this);
        table.update(query,document);
        updateCashedEntity(ent);
        }
    @Override
    public synchronized boolean updateField(Entity ent, long id, String fname, String prefix) throws UniException {
        removeCashedEntity(ent,id);
        DBCollection table = table(ent);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", id);
        DBObject result = table.findOne(query);
        if (result==null)
            return false;
        ent.putFieldValue(prefix, new DocumentWrap((BasicDBObject) result),0,this,fname);
        table.update(query,result);
        return true;
        }
    public EntityList<Entity> getAllRecords(Entity ent, int level, String pathsList, RequestStatistic statistic) throws UniException{
        HashMap path = pathsList.length()!=0 ? parsePaths(pathsList) : null;
        DBCollection table = table(ent);
        DBCursor cursor = table.find();
        EntityList<Entity> out = new EntityList<>();
        while(cursor.hasNext()) {
            if (statistic!=null)
                statistic.entityCount++;
            DBObject obj = cursor.next();
            Entity xx = null;
            try {
                xx = (Entity) ent.getClass().newInstance();
                } catch (Exception e) {
                    throw UniException.bug("Illegal class " + ent.getClass().getSimpleName());
                    }
            xx.getData("", new DocumentWrap(obj), level, this, path,statistic);
            out.add(xx);
            }
        return out;
        }
    @Override
    public EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException {
        EntityList out = new EntityList();
        DBCollection table = table(ent);
        Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        List<BasicDBObject> zz = new ArrayList<BasicDBObject>();
        zz.add(new BasicDBObject("valid", true));
        Document regexQuery = new Document();
        System.out.println(Pattern.quote(pattern));
        regexQuery.append("$regex", ".*"+Pattern.quote(pattern) + ".*");
        BasicDBObject criteria = new BasicDBObject("name", regexQuery);
        zz.add(criteria);
        BasicDBObject query = new BasicDBObject();
        query.put("$and", zz);
        DBCursor cursor = table.find(query);
        if (cursor.count()> ValuesBase.PopupListMaxSize)
            return out;
        while(cursor.hasNext()) {
            DBObject obj = cursor.next();
            EntityNamed xx = null;
            xx = new EntityNamed();
            xx.setOid(((Long) obj.get("oid")).longValue());
            xx.setName((String) obj.get("name"));
            out.add(xx);
            }
        return out;
        }
    @Override
    public String clearDB() {
        clearCash();
        Object olist[] = ValuesBase.EntityFactory.classList().toArray();
        String out="";
        TableItem item=null;
        for(int i=0;i<olist.length;i++){
            try {
                item = (TableItem)olist[i];
                if (!item.isTable)
                    continue;
                Entity ent = (Entity)(item.clazz.newInstance());
                dropTable(ent);
                ent.setOid(1);
                ent.setValid(false);
                add(ent,0,true);
                createIndex(ent,"oid");
                createIndex(ent,"valid");
                for(String ss : item.indexes)
                    createIndex(ent,ss);
                } catch (Exception ee){
                    String ss = "Не могу создать "+ item.clazz.getSimpleName()+"\n"+ee.toString()+"\n";
                    System.out.print(ss);
                    out+=ss;
                }
            }
    //    try {
    //        add(ValuesBase.superUser,0,false);
    //        } catch (UniException e) {
    //            String ss = "Не могу создать суперадмина \n"+e.toString()+"\n";
    //            System.out.print(ss);
    //            out+=ss;
    //            }
        return out;
        }
    @Override
    public String clearTable(String table) throws UniException {
            try {
                TableItem item = ValuesBase.EntityFactory.getItemForSimpleName(table);
                if (item==null)
                    return "Entity не найден: "+table;
                if (!item.isTable)
                    return "Not table: "+table;
                Entity ent = (Entity)(item.clazz.newInstance());
                clearCash(ent);
                dropTable(ent);
                ent.setOid(1);
                ent.setValid(false);
                add(ent,0,true);
                createIndex(ent,"oid");
                createIndex(ent,"valid");
                for(String ss : item.indexes)
                    createIndex(ent,ss);
            } catch (Exception ee){
                String ss = "Не могу создать "+ ValuesBase.EntityFactory.get(table+"\n"+ee.toString());
                System.out.println(ss);
                return ss;
            }
        return "";
        }
    //--------------------------------------------------------------------------------------------------------------
    public static void main(String ss[]){
        MongoDB db = new MongoDB();
        try {
            db.openDB(4567);
            db.showTables();
            /*
            EntityList<EntityNamed> zz = db.getListForPattern(new Facility(),"кор");
            for(EntityNamed ee : zz)
                System.out.println(ee.getOid()+" "+ee.getName());
            User us = new User();
            Technician tc = new Technician();
            db.dropTable(us);
            db.dropTable(tc);
            db.dropTable(new Shift());
            db.dropTable(new Artifact());
            long id1 = db.add(new User(ValuesBase.UserBossType,"Турков","Александр","Иванович","boss","1234",""));
            long id2 = db.add(new User(0,"Иванов","Иван","Иванович","","1234","9139999999"));
            long id3 = db.add(new User(ValuesBase.UserBookKeeperType,"Юдина","Наталья","Семеновна","","1234","89131234567"));
            System.out.println(db.getAll(us));
            db.deleteById(new User(),id2,false);
            System.out.println(db.getAll(us));
            System.out.println(db.getAll(tc));
            db.showTables();
            */
            db.closeDB();
            } catch (UniException e) { System.out.println(e.toString()); }
        }
}