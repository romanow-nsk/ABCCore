package romanow.abc.core.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ConstMap extends HashMap<String, ConstGroup>{
    public String title(String group, int constId){
        ConstGroup gmap = get(group);
        if (gmap==null)
            return "???: "+group;
        ConstValue ss = gmap.get(constId);
        return ss!=null ? ss.title() : "???: "+group+":"+constId;
        }
    public void put(String group, int constId, String title, String constName){
        ConstGroup gmap = get(group);
        if (gmap==null){
            gmap = new ConstGroup(group);
            put(group,gmap);
            }
        gmap.put(new ConstValue(group,constName,title,constId));
        }
    //------------------------------------------------------------------------------------------------------
    public void createConstList(Class cl){
        Object oo = null;
        try {
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
                put(about.group(),vv,about.title(),mname);
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<ConstValue> getValuesList(String gName){
        ConstGroup gmap = get(gName);
        if (gmap==null)
            return new ArrayList<ConstValue>();
        ArrayList<ConstValue> list = gmap.createList();
        list.sort(new Comparator<ConstValue>() {
            @Override
            public int compare(ConstValue o1, ConstValue o2) {
                return o1.value()-o2.value();
                }
            });
        return list;
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
