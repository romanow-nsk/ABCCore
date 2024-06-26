package romanow.abc.core.entity;

import romanow.abc.core.I_Exec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

//--------------- Массив ссылок, собираемых по индексам обратных
public class EntityRefList<T extends Entity> extends ArrayList<T> {
    private transient Class typeT = null;
    private transient HashMap<Long, T> numMap = null;
    private transient HashMap<String, T> nameMap = null;
    private transient HashMap<String, T> titleMap = null;
    public void createMap(){
        nameMap = new HashMap<>();
        numMap = new HashMap<>();
        titleMap = new HashMap<>();
        for(Entity ent : this){
            if (ent==null) continue;
            titleMap.put(ent.getTitle(),(T)ent);
            nameMap.put(ent.getName(),(T)ent);
            numMap.put(ent.getKeyNum(),(T)ent);
            }
        sortByKeyNum();
        }
    public T getByNumber(long key){
        return numMap==null ? null : numMap.get(key);
        }
    public T getByTitle(String key){
        return titleMap==null ? null : titleMap.get(key);
        }
    public T getByName(String key){
        return nameMap==null ? null : nameMap.get(key);
        }
    public T getByNameNoMap(String key){
        for(Entity entity : this)
            if (entity.getName().equals(key))
                return (T)entity;
        return null;
        }
    public T getByNumberNoMap(long key){
        for(Entity entity : this)
            if (entity.getKeyNum()==key)
                return (T)entity;
        return null;
        }
    public EntityRefList(EntityRefList<T> src){
        clear();
        for(Entity ff : src){
            add((T)ff);
            }
        }
    public void set(EntityList<T> src){
        clear();
        for(Entity ent : src)
            add((T)ent);
        }
    public Class getTypeT() {
        return typeT; }
    public void setTypeT(Class typeT) {
        this.typeT = typeT; }
    public String toString(){
        String out="";
        for(int i=0;i<size(); i++){
            if (i!=0) out+="\n";
            out+=get(i).toString();
            }
        return out;
        }
    public String toShortString(){
        String out="";
        for(int i=0;i<size(); i++){
            if (i!=0) out+="\n";
            out+=get(i).toShortString();
            }
        return out;
        }
    public String toFullString(){
        String out="";
        for(int i=0;i<size(); i++){
            if (i!=0) out+="\n";
            out+=get(i).toFullString();
            }
        return out;
    }
    public String toNameString(){
        String out="";
        for(int i=0;i<size(); i++){
            Entity uu = get(i);
            if (uu==null)
                continue;
            if (i!=0) out+=", ";
            out+=uu.objectName();
            }
        return out;
        }
    public boolean removeById(long id){
        for (int i=0;i<size();i++){
            if (get(i).getOid()==id){
                remove(i);
                return true;
                }
            }
        return false;
        }
    public T getById(long id){
        for (int i=0;i<size();i++){
            if (get(i).getOid()==id){
                return get(i);
                }
            }
        return null;
        }
    public void removeAllRefs(){
        clear();
        }
    public void forEachLink(I_Exec fun){
        for (int i=0;i<size();i++){
            if (get(i).getOid()!=0)
                fun.exec(get(i));
            }
        }
    public EntityRefList(){}
    public EntityRefList(Class type){
        typeT = type; }
    public void addRef(Entity ent){
        add((T)ent);
        }
    public String getTitle(){
        return  size()==0 ? "" : (get(0).getTitle()+(size()==1 ? "" : "["+size()+"] "));
    }
    public void sortByTitle(){
        sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }
    public void sortByOid(){
        sortByOid(false);
        }
    public void sortByOid(final boolean inc){
        sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                long vv =  o1.getOid()-o2.getOid();
                if (vv==0) return 0;
                if (inc) vv = -vv;
                return vv <0 ? -1 : 1;
            }
        });
    }
    public void sortByKeyNum(){
        sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                long vv =  o1.getKeyNum()-o2.getKeyNum();
                if (vv==0) return 0;
                return vv <0 ? -1 : 1;
            }
        });
    }
}
