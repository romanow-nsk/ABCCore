package romanow.abc.core.jdbc;

import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityField;
import romanow.abc.core.mongo.DAO;
import org.bson.Document;

import java.sql.ResultSet;
import java.util.ArrayList;

public class SQLFieldList  extends ArrayList<SQLField> {
    public SQLFieldList(){}

    public String getFieldPrefix(String parent,EntityField ff){
        String key = parent+"."+ff.name;
        String out = ValuesBase.PrefixMap.get(key);
        return out;
        }
    public String createFields(Entity ent) {
        return createFields(ent.getClass().getSimpleName(),false);
        }
    public String createFields(String table) {
        return createFields(table,false);
        }
    public String createFields(String table, boolean second) {
        return createFields(null,table,second);
        }
    public String createFields(String prefix, String table, boolean second) {
        TableItem item = ValuesBase.EntityFactory.getItemForSimpleName(table);
        if (item == null)
            return "Entity не найден: " + table;
        if (!second && !item.isTable)
            return "Not table: " + table;
        try {
            DAO ent = (DAO) (item.clazz.newInstance());
            item.createFields();
            ArrayList<EntityField> fields = item.getFields();
            for (int i = 0; i < fields.size(); i++) {
                EntityField ff = fields.get(i);
                switch (ff.type) {
                    case DAO.dbInt:
                        add(new SQLField(prefix,ff.name, ff.type, "int"));
                        break;
                    case DAO.dbDouble:
                        add(new SQLField(prefix,ff.name, ff.type, "real"));
                        break;
                    case DAO.dbBoolean:
                        add(new SQLField(prefix,ff.name, ff.type, "tinyint(1)"));
                        break;
                    case DAO.dbShort:
                        add(new SQLField(prefix,ff.name, ff.type, "tinyint(2)"));
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        add(new SQLField(prefix,ff.name, ff.type, "long"));
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        add(new SQLField(prefix,ff.name, ff.type, "mediumtext"));
                        break;
                    case DAO.dbDAOLink:
                        String pref = getFieldPrefix(table,ff);
                        if (pref == null)
                            return "Не найден префикс для "+table+"."+ff.name;
                        SQLFieldList two = new SQLFieldList();
                        String ss = two.createFields(pref,ff.field.getType().getSimpleName(),true);
                        if (ss!=null)
                            return ss;
                        for(SQLField field : two){
                            add(field);                 // Добавить вторичные со своими префиксами
                            }
                        break;
                    }
                }
            } catch (Exception ee) {
                return ee.toString();
                }
        return null;
        }
    Document createDocument(ResultSet set) throws UniException {
        /*
        try {
            ResultSetMetaData meta=set.getMetaData();
            int n=meta.getColumnCount();
            for (int i=1;i<=n;i++){
                String gg=meta.getColumnName(i);
                String vv=meta.getColumnLabel(i);
                String dd=set.getString(i);
                System.out.println(gg+" "+vv+" "+dd);
                }
            } catch (SQLException ee){}
         */
        Document out = new Document();
        int i=0;
        for(SQLField ff : this){
            i++;
            String ss = "";
            try {
                ss = set.getString(ff.name());
                //System.out.println(ff.name()+":"+ss);
                switch (ff.DAOType){
                case DAO.dbInt:
                    out.append(ff.name(), set.getInt(ff.name()));
                    break;
                case DAO.dbDouble:
                    out.append(ff.name(), set.getDouble(ff.name()));
                    break;
                case DAO.dbBoolean:
                    out.append(ff.name(), set.getBoolean(ff.name()));
                    break;
                case DAO.dbShort:
                    out.append(ff.name(), set.getShort(ff.name()));
                    break;
                case DAO.dbLong:
                case DAO.dbLink:
                    out.append(ff.name(), set.getLong(ff.name()));
                    break;
                case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                case DAO.dbString2:
                case DAO.dbString:
                    out.append(ff.name(), set.getString(ff.name()));
                    break;
                    }
                }catch (Exception ee){
                    System.out.println("Ошибка SQL-RecordSet "+ff.name()+"="+ss);
                    System.out.print(toString());
                    //throw UniException.sql(ee);
                    }
                }
        return out;
        }

    public String createUpdateString(org.bson.Document set) throws UniException{
        return createUpdateString(set,null);
        }
    public String createUpdateString(org.bson.Document set, String only) throws UniException{
        StringBuffer out = new StringBuffer();
        boolean first=true;
        try {
            for(SQLField ff : this){
                if (only!=null && !ff.name().equals(only))
                    continue;
                if (ff.name.equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first=false;
                switch (ff.DAOType){
                    case DAO.dbInt:
                        out.append(ff.name()+"="+((Integer)set.get(ff.name())).intValue());
                        break;
                    case DAO.dbDouble:
                        out.append(ff.name()+"="+((Double)set.get(ff.name())).doubleValue());
                        break;
                    case DAO.dbBoolean:
                        out.append(ff.name()+"="+((Boolean)set.get(ff.name())).booleanValue());
                        break;
                    case DAO.dbShort:
                        out.append(ff.name()+"="+((Short)set.get(ff.name())).shortValue());
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        out.append(ff.name()+"="+((Long)set.get(ff.name())).longValue());
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        out.append(ff.name()+"=\'"+(String) set.get(ff.name())+"\'");
                        break;
                    }
                }
            }catch (Exception ee){
                throw UniException.sql(ee);
                }
        return out.toString();
        }
    public String createInsertString(org.bson.Document set, boolean ownId) throws UniException{
        StringBuffer out = new StringBuffer();
        boolean first=true;
        SQLField ff2=null;
        try {
            out.append('(');
            for(SQLField ff : this) {
                if (!ownId && ff.name().equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first = false;
                out.append(ff.name());
                }
            out.append(") VALUES (");
            first = true;
            for(SQLField ff : this){
                ff2 =ff;
                if (!ownId && ff.name().equals("oid"))
                    continue;
                if (!first) out.append(',');
                else
                    first=false;
                switch (ff.DAOType){
                    case DAO.dbInt:
                        out.append(((Integer)set.get(ff.name())).intValue());
                        break;
                    case DAO.dbDouble:
                        out.append(((Double)set.get(ff.name())).doubleValue());
                        break;
                    case DAO.dbBoolean:
                        out.append(((Boolean)set.get(ff.name())).booleanValue());
                        break;
                    case DAO.dbShort:
                        out.append(((Short)set.get(ff.name())).shortValue());
                        break;
                    case DAO.dbLong:
                    case DAO.dbLink:
                        out.append(((Long)set.get(ff.name())).longValue());
                        break;
                    case DAO.dbLinkList:                                    // LinkList  как строка id,id,...
                    case DAO.dbString2:
                    case DAO.dbString:
                        out.append("\'"+(String) set.get(ff.name())+"\'");
                        break;
                }
            }
        out.append(')');
        }catch (Exception ee){
            System.out.println(ff2.name);
            Utils.printFatalMessage(ee);
            throw UniException.sql(ee);
            }
        return out.toString();
        }

    public String createSelectString() throws UniException{
        StringBuffer out = new StringBuffer();
        boolean first=true;
        SQLField ff2=null;
            first = true;
            for(SQLField ff : this){
                if (!first) out.append(',');
                else
                    first=false;
                out.append(ff.name());
            }
        return out.toString();
        }
    public String toString(){
        String out = "";
        for(SQLField ff : this)
            out += ff.toString()+"\n";
        return out;
    }
}
