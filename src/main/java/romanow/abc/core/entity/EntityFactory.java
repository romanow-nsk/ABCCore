package romanow.abc.core.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class EntityFactory {
    private HashMap<Class, String> foreMap = new HashMap();
    private HashMap<String, Class> invertMap = new HashMap();
    public EntityFactory(){ init(); }
    public void init(){}
    public void put(String name,Class clazz){
        foreMap.put(clazz,name);
        invertMap.put(name,clazz);
        }
    public Class getClassForSimpleName(String name){
        Object ss[] = foreMap.keySet().toArray();
        for(Object oo : ss){
            Class cc = (Class)oo;
            if (cc.getSimpleName().equals(name))
                return cc;
            }
        return null;
        }
    public Class get(String name){
        return invertMap.get(name);
        }
    public String get(Class cls){
        return foreMap.get(cls);
        }
    public String getEntityNameBySimpleClass(String name){
        Class zz = getClassForSimpleName(name);
        return foreMap.get(zz);
        }
    public ArrayList<Class> classList(){
        Class cc[] = new Class[foreMap.size()];
        foreMap.keySet().toArray(cc);
        ArrayList<Class> out = new ArrayList<>();
        for(int i=0;i<cc.length;i++){
            out.add(cc[i]);
            }
        out.sort(new Comparator<Class>() {
            @Override
            public int compare(Class o1, Class o2) {
                return o1.getSimpleName().compareTo(o2.getSimpleName());
            }
        });
        return out;
        }
    public ArrayList<String> nameList(){
        String cc[] = new String[invertMap.size()];
        invertMap.keySet().toArray(cc);
        boolean found=false;
        do  {
            found=false;
            for(int i=1;i<cc.length;i++)
                if(cc[i-1].compareTo(cc[i])>0){
                    String zz = cc[i-1]; cc[i-1]=cc[i];cc[i]=zz;
                    found=true;
                }
        } while (found);
        ArrayList<String> out = new ArrayList<>();
        for(int i=0;i<cc.length;i++)
            out.add(cc[i]);
        return out;
        }
    public int size(){ return foreMap.size(); }
    }
