package romanow.abc.core.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ConstMap extends HashMap<String, HashMap<Integer,String>>{
    public String title(String group, int constId){
        HashMap<Integer,String> gmap = get(group);
        if (gmap==null)
            return "???: "+group;
        String ss = gmap.get(constId);
        return ss!=null ? ss : "???: "+group+":"+constId;
        }
    public void put(String group, int constId, String title){
        HashMap<Integer,String> gmap = get(group);
        if (gmap==null){
            gmap = new HashMap<>();
            put(group,gmap);
            }
        gmap.put(constId,title);
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
                put(about.group(),vv,about.title());
            }
        }
    }
}
