package romanow.abc.core.constants;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ConstMap extends HashMap<String, ConstGroup>{
    @Getter private ArrayList<ConstValue> constList = new ArrayList<>();
    public String title(String group, int constId){
        ConstGroup gmap = get(group);
        if (gmap==null)
            return "???: "+group;
        ConstValue ss = gmap.get(constId);
        return ss!=null ? ss.title() : "???: "+group+":"+constId;
        }
    public void put(ConstValue value){
        String group = value.groupName();
        ConstGroup gmap = get(group);
        if (gmap==null){
            gmap = new ConstGroup(group);
            put(group,gmap);
            }
        gmap.put(value);
        }
    //------------------------------------------------------------------------------------------------------
    public void createConstList(Class cl) {
        clear();                                    // Очистить для повтороного построения Values-ValuesBase
        Object oo = null;
        try {
            oo = cl.newInstance();
            createConstList(oo);
            } catch (Exception e) {
                System.out.println("Не создан список констант");
                return;
            }
        }
    public void createConstList(Object oo){
        Field[] fields = oo.getClass().getFields();
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
                ConstValue value = new ConstValue(about.group(),mname,about.title(),about.className(),vv);
                constList.add(value);
                //System.out.println(about.group()+":"+mname + " ="+vv+" "+about.title());
                put(value);
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public ConstList getValuesList(String gName){
        ConstGroup gmap = get(gName);
        if (gmap==null)
            return new ConstList("");
        ConstList list = gmap.createList();
        list.sort(new Comparator<ConstValue>() {
            @Override
            public int compare(ConstValue o1, ConstValue o2) {
                return o1.value()-o2.value();
                }
            });
        return list;
        }
    public HashMap<Integer,ConstValue> getGroupMapByValue(String gName){
        ConstList list = getValuesList(gName);
        HashMap<Integer,ConstValue> map = new HashMap<>();
        for(ConstValue cc : list)
            map.put(cc.value(),cc);
        return map;
        }
    public ArrayList<ConstValue> getGroupList(String gName){
        HashMap<Integer,ConstValue> map = getGroupMapByValue(gName);
        Object oo[] = map.values().toArray();
        ArrayList<ConstValue> out = new ArrayList<>();
        for(Object vv : oo)
            out.add((ConstValue) vv);
        out.sort(new Comparator<ConstValue>() {
            @Override
            public int compare(ConstValue o1, ConstValue o2) {
                return o1.value()-o2.value();
                }
            });
        return out;
        }
    public HashMap<String,ConstValue> getGroupMapByName(String gName){
        ConstList list = getValuesList(gName);
        HashMap<String,ConstValue> map = new HashMap<>();
        for(ConstValue cc : list)
            map.put(cc.name(),cc);
        return map;
        }
    public HashMap<String,ConstValue> getGroupMapByClassName(String gName){
        ConstList list = getValuesList(gName);
        HashMap<String,ConstValue> map = new HashMap<>();
        for(ConstValue cc : list)
            map.put(cc.className(),cc);
        return map;
    }
    public HashMap<String,ConstValue> getGroupMapByTitle(String gName){
        ConstList list = getValuesList(gName);
        HashMap<String,ConstValue> map = new HashMap<>();
        for(ConstValue cc : list)
            map.put(cc.title(),cc);
        return map;
    }
    public String toString(){
        String out = "";
        for(String gg : this.keySet())
            out+=get(gg).toString();
        return out;
        }
    public ConstList getConstAll(){
        ConstList out = new ConstList("");
        for(String gg : this.keySet()){
            ConstGroup group = get(gg);
            ConstList list = new ConstList(group.group);
            for(ConstValue vv : group.createList())
                out.add(vv);
            }
        return out;
        }
    public ArrayList<ConstList> getConstByGroups(){
        ArrayList<ConstList> out = new ArrayList<>();
        for(String gg : this.keySet()){
            ConstGroup group = get(gg);
            ConstList list = new ConstList(group.group);
            for(ConstValue vv : group.createList())
                list.add(vv);
            out.add(list);
            }
        return out;
        }
    }
