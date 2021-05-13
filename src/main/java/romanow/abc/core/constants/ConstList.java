package romanow.abc.core.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstList extends ArrayList<ConstValue> {
    transient HashMap<String,ConstValue> map = new HashMap<>();
    public ConstValue getConst(String name){
        return map.get(name);
        }
    public int getValue(String name){
        return map.get(name) == null ? -1 : map.get(name).value();
        }
    public ConstList(){}
    public void resreshMap(){
        map.clear();
        for(ConstValue cc : this){
            map.put(cc.name(),cc);
            }
        }
    public ArrayList<String> getTitleList(String gName){
        ArrayList<String> out = new ArrayList<>();
        for(ConstValue cc : this){
            if (cc.groupName().equals(gName))
                out.add(cc.title());
            }
        return out;
        }
    public ArrayList<ConstValue> getValuesList(String gName){
        ArrayList<ConstValue> out = new ArrayList<>();
        for(ConstValue cc : this){
            if (cc.groupName().equals(gName))
                out.add(cc);
            }
        return out;
    }
    public int getValue(String group, String title){
        for(ConstValue cc : this){
            if (cc.groupName().equals(group) && cc.title().equals(title))
                return cc.value();
            }
        return -1;
        }
    public ArrayList<ConstList> getByGroups(){
        ArrayList<ConstList> out = new ArrayList<>();
        HashMap<String,ConstList> maps = new HashMap<>();
        for(ConstValue cc : this){
            String gr = cc.groupName();
            if (maps.get(gr)==null){
                ConstList tt = new ConstList();
                maps.put(gr,tt);
                out.add(tt);
                }
            maps.get(gr).put(cc);
            }
        return out;
        }
    public void put(ConstValue cc){
        add(cc);
        map.put(cc.name(),cc);
        }
    public void createConstList(){
        Class cl = null;
        Object oo = null;
        try {
            ValuesBase.env().applicationClass(ValuesBase.ClassNameValues);
            oo = cl.newInstance();
            } catch (Exception e) {
                System.out.println("Не создан список констант");
                return;
                }
        Field[] fields = cl.getFields();
        for(Field fd: fields){
            if ((fd.getModifiers() & Modifier.STATIC)==0) continue;
            fd.setAccessible(true);         // Сделать доступными private-поля
            String mname = fd.getName();
            if(fd.isAnnotationPresent(CONST.class)) {
                CONST about = (CONST) fd.getAnnotation(CONST.class);
                int vv = 0;
                try {
                    vv = fd.getInt(oo);
                    } catch (Exception e) {
                        vv=0;
                        }
                //System.out.println(about.group()+":"+mname + " ="+vv+" "+about.title());
                ConstValue cc = new ConstValue(about.group(),mname,about.title(),vv);
                add(cc);
                map.put(mname,cc);
            }
        }
    }
    public String getCommonName(){
        return size()==0 ? "" : get(0).groupName();
        }
    public String toString(){
        String out = getCommonName()+"\n";
        for (ConstValue cc : this)
            out += cc.toString()+"\n";
        return out;
    }
    //------------------------------------------------------------------------------------------
    public static void main(String ss[]){
        ConstList cc = new ConstList();
        cc.createConstList();
        System.out.println(cc.getTitleList("User"));
        System.out.println(cc.getValue("User","Начальник ТО"));
        System.out.println(cc.getValue("UserMaintenanceChefType"));
        System.out.println(cc);
        System.out.println(cc.getByGroups());
        }
}
