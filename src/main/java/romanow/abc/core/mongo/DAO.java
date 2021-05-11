package romanow.abc.core.mongo;

import romanow.abc.core.I_ExcelRW;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.*;
import romanow.abc.core.export.ExCellCounter;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class DAO implements I_ExcelRW, I_MongoRW {
    private transient ArrayList<EntityField> fld=null;
    private static String lastField="";
    private static int fieldCount=0;
    //-------------------------------------- РЕФЛЕКСИЯ -----------------------------------------------------------------
    public final static String dbTypes[]={"int","String","double","boolean","short","long","java.lang.String",
            "romanow.abc.core.entity.EntityLink","romanow.abc.core.entity.EntityLinkList","romanow.abc.core.entity.EntityRefList"};
    public final static String dbTitle[] ={"int","String","double","boolean","short","long","String",
            "EntityLink","EntityLinkList","EntityRefList","DAOLink"};
    public final static byte dbInt=0,dbString=1,dbDouble=2,dbBoolean=3,dbShort=4,dbLong=5,dbString2=6,dbLink=7,dbLinkList=8,dbRefList=9,dbDAOLink=10;  //  ID-ы сериализуемых типов
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
        Field fld = getFieldEx(name,dbInt);
        try {
            return fld.getInt(this);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public String getFieldValueString(String name) throws UniException {
        Field fld = getFieldEx(name,dbString2);
        try {
            return  (String) fld.get(this);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public boolean getFieldValueBoolean(String name) throws UniException {
        Field fld = getFieldEx(name,dbBoolean);
        try {
            return fld.getBoolean(this);
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueInt(String name, int val) throws UniException {
        Field fld = getFieldEx(name,dbInt);
        try {
            fld.setInt(this,val);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueString(String name, String val) throws UniException {
        Field fld = getFieldEx(name,dbString2);
        try {
            fld.set(this,val);
            } catch(Exception ee){
                throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
        }
    public void setFieldValueBoolean(String name, boolean val) throws UniException {
        Field fld = getFieldEx(name,dbBoolean);
        try {
            fld.setBoolean(this,val);
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+name+"\n"+ee.toString());  }
    }
    //--------------------------------------------------------------------------------------------------------
    public ArrayList<EntityField> getFields() throws UniException {
        if (fld!=null)
            return fld;
        TableItem item = ValuesBase.EntityFactory.getItemForSimpleName(getClass().getSimpleName());
        fld = item.getFields();
        return fld;
        }
    final public void getDBValues(String prefix, org.bson.Document out) throws UniException{
        getDBValues(prefix, out,0,null,null,null);
        }
    private void error(String prefix,EntityField ff){
        String ss = getClass().getSimpleName()+"."+(prefix+ff.name+" отсуствует");
        if (!ss.equals(lastField)){
            System.out.println("["+fieldCount+"]"+lastField+"\n"+ss);
            lastField=ss;
            fieldCount=1;
            }
        else
            fieldCount++;
        }
    //----------------------- Парсинг из строки -------------------------------------------------
    public String toStringValue(){ return  "???"; }
    public void parseValue(String ss) throws Exception {
        throw UniException.bug("Обновление поля не поддерживается");
        }
    //----------------------- Чтение полей из объекта -------------------------------------------
    public String updateField(EntityField ff, String value){
        try {
        switch(ff.type){
            case dbInt:
                ff.field.setInt(this, Integer.parseInt(value));
                break;
            case dbShort:
                ff.field.setShort(this, Short.parseShort(value));
                break;
            case dbLong:
                ff.field.setLong(this, Long.parseLong(value));
                break;
            case dbDouble:
                ff.field.setDouble(this, Double.parseDouble(value));
                break;
            case dbBoolean:
                ff.field.setBoolean(this, Boolean.parseBoolean(value));
                break;
            case dbString2:
            case dbString:
                ff.field.set(this, value);
                break;
            case dbLink:
                EntityLink link = (EntityLink)ff.field.get(this);
                link.setOid(Long.parseLong(value));
                break;
            case dbLinkList:
                EntityLinkList list = (EntityLinkList)ff.field.get(this);
                list.parseIdList(value);
                break;
            case dbDAOLink:
                DAO dd = (DAO)ff.field.get(this);
                dd.parseValue(value);
                break;
            default:
                return "Обновление поля "+ff.name+" не поддерживается";
                }
            return null;
            } catch (Exception ee){  return  "Ошибка формата данных "+ff.name+":"+value;}
        }
    final public ArrayList<EntityField> getDBValues() throws UniException{
        ArrayList<EntityField> out = new ArrayList<>();
        EntityField ff=new EntityField();
        try {
            getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                EntityField xx = new EntityField(ff);
                switch(ff.type){
                    case dbInt:
                        xx.value = ""+ ff.field.getInt(this);
                        break;
                    case dbShort:
                        xx.value = ""+ ff.field.getShort(this);
                        break;
                    case dbLong:
                        xx.value = ""+ ff.field.getLong(this);
                        break;
                    case dbDouble:
                        xx.value = ""+ ff.field.getDouble(this);
                        break;
                    case dbBoolean:
                        xx.value = ""+ ff.field.getBoolean(this);
                        break;
                    case dbString2:
                    case dbString:
                        xx.value = ""+ (String) ff.field.get(this);
                        break;
                    case dbLink:
                        EntityLink link = (EntityLink)ff.field.get(this);
                        xx.value = ""+link.getOid();
                        break;
                    case dbLinkList:
                        EntityLinkList list = (EntityLinkList)ff.field.get(this);
                        xx.value =list.getIdListString();
                        break;
                    case dbDAOLink:
                        DAO dd = (DAO)ff.field.get(this);
                        xx.value = dd.toStringValue();
                        break;
                    }
                out.add(xx);
                }
            return out;
            }
        catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());  }
        }
    public String getFieldPrefix(EntityField ff){
        String key = getClass().getSimpleName()+"."+ff.name;
        String out = ValuesBase.PrefixMap.get(key);
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
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                switch(ff.type) {
                    case dbInt:
                        try {
                            ff.field.setInt(this, ((Integer) out.get(prefix + ff.name)).intValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.setInt(this, 0);
                                }
                        break;
                    case dbShort:
                        try {
                            ff.field.setShort(this, ((Short) out.get(prefix + ff.name)).shortValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.setShort(this, (short) 0);
                                }
                        break;
                    case dbLong:
                        try {
                            ff.field.setLong(this, ((Long) out.get(prefix + ff.name)).longValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.setLong(this, 0);
                                }
                        break;
                    case dbDouble:
                        try {
                            ff.field.setDouble(this, ((Double) out.get(prefix + ff.name)).doubleValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.setDouble(this, 0);
                                }
                        break;
                    case dbBoolean:
                        try {
                            ff.field.setBoolean(this, ((Boolean) out.get(prefix + ff.name)).booleanValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.setBoolean(this, false);
                                }
                        break;
                    case dbString2:
                    case dbString:
                        try {
                            ff.field.set(this, (String) out.get(prefix + ff.name));
                            } catch (Exception ee) {
                                error(prefix, ff);
                                ff.field.set(this, "");
                                }
                        break;
                    // При чтении  объекта EntityLink с oid!=0, level!=0 и наличием класса-прототипа - создается объект
                    // рекурсивно читается и ссылка на него помещается в EntityLink
                    case dbLink:
                        EntityLink link = (EntityLink) ff.field.get(this);
                        try {
                            link.setOid(((Long) out.get(prefix + ff.name)).longValue());
                            } catch (Exception ee) {
                                error(prefix, ff);
                                link.setOid(0);
                            }
                        Class cc = link.getTypeT();         //660
                        if (cc == null)
                            break;
                        cname = cc.getSimpleName();
                        bb = level != 0 && link.getOid() != 0 && !(path != null && path.get(cname) == null);
                        if (!bb)
                            break;
                        Entity two = (Entity) link.getTypeT().newInstance();
                        if (!mongo.getById(two, link.getOid(), level - 1, ValuesBase.DeleteMode, path, statistic)) {
                            //System.out.println("Не найден " + cname + " id=" + link.getOid());
                            link.setOid(0);
                            link.setRef(null);
                        } else {
                            link.setRef(two);
                        }
                        break;
                    case dbLinkList:
                        EntityLinkList list = (EntityLinkList) ff.field.get(this);
                        try {
                            String mm = (String) out.get(prefix + ff.name);
                            list.parseIdList(mm);
                            } catch (Exception ee) {
                                error(prefix, ff);
                                list = new EntityLinkList();
                                }
                        cc = list.getTypeT();
                        if (cc == null)
                            break;
                        cname = cc.getSimpleName();
                        bb = level != 0 && cc != null && !(path != null && path.get(cname) == null);
                        if (!bb)
                            break;
                        for (int ii = 0; ii < list.size(); ii++) {
                            EntityLink link2 = (EntityLink) list.get(ii);
                            if (link2.getOid() == 0)
                                continue;
                            two = (Entity) list.getTypeT().newInstance();
                            if (!mongo.getById(two, link2.getOid(), level - 1, ValuesBase.DeleteMode, path, statistic)) {
                                System.out.println("Не найден " + list.getTypeT().getSimpleName() + " id=" + link2.getOid());
                                link2.setOid(0);
                                link2.setRef(null);
                            } else {
                                link2.setRef(two);
                            }
                        }
                        break;
                    case dbDAOLink:
                        DAO dd = (DAO) ff.field.get(this);
                        String pref = getFieldPrefix(ff);
                        if (pref != null)
                            dd.getData(pref + "_", out, 0, null, statistic);
                        else
                            noField(1, ff);
                        break;
                    }
                }
            for(int i=0;i<fld.size();i++){          // После ВСЕХ
                ff=fld.get(i);
                switch(ff.type) {
                    case dbRefList:           // Загрузка по
                        EntityRefList list2 = (EntityRefList) ff.field.get(this);
                        Class cc = list2.getTypeT();
                        if (cc == null)
                            break;
                        bb = level != 0 && cc != null && !(path != null && path.get(cname) == null);
                        if (!bb)
                            break;      // Имя поля = EntityLink совпадает с именем класса, на который ссылается
                        Entity par1 = (Entity) cc.newInstance();
                        long par2 = ((Entity)this).getOid();
                        I_DBQuery query = new DBQueryList().add("valid",true).add(this.getClass().getSimpleName(),par2);
                        //System.out.println("LinkRefList: "+cc.getSimpleName()+"."+this.getClass().getSimpleName()+"["+par2+"]");
                        EntityList res = mongo.getAllByQuery(par1,query,level-1);
                        list2.set(res);
                        break;
                }
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
        switch(ff.type){
            case dbInt:	    out.put(prefix+ff.name,ff.field.getInt(this)); break;
            case dbShort:	out.put(prefix+ff.name,ff.field.getShort(this)); break;
            case dbLong:	out.put(prefix+ff.name,ff.field.getLong(this)); break;
            case dbDouble:	out.put(prefix+ff.name,ff.field.getDouble(this)); break;
            case dbBoolean: out.put(prefix+ff.name,ff.field.getBoolean(this));break;
            case dbString2:
            case dbString:	out.put(prefix+ff.name,ff.field.get(this)); break;
            // Для объекта EntityLink с oid==0, ref!=null level!=0 и наличием класса-прототипа -
            // вызывается  рекурсивно метод добавления, полученный oid его помещается в EntityLink
            // Для объекта EntityLink с oid<0, ref!=null level!=0 и наличием класса-прототипа -
            // вызывается  рекурсивно метод обновления, -oid его помещается в EntityLink
            case dbLink:
                EntityLink link = (EntityLink)ff.field.get(this);
                long oid = link.getOid();
                if (level!=0 && link.getRef()!=null){
                    switch(link.getOperation()){
                        case ValuesBase.OperationAdd:
                            oid = mongo.add(link.getRef(),level-1);
                            link.setOid(oid);
                            break;
                        case ValuesBase.OperationUpdate:
                            mongo.update(link.getRef(),level-1);
                            break;
                    }
                }
                out.put(prefix+ff.name,link.getOid());
                break;
            case dbLinkList:
                EntityLinkList list = (EntityLinkList)ff.field.get(this);
                if (level!=0){
                    for(int ii=0;ii<list.size();ii++){
                        EntityLink link2 = (EntityLink) list.get(ii);
                        if (link2.getRef()==null)
                            continue;
                        switch(link2.getOperation()){
                            case ValuesBase.OperationAdd:
                                long oid2 = mongo.add(link2.getRef(),level-1);
                                link2.setOid(oid2);
                                break;
                            case ValuesBase.OperationUpdate:
                                mongo.update(link2.getRef(),level-1);
                                break;
                            }
                        }
                    }
                out.put(prefix+ff.name,list.getIdListString());
                //------------- 661 двоичная сериализация
                //out.put(prefix+"_"+ff.name,list.getIdListBinary());
                break;
            case dbDAOLink:
                DAO dd = (DAO)ff.field.get(this);
                String pref = getFieldPrefix(ff);
                if (pref!=null)
                    dd.putData(pref+"_",out,0,null);
                else
                    noField(2,ff);
                break;
            case dbRefList:         // НЕ ПИШЕТСЯ
                 break;
                }
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
    final public void getXMLValues(Row row, ExCellCounter cnt) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                switch(ff.type){
                    case dbInt:	    try {
                                        ff.field.setInt(this, ((int)row.getCell(cnt.getIdx()).getNumericCellValue()));
                                        } catch (Exception ee){}
                                        break;
                    case dbShort:	try {
                                        ff.field.setShort(this, ((short)row.getCell(cnt.getIdx()).getNumericCellValue()));
                                        } catch (Exception ee){}
                                    break;
                    case dbLong:	try {
                                        ff.field.setLong(this, ((long)row.getCell(cnt.getIdx()).getNumericCellValue()));
                                        } catch (Exception ee){}
                                    break;
                    case dbDouble:	try {
                                        ff.field.setDouble(this, (row.getCell(cnt.getIdx()).getNumericCellValue()));
                                        } catch (Exception ee){}
                                    break;
                    case dbBoolean: try {
                                        ff.field.setBoolean(this, ((int)row.getCell(cnt.getIdx()).getNumericCellValue()!=0));
                                        } catch (Exception ee){}
                                    break;
                    case dbString2:
                    case dbString:	try {
                                        ff.field.set(this, row.getCell(cnt.getIdx()).getStringCellValue());
                                        } catch (Exception ee){}
                                    break;
                    case dbLink:    EntityLink link = (EntityLink)ff.field.get(this);
                                    try {
                                        link.setOid(((long)row.getCell(cnt.getIdx()).getNumericCellValue()));
                                        } catch (Exception ee){}
                                    break;
                    case dbLinkList:EntityLinkList list = (EntityLinkList)ff.field.get(this);
                                    try {
                                        list.parseIdList(row.getCell(cnt.getIdx()).getStringCellValue());
                                        } catch (Exception ee){}
                                    break;
                    case dbDAOLink:
                                    DAO dd = (DAO)ff.field.get(this);
                                    String pref = getFieldPrefix(ff);
                                    if (pref!=null)
                                        dd.getData(row,cnt);
                                    else
                                        noField(3,ff);
                                    break;
                    case dbRefList: row.getCell(cnt.getIdx()).getNumericCellValue(); break;
                }
            }
        afterLoad();
        } catch(Exception ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name);  }
        }
    final public void putXMLValues(Row row, ExCellCounter cnt) throws UniException{
        EntityField ff=new EntityField();
        try {
           getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                switch(ff.type){
                    case dbInt:	    row.createCell(cnt.getIdx()).setCellValue(ff.field.getInt(this)); break;
                    case dbShort:	row.createCell(cnt.getIdx()).setCellValue(ff.field.getShort(this)); break;
                    case dbLong:	row.createCell(cnt.getIdx()).setCellValue(ff.field.getLong(this)); break;
                    case dbDouble:	row.createCell(cnt.getIdx()).setCellValue(ff.field.getDouble(this)); break;
                    case dbBoolean: row.createCell(cnt.getIdx()).setCellValue(ff.field.getBoolean(this) ? 1:0);break;
                    case dbString2:
                    case dbString:	row.createCell(cnt.getIdx()).setCellValue((String) ff.field.get(this));
                        break;
                    case dbLink:    EntityLink link = (EntityLink)ff.field.get(this);
                        row.createCell(cnt.getIdx()).setCellValue(link.getOid()); break;
                    case dbLinkList:EntityLinkList list = (EntityLinkList)ff.field.get(this);
                        row.createCell(cnt.getIdx()).setCellValue(list.getIdListString());
                        break;
                    case dbDAOLink:
                        DAO dd = (DAO)ff.field.get(this);
                        String pref = getFieldPrefix(ff);
                        if (pref!=null)
                            dd.putData(row,cnt);
                        else
                            noField(4,ff);
                        break;
                    case dbRefList:
                        row.createCell(cnt.getIdx()).setCellValue(0); break;
                }
            }
        }
        catch(Exception ee){
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
            if(ff.type!=DAO.dbDAOLink)
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
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
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
    final public void copyDBValues(DAO out) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                switch(ff.type){
                    case dbInt:	    try {
                        ff.field.setInt(this, ff.field.getInt(out));
                        } catch (Throwable ee){
                            error("",ff); ff.field.setInt(this,0);
                            }
                        break;
                    case dbShort:	try {
                        ff.field.setShort(this, ff.field.getShort(out));
                    } catch (Throwable ee){
                        error("",ff); ff.field.setShort(this,(short) 0); }
                        break;
                    case dbLong:	try {
                        ff.field.setLong(this, ff.field.getLong(out));
                    } catch (Throwable ee){
                        error("",ff); ff.field.setLong(this, 0); }
                        break;
                    case dbDouble:	try {
                        ff.field.setDouble(this, ff.field.getDouble(out));
                    } catch (Throwable ee){
                        error("",ff); ff.field.setDouble(this,0); }
                        break;
                    case dbBoolean: try {
                        ff.field.setBoolean(this, ff.field.getBoolean(out));
                    } catch (Throwable ee){
                        error("",ff); ff.field.setBoolean(this,false); }
                        break;
                    case dbString2:
                    case dbString:	try {
                        ff.field.set(this, (String)ff.field.get(out));
                    } catch (Throwable ee){
                        error("",ff); ff.field.set(this,""); }
                        break;
                    case dbLink:            // САМИ ОБЪЕКТЫ НЕ КОПИРУЮТСЯ И ССЫЛКИ ТОЖЕ
                        EntityLink link = (EntityLink)ff.field.get(this);
                        EntityLink link2 = (EntityLink)ff.field.get(out);
                        try {
                            link.setOid(link2.getOid());
                            link.setRef(null);
                            } catch (Exception ee){ error("",ff); link.setOid(0); }
                        break;
                    case dbLinkList:
                        EntityLinkList list = (EntityLinkList)ff.field.get(this);
                        EntityLinkList list2 = (EntityLinkList)ff.field.get(out);
                        try {
                            for(int ii=0;ii<list2.size();ii++){
                                list.add(((EntityLink)list2.get(ii)).getOid());
                                }
                            } catch (Exception ee){ error("",ff); list = new EntityLinkList(); }
                        break;
                    case dbDAOLink:
                        DAO proto = (DAO)ff.field.get(this);
                        DAO clone = null;
                        if (proto!=null){
                            clone = (DAO)proto.getClass().newInstance();
                            clone.copyDBValues(proto);
                            }
                        ff.field.set(this,clone);
                        break;
                    }
                }
            }
        catch(Throwable ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());  }
        }
    final public void loadDBValues(DAO src, int level, I_MongoDB mongo) throws UniException{
        EntityField ff=new EntityField();
        try {
            getFields();
            for(int i=0;i<fld.size();i++){
                ff=fld.get(i);
                //EntityField ff2 = fld2.get(i);
                switch(ff.type){
                    case dbInt:	    try {
                            ff.field.setInt(this, ff.field.getInt(src));
                        } catch (Throwable ee){
                        error("",ff); ff.field.setInt(this,0);
                        }
                        break;
                    case dbShort:	try {
                            ff.field.setShort(this, ff.field.getShort(src));
                        } catch (Throwable ee){
                        error("",ff); ff.field.setShort(this,(short) 0); }
                        break;
                    case dbLong:	try {
                            ff.field.setLong(this, ff.field.getLong(src));
                        } catch (Throwable ee){
                        error("",ff); ff.field.setLong(this, 0); }
                        break;
                    case dbDouble:	try {
                            ff.field.setDouble(this, ff.field.getDouble(src));
                        } catch (Throwable ee){
                        error("",ff); ff.field.setDouble(this,0); }
                        break;
                    case dbBoolean: try {
                            ff.field.setBoolean(this, ff.field.getBoolean(src));
                        } catch (Throwable ee){
                        error("",ff); ff.field.setBoolean(this,false); }
                        break;
                    case dbString2:
                    case dbString:	try {
                            ff.field.set(this, (String)ff.field.get(src));
                        } catch (Throwable ee){
                            error("",ff); ff.field.set(this,""); }
                        break;
                    case dbLink:
                        EntityLink link = (EntityLink)ff.field.get(this);
                        EntityLink link2 = (EntityLink)ff.field.get(src);
                        link.setOid(link2.getOid());
                        if (level!=0 && link.getTypeT()!=null && link.getOid()!=0){
                            Entity two = (Entity)link.getTypeT().newInstance();
                            if (!mongo.getById(two,link.getOid(),level-1,null)) {
                                System.out.println("Не найден " + link.getTypeT().getSimpleName() + " id=" + link.getOid());
                                link.setOid(0);
                                link.setRef(null);
                                }
                            else
                                link.setRef(two);
                            }
                        break;
                    case dbLinkList:
                        EntityLinkList list = (EntityLinkList)ff.field.get(this);
                        EntityLinkList list2 = (EntityLinkList)ff.field.get(src);
                        try {
                            for(int ii=0;ii<list2.size();ii++){
                                list.add(((EntityLink)list2.get(ii)).getOid());   // КОПИИИ !!!!!!!!!!!!!!!!!
                                }
                            } catch (Exception ee){ error("",ff); list = new EntityLinkList(); }
                        if (level!=0 && list.getTypeT()!=null){
                            for(int ii=0;ii<list.size();ii++){
                                EntityLink link3 = (EntityLink) list.get(ii);
                                if (link3.getOid()==0)
                                    continue;
                                Entity two = (Entity)list.getTypeT().newInstance();
                                if (!mongo.getById(two,link3.getOid(),level-1,null)) {
                                    System.out.println("Не найден " + list.getTypeT().getSimpleName() + " id=" + link3.getOid());
                                    link3.setOid(0);
                                    link3.setRef(null);
                                    }
                                else
                                    link3.setRef(two);
                                }
                            }
                        break;
                    case dbDAOLink:
                        DAO proto = (DAO)ff.field.get(src);
                        DAO clone = null;
                        if (proto!=null){
                            clone = (DAO)proto.getClass().newInstance();
                            clone.loadDBValues(proto,0,null);
                            }
                        ff.field.set(this,clone);                   // Ссылку копировать - КЛОН
                        //ff.field.set(this,ff.field.get(src));     // Ссылки копировать - КЛОНЫ НЕ ДЕЛАТЬ
                        break;
                }
            }
        }
        catch(Throwable ee){
            throw UniException.bug(getClass().getSimpleName()+"."+ff.name+"\n"+ee.toString());
            }
        }
}
