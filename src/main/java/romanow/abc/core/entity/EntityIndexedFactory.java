package romanow.abc.core.entity;

import romanow.abc.core.constants.TableItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class EntityIndexedFactory {
    private HashMap<Class, TableItem> foreMap = new HashMap();
    private HashMap<String, TableItem> invertMap = new HashMap();
    private HashMap<String, TableItem> simpleNameMap = new HashMap();
    public EntityIndexedFactory(){ init(); }
    public void init(){}
    public void put(TableItem item){
        String shortName = item.clazz.getSimpleName();
        TableItem old = simpleNameMap.get(shortName);
        if (old!=null){       // Замена по коротким именам
            simpleNameMap.remove(shortName);
            invertMap.remove(old.name);
            foreMap.remove(old.clazz);
            }
        foreMap.put(item.clazz,item);
        invertMap.put(item.name,item);
        simpleNameMap.put(item.clazz.getSimpleName(),item);
        }
    public Class getClassForSimpleName(String name){
        TableItem item = getItemForSimpleName(name);
        return item==null ? null : item.clazz;
        }
    public TableItem getItemForSimpleName(String name){
        return simpleNameMap.get(name);
        }
    public Class get(String name){
        return invertMap.get(name).clazz;
        }
    public String get(Class cls){
        return foreMap.get(cls).name;
        }
    public String getEntityNameBySimpleClass(String name){
        TableItem item = getItemForSimpleName(name);
        return item==null ? null : item.name;
        }
    public ArrayList<TableItem> classList(){
        return classList(false);
        }
    public ArrayList<TableItem> classList(final boolean className){
        TableItem cc[] = new TableItem[foreMap.size()];
        foreMap.values().toArray(cc);
        ArrayList<TableItem> out = new ArrayList<>();
        for(int i=0;i<cc.length;i++){
            if (cc[i].isTable)
                out.add(cc[i]);
            }
        out.sort(new Comparator<TableItem>() {
            @Override
            public int compare(TableItem o1, TableItem o2) {
                return className ? o1.clazz.getSimpleName().compareTo(o2.clazz.getSimpleName()) : o1.name.compareTo(o2.name);
            }
        });
        return out;
        }
    public ArrayList<String> nameList(){
        TableItem cc[] = new TableItem[invertMap.size()];
        invertMap.values().toArray(cc);
        ArrayList<String> out = new ArrayList<>();
        for(int i=0;i<cc.length;i++)
            if (cc[i].isTable)
                out.add(cc[i].name);
        out.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return out;
        }
    public int size(){ return foreMap.size(); }
    }
