package romanow.abc.core.mongo;

import romanow.abc.core.I_ExcelRW;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.*;
import romanow.abc.core.export.ExCellCounter;
import org.apache.poi.ss.usermodel.Row;
import romanow.abc.core.mongo.access.I_DAOAccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class DAO implements I_ExcelRW, I_MongoRW {
    private transient ArrayList<EntityField> fld=null;
    private transient TableItem table=null;
    private static HashMap <String,Integer> errorsMap = new HashMap<>();
    private static int noFieldErrorCount=100;
    public Field getField(String name,int type) throws UniException {
        getFields();
        for(EntityField ff : fld)
            if(ff.name.equals(name)) {
                return ff.type == type ? ff.field : null;
                }
        return null;
        }
    public Field getFieldEx(String name,int type) throws UniException {
        Field fld = getField(name,type);
        if (fld==null)
            throw UniException.bug("Нет поля "+getClass().getSimpleName()+"."+name);
        return fld;
        }
    //---------------------- работа с отдельными полями ------------------------------------------------------
    public int getFieldValueInt(String name) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOInt);
        try {
            return fld.getInt(this);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public String getFieldValueString(String name) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOString2);
        try {
            return  (String) fld.get(this);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public boolean getFieldValueBoolean(String name) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOBoolean);
        try {
            return fld.getBoolean(this);
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueInt(String name, int val) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOInt);
        try {
            fld.setInt(this,val);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueString(String name, String val) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOString2);
        try {
            fld.set(this,val);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueBoolean(String name, boolean val) throws UniException {
        Field fld = getFieldEx(name,ValuesBase.DAOBoolean);
        try {
            fld.setBoolean(this,val);
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
    }
    //--------------------------------------------------------------------------------------------------------
    public ArrayList<EntityField> getFields() throws UniException {
        if (fld!=null)
            return fld;
        table = ValuesBase.EntityFactory().getItemForSimpleName(getClass().getSimpleName());
        fld = table.getFields();
        return fld;
        }
    final public void getDBValues(String prefix, org.bson.Document out) throws UniException{
        getDBValues(prefix, out,0,null,null,null);
        }
    public void error(String prefix,EntityField ff){
        error(prefix,ff,null);
        }
    public void error(String prefix,EntityField ff, Exception ee){
        String key = getClass().getSimpleName()+"."+prefix+ff.name;
        Integer vv = errorsMap.get(key);
        if (vv==null){
            System.out.println(key +" отсутствует");
            errorsMap.put(key,1);
        }
        else{
            int vv2 = vv.intValue()+1;
            errorsMap.put(key,vv2);
            if (vv2 % noFieldErrorCount==0)
                System.out.println(key +" отсутствует ="+vv2);
        }
    }
    //----------------------- Парсинг из строки -------------------------------------------------
    public String toStringValue(){ return  "???"; }
    public void parseValue(String ss) throws Exception {
        throw UniException.bug("Обновление поля не поддерживается");
        }
    //----------------------- Чтение полей из объекта -------------------------------------------
    public String updateField(EntityField ff, String value){
        try {
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            map.get(ff.type).updateField(ff,this,value);
            return null;
            } catch (Exception ee){  return  "Ошибка формата данных "+ff.name+":"+value+"\n"+ee.toString();}
        }
    final public ArrayList<EntityField> getDBValues() throws UniException{
        ArrayList<EntityField> out = new ArrayList<>();
        EntityField ff=new EntityField();
        try {
            getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                EntityField xx = new EntityField(ff);
                try {
                    map.get(ff.type).getField(xx,this);
                    out.add(xx);
                    } catch (Exception ee){
                        error("",ff);
                        }
                }
            return out;
            }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());  }
            }
    public String getFieldPrefix(EntityField ff){
        String key = getClass().getSimpleName()+"."+ff.name;
        String out = ValuesBase.PrefixMap().get(key);
        return out;
        }
    final public void getDBValues(String prefix, org.bson.Document out, int level, I_MongoDB mongo,
        HashMap<String,String> path,RequestStatistic statistic) throws UniException{
        String cname="";
        boolean bb=false;
        if (statistic!=null)
            statistic.entityCount++;
        EntityField ff=new EntityField();
        try {
            getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                if (ff.type!=ValuesBase.DAOEntityRefList)
                    map.get(ff.type).getDBValue(ff,this,prefix,out,level,mongo,path,statistic);
                }
            for(int i=0;i<fld.size();i++){          // После ВСЕХ
                ff=fld.get(i);
                if (ff.type==ValuesBase.DAOEntityRefList)
                    map.get(ff.type).getDBValue(ff,this,prefix,out,level,mongo,path,statistic);
            }
        afterLoad();
        }
        catch(Exception ee){
            Utils.printFatalMessage(ee);
            throw UniException.bug(getClass().getSimpleName()+"["+out.get("oid")+"]."+ff.name+"\n"+ee.toString());  }
        }
    final public void putDBValues(String prefix, org.bson.Document out) throws UniException{
        putDBValues(prefix, out,0,null);
        }

    public void putFieldValue(String prefix, org.bson.Document out, int level, I_MongoDB mongo,String fname) throws UniException{
        getFields();
        for(int i=0;i<fld.size();i++){
            EntityField ff=fld.get(i);
            if (ff.name.equals(fname)){
                if (prefix!=null && prefix.length()!=0) {
                    DAO link=null;
                    try {                                   // Для связанных объектов
                        link = (DAO)ff.field.get(this);
                        } catch (Exception ex){
                            throw UniException.bug(getClass().getSimpleName()+"["+out.get("oid")+"]."+fname+"\n"+ex.toString());
                            }
                        link.putData(prefix,out,0,mongo);
                    }
                else
                    putFieldValue("",out,level,mongo,ff);
                return;
                }
            }
        throw UniException.bug("Illegal field" + getClass().getSimpleName()+"["+out.get("oid")+"]."+fname);
        }
    public void putFieldValue(String prefix, org.bson.Document out, int level, I_MongoDB mongo,EntityField ff) throws UniException{
        try {
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            map.get(ff.type).putFieldValue(this,prefix,out,level,mongo,ff);
            }
            catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"["+out.get("oid")+"]."+ff.name+"\n"+ee.toString());  }
        }
    final public void putDBValues(String prefix, org.bson.Document out, int level, I_MongoDB mongo) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                putFieldValue(prefix,out,level,mongo,ff);
            }
        }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"["+out.getString("oid")+"]."+ff.name);  }
        }
    final public void getXMLValues(Row row, String prefix,HashMap<String,Integer> colMap) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                Integer idx = colMap.get(prefix+ff.name);
                if (idx==null && ff.type!=ValuesBase.DAOLink)
                    continue;
                int cnt= ff.type!=ValuesBase.DAOLink ? idx.intValue() : 0;
                try {
                    map.get(ff.type).getXMLValues(ff,this,row,cnt,colMap);
                    } catch (Exception ee){
                        error(prefix,ff);
                        }
                }
            afterLoad();
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+prefix+ff.name);  }
        }
    final public void putXMLValues(Row row, ExCellCounter cnt) throws UniException{
        EntityField ff=new EntityField();
        try {
           getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                map.get(ff.type).putXMLValues(ff,this,row,cnt);
                }
            }
        catch(Exception ee) {
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name);  }
        }
    public void noField(int num,EntityField ff){
        System.out.println("Поле не обрабатывается ["+num+"] "+getClass().getSimpleName()+"."+ff.name);
        }
    public void putXMLHeader(String prefix, ArrayList<String> list) throws UniException{
        EntityField ff=new EntityField();
        getFields();
        for(int i=0;i<fld.size();i++){
            ff = fld.get(i);
            if(ff.type!=ValuesBase.DAOLink)
                list.add(prefix+fld.get(i).name);
            else{
                DAO dd=null;
                try {
                    dd = (DAO) ff.field.get(this);
                    }catch(Exception ee){
                        throw UniException.bug(getClass().getSimpleName()+"."+ff.name);
                        }
                String pref = getFieldPrefix(ff);
                if (pref!=null)
                    dd.putXMLHeader(pref+"_",list);
                else
                    noField(5,ff);
            }
            //System.out.println(getClass().getSimpleName()+"."+prefix+fld.get(i).name);
        }
    }
    //-------------------- Восстановление после загрузки ---------------------------------------------------------
    public void afterLoad(){}
    //----------------- Операции с БД ----------------------------------------------------------------------------------
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo,
        HashMap<String,String> paths,RequestStatistic statistic) throws UniException {
        getDBValues(prefix,res,level,mongo,paths,statistic);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo,RequestStatistic statistic) throws UniException {
        getDBValues(prefix,res,level,mongo,null,statistic);
        }
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
    final public void copyDBValues(DAO src) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                map.get(ff.type).copyDBValues(ff,this,src);
                }
            }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());  }
        }
    final public void loadDBValues(DAO src, int level, I_MongoDB mongo) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                map.get(ff.type).loadDBValues(ff,this,src,level,mongo);
                }
            }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());
            }
        }
    //------------------------------------------------------------------------------------------------------------------
    public String createKotlinClassSource() throws UniException {
        String className =getClass().getSimpleName();
        getFields();
        String out = "class "+className;
        if (table.isTable) out+=":Entity";
        out+="{\n";
        EntityField ff=new EntityField();
        try {
            HashMap<Integer, I_DAOAccess> map = ValuesBase.getOne().getDaoAccessFactory().getClassMap();
            HashMap<Integer, ConstValue> map2 = ValuesBase.constMap().getGroupMapByValue("DAOType");
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                if (table.isTable && ff.fieldClassName.equals("Entity"))
                    continue;
                I_DAOAccess access = map.get(ff.type);
                String typeName = map2.get(ff.type).title();
                typeName = typeName.substring(0,1).toUpperCase()+typeName.substring(1);
                String ss = "    var "+access.createKotlinFieldDefine(ff)+"\n";
                out+=ss;
                }
            }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());
            }
        return out+"    constructor() {}\n}\n";
        }
    public static void createKotlinClassFile(String outPackage,String className, String ss) throws Exception{
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outPackage+"/"+className+".kt"),"UTF-8");
        out.write("package "+outPackage.replace("/",".")+"\n"+ss);
        out.flush();
        out.close();
        }
    public static void createKotlinClassSources(){
        String outPackage = "abc/core/subjectarea";
        File ff = new File(outPackage+"/");
        ff.mkdirs();
        try {
            createKotlinClassFile(outPackage,"JEmpty","class JEmpty {}\n");
            createKotlinClassFile(outPackage,"JInt","class JInt { var value = 0 }\n");
            createKotlinClassFile(outPackage,"JBoolean","class JBoolean { var value = false }\n");
            createKotlinClassFile(outPackage,"JString","class JString { var value = \"\" }\n");
            createKotlinClassFile(outPackage,"JLong","class JLong { var value = 0L}\n");
            createKotlinClassFile(outPackage,"ArtifactList","class ArtifactList : EntityList<Artifact?>(){}\n");
            createKotlinClassFile(outPackage,"ConstList","class ConstList(val group : String?=\"\") : ArrayList<ConstValue>() { }\n");
            createKotlinClassFile(outPackage,"ConstValue","class ConstValue(var groupName:String?=\"\", var name:String?=\"\", var title:String?=\"...\", var className:String?=\"\", var value:Int=0) { }");
            createKotlinClassFile(outPackage,"EntityList","open class EntityList<T : Entity?> : ArrayList<T>() { }");
            createKotlinClassFile(outPackage,"EntityNamed","open class EntityNamed : Entity() { var name = \"\"}\n");
            createKotlinClassFile(outPackage,"EntityLinkList","class EntityLinkList<T : Entity?> : ArrayList<EntityLink<T>?>() {}\n");
            createKotlinClassFile(outPackage,"EntityRefList","class EntityRefList<T : Entity?> : ArrayList<T>() {}\n");
            createKotlinClassFile(outPackage,"EntityLink","class EntityLink<T : Entity?> {\n"+
                "    var oid: Long = 0L\n"+
                "    var ref: T? = null\n}\n");
            createKotlinClassFile(outPackage,"Entity","open class Entity{\n" +
                    "    var oid: Long = 0\n" +
                    "    var isValid = true\n" +
                    "}\n");
            } catch (Exception e) {
                System.out.println("Ошибка создания EntityLink...");
                }
        ArrayList<TableItem> tables = ValuesBase.init().getEntityFactory().classList(true,true);
        for (TableItem item : tables){
            try {
                DAO dao = (DAO)item.clazz.newInstance();
                String ss =  dao.createKotlinClassSource();
                createKotlinClassFile(outPackage,dao.getClass().getSimpleName(),ss);
                } catch (Exception e) {
                    System.out.println("Ошибка создания "+item.name);
                    }
            }
        }

}
