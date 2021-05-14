package romanow.abc.core.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.EntityNamed;
import romanow.abc.core.entity.base.BugMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MongoDB36 extends I_MongoDB {
    private MongoClient mongo = null;
    private MongoDatabase mongoDB=null;
    public MongoDB36(){ }
    private boolean testDB(int port){
        String dbName = ValuesBase.env().applicationName(ValuesBase.AppNameDBName)+port;
        try {
            List<String> ss = mongo.getDatabaseNames();
            for (String zz : ss)
                if (zz.equals(dbName))
                    return true;
            return false;
        } catch (Exception ee){ System.out.println(ee); return false; }
    }

    @Override
    public boolean openDB(int port) throws UniException {
        clearCash();
        try {
            boolean auth=false;
            connect();
            if (testDB(port))
                mongoDB = mongo.getDatabase(ValuesBase.env().applicationName(ValuesBase.AppNameDBName)+port);
            else
                return false;
        } catch (Exception ee){ System.out.println(ee); return false; }
        return isOpen();
    }

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
        MongoIterable<String> tables = mongoDB.listCollectionNames();
        for(String coll : tables){
            System.out.println(coll);
            }
        }

    @Override
    public void createIndex(Entity entity, String name) throws UniException {
        MongoCollection coll = table(entity);
        //TODO !!!!!!!!!!!!!!!!!!!!!!!!
        //coll.createIndex(new Bson() {
        }

    private MongoCollection table(Entity ent) throws UniException {
        if (!isOpen())
            throw UniException.sql("No DB-connection");
        return mongoDB.getCollection(ent.getClass().getSimpleName());
        }
    private Document findOne(MongoCollection table, BasicDBObject query){
        MongoCursor<Document> cursor = table.find(query).iterator();
        return cursor.hasNext() ? cursor.next() : null;
        }
    /*
    @Override
    public boolean getByQuery(Entity ent, List<BasicDBObject> query) throws UniException{
        MongoCollection table = table(ent);
        DBObject result = findOne(table,query);
        if (result==null)
            return false;
        //ent.setOid(((Long)result.get("oid")).longValue()); // Читается в getData
        ent.getData("", result, 0, null);
        return ent.isValid();
        }
     */
    @Override
    public int getCountByQuery(Entity ent, BasicDBObject query) throws UniException{
        MongoCollection table = table(ent);
        MongoCursor<Document> cursor = table.find(query).iterator();
        int i=0;
        while (cursor.hasNext()){
            i++; cursor.next();
            }
        return i;
    }
    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, BasicDBObject query, int level, String pathsList,RequestStatistic  statistic) throws UniException{
        HashMap path = pathsList.length()!=0 ? parsePaths(pathsList) : null;
        MongoCollection table = table(ent);
        MongoCursor<Document> cursor = table.find(query).iterator();
        final EntityList<Entity> out = new EntityList<>();
        while (cursor.hasNext()){
            if (statistic!=null)
                statistic.entityCount++;
            Document obj = cursor.next();
            Entity xx = null;
            try {
                xx = (Entity) ent.getClass().newInstance();
                xx.getData("", obj, level, this,path,statistic);
                out.add(xx);
            } catch (Exception e) {}
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
                ent.loadDBValues(src,level,this);
                incCashCount(true);
                return ent.isValid()!=mode;
                }
            incCashCount(false);
            }
        MongoCollection table = table(ent);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", id);
        Document result = findOne(table,query);
        if (result==null)
            return false;
        // ent.setOid(((Long)result.get("oid")).longValue()); // Читается в getData
        ent.getData("", result,level,this,path,statistic);
        updateCashedEntity(ent);
        if (statistic!=null)
            statistic.entityCount++;
        return ent.isValid()!=mode;
        }
    @Override
    public synchronized long nextOid(Entity ent,boolean fromEntity) throws UniException {
        MongoCollection table = table(ent);
        MongoCursor<Document> cursor = table.find().iterator();
        if (!cursor.hasNext()) {
            throw UniException.bug("Ошибка генерации ключа в " + ent.getClass().getSimpleName());
            }
        Document result  = cursor.next();
        long oid = ((Long) result.get("oid")).longValue();
        result.put("oid",fromEntity ? ent.getOid()+1 : oid+1);
        Bson filter = Filters.eq("oid",oid);
        table.updateOne(filter,new Document("$set",result));
        return oid;
        //return table(ent).count()+1;
        }

    @Override
    public synchronized long lastOid(Entity ent) throws UniException {
        MongoCollection table = table(ent);
        MongoCursor<Document> cursor = table.find().iterator();
        if (!cursor.hasNext()) {
            throw UniException.bug("Ошибка генерации ключа в " + ent.getClass().getSimpleName());
            }
        Document result  = cursor.next();
        long oid = ((Long) result.get("oid")).longValue();
        return oid;
        }

    @Override
    public void remove(Entity entity, long id) throws UniException {
        MongoCollection table = table(entity);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", id);
        table.deleteOne(query);
        removeCashedEntity(entity);
        }

    @Override
    public synchronized boolean updateField(Entity ent, long id, String fname, String prefix) throws UniException {
        removeCashedEntity(ent,id);
        MongoCollection table = table(ent);
        BasicDBObject query = new BasicDBObject();
        query.put("oid", ent.getOid());
        Document result = findOne(table,query);
        if (result==null)
            return false;
        ent.putFieldValue(prefix, result,0,this,fname);
        Bson filter = Filters.eq("oid",id);
        table.updateOne(filter,new Document("$set",result));
        return true;
    }

    @Override
    public void dropTable(Entity ent) throws UniException {
        clearCash(ent);
        table(ent).drop();
        }

    @Override
    public long add(Entity ent, int level, boolean ownOid) throws UniException {
        long id;
        if (!ownOid){
            id = nextOid(ent);
            ent.setOid(id);
            }
        else
            id = ent.getOid();
        Document document = new Document();
        //document.put("oid", id);
        //document.put("valid",true); // Пишется в putData
        ent.putData("", document,level,this);
        table(ent).insertOne(document);
        return id;
    }

    @Override
    public void update(Entity ent, int level) throws UniException {
        MongoCollection table = table(ent);
        Bson filter = Filters.eq("oid",ent.getOid());
        Document document = new Document();
        document.put("oid", ent.getOid());
        document.put("valid", ent.isValid());
        ent.putData("", document,level,this);
        table.updateOne(filter,new Document("$set",document));
        updateCashedEntity(ent);
    }

    public EntityList<Entity> getAllRecords(Entity ent, int level, String pathsList,RequestStatistic statistic) throws UniException{
        HashMap path = pathsList.length()!=0 ? parsePaths(pathsList) : null;
        MongoCollection table = table(ent);
        MongoCursor<Document> cursor = table.find().iterator();
        EntityList<Entity> out = new EntityList<>();
        while(cursor.hasNext()) {
            if (statistic!=null)
                statistic.entityCount++;
            Document obj = cursor.next();
            Entity xx = null;
            try {
                xx = (Entity) ent.getClass().newInstance();
            } catch (Exception e) {
                throw UniException.bug("Illegal class " + ent.getClass().getSimpleName());
            }
            xx.getData("", obj, level, this,path,statistic);
            out.add(xx);
        }
        return out;
    }
    @Override
    public EntityList<EntityNamed> getListForPattern(Entity ent, String pattern) throws UniException {
        EntityList out = new EntityList();
        MongoCollection table = table(ent);
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
        MongoCursor<Document> cursor = table.find(query).iterator();
        while (cursor.hasNext()){
            Document obj = cursor.next();
            EntityNamed xx = null;
            xx = new EntityNamed();
            xx.setOid(((Long) obj.get("oid")).longValue());
            xx.setName((String) obj.get("name"));
            out.add(xx);
        }
        if (out.size()> ValuesBase.PopupListMaxSize)
            out.clear();
        return out;
        }
    @Override
    public String clearDB(){
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
                String ss = "Не могу создать "+ ValuesBase.EntityFactory.get(item.clazz.getSimpleName())+"\n"+ee.toString();
                System.out.println(ss);
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
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String ss[]){
        I_MongoDB db = new MongoDB36();
        try {
            db.openDB(4567);
            BugMessage bb = new BugMessage();
            bb = new BugMessage("aaaaaaaaaaaaaaaaaaaaaa");
            db.add(bb);
            bb = new BugMessage("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            db.add(bb);
            System.out.println(db.getAll(bb));
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
    @Override
    public EntityList<Entity> getAllByQuery(Entity ent, I_DBQuery query, int level, String pathList, RequestStatistic statistic) throws UniException {
        return getAllByQuery(ent,query.getQuery(),level,pathList,statistic);
        }
    @Override
    public int getCountByQuery(Entity ent, I_DBQuery query) throws UniException {
        return getCountByQuery(ent,query.getQuery());
        }
    @Override
    public String getDriverName() {
        return "MongoDB 3.6";
    }

}
