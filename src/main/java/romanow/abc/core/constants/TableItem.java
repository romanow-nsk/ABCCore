package romanow.abc.core.constants;

import romanow.abc.core.entity.EntityField;
import romanow.abc.core.mongo.DAO;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class TableItem {
    public final String name;
    public final Class clazz;
    public final ArrayList<String> indexes;
    public final boolean isTable;
    private boolean exportXLS=true;
    private ArrayList<EntityField> fields=null;     // Список полей здесь
    public TableItem(String name, Class clazz, ArrayList<String> indexes) {
        this.name = name;
        this.clazz = clazz;
        this.indexes = indexes;
        isTable=true;
        createFields();
        }
    public TableItem(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
        this.indexes = new ArrayList<>();
        isTable=true;
        createFields();
        }
    public TableItem(String name0, Class clazz,boolean isTable0) {
        this.name = name0;
        this.clazz = clazz;
        this.indexes = new ArrayList<>();
        isTable=isTable0;
        createFields();
        }
    public TableItem add(String ss){
        indexes.add(ss);
        return this;
        }
    public ArrayList<EntityField> getFields() {
        return fields;
        }
    public TableItem disableExportXLS(){
        exportXLS = false; return this;
        }
    public boolean isExportXLS() {
        return exportXLS; }
    //------------------------------------------------------------------------------------------------------------------
    //public int getDbTypeId(String ss){
    //    int i;
    //    for (i=0;i<DAO.dbTypes.length;i++){
    //        if (DAO.dbTypes[i].equals(ss)) return i;
    //        }
    //    return -1;
    //    }
    public static boolean isDAOClass(Class cls){
        for(; cls!= null; cls=cls.getSuperclass()) {    // Цикл по текущему и базовым
            if (cls==DAO.class)
                return true;
            }
        return false;
        }
    public void createFields(){
        Class cls=clazz;
        fields = new ArrayList<>();
        HashMap<String,ConstValue> classMap = ValuesBase.daoClassMap();
        for(; cls!= DAO.class; cls=cls.getSuperclass()){    // Цикл по текущему и базовым
            Field flds[]=cls.getDeclaredFields();    //
            for(int i=0;i<flds.length;i++){          // Перебор всех полей
                flds[i].setAccessible(true);         // Сделать доступными private-поля
                if ((flds[i].getModifiers() & Modifier.TRANSIENT)!=0) continue;
                if ((flds[i].getModifiers() & Modifier.STATIC)!=0) continue;
                String tname=flds[i].getType().getName();
                ConstValue cc = classMap.get(tname);
                //int type=getDbTypeId(tname);
                String name=flds[i].getName();
                if (cc==null){
                    if (isDAOClass(flds[i].getType())) {
                        fields.add(new EntityField(name, ValuesBase.DAOLink, flds[i]));
                        }
                    continue;
                    }
                fields.add(new EntityField(name,cc.value(),flds[i]));
            }
        }
    }
    public static void main(String ss[]){
        new TableItem("",null).disableExportXLS();
    }
}


