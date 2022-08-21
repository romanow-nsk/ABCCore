package romanow.abc.core.entity;

import romanow.abc.core.I_ExecLink;
import romanow.abc.core.entity.artifacts.Artifact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class EntityLinkList<T extends Entity> extends ArrayList<EntityLink<T>> {
    private transient Class typeT = null;
    private transient HashMap<Long, T> numMap = null;
    private transient HashMap<String, T> nameMap = null;
    public void createMap(){
        nameMap = new HashMap<>();
        numMap = new HashMap<>();
        for(EntityLink pp : this){
            Entity ent = pp.getRef();
            if (ent==null) continue;
            nameMap.put(ent.getTitle(),(T)ent);
            numMap.put(ent.getKeyNum(),(T)ent);
            }
        }
    public T getByNumber(int key){
        return numMap==null ? null : numMap.get(key);
        }
    public T getByTitle(String key){
        return nameMap==null ? null : nameMap.get(key);
    }
    public EntityLinkList(EntityLinkList<T> src){
        clear();
        for(EntityLink<T> ff : src){
            add(ff.getOid());
            }
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
            Entity uu = get(i).getRef();
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
    public EntityLink getById(long id){
        for (int i=0;i<size();i++){
            if (get(i).getOid()==id){
                return get(i);
                }
            }
        return null;
        }
    public void removeAllRefs(){
        for (EntityLink vv : this)
            vv.setRef(null);
        }
    public void forEachLink(I_ExecLink fun){
        for (int i=0;i<size();i++){
            if (get(i).getOid()!=0)
                fun.exec(get(i));
            }
        }
     public String getIdListBinary(){        // Пропускает id=0
         int sz = 0;
         for (EntityLink vv : this)
             if (vv.getOid() != 0)
                 sz++;
         char out[] = new char[sz*4];
         int k = 0;
         for (EntityLink vv : this){
             long ll = vv.getOid();
             if (ll == 0)
                 continue;
             for(int j=0;j<4;j++){
                out[k+3-j] = (char) ll;     // Старршими байтами вперед
                ll>>=16;
                }
             k+=4;
             }
         return new String(out);
         }
    public String getIdListStringOld(){        // Пропускает id=0
        int count=0;
        String out = "";
        if (size()==0) return out;
        for (int i=0;i<size();i++){
            if (get(i).getOid()==0)
                continue;
            if(count!=0)
                out+=",";
            out+=get(i).getOid();
            count++;
            }
        return out;
        }
    public String getIdListString(){        // Пропускает id=0
        int count=0;
        StringBuffer out = new StringBuffer();
        if (size()==0) return out.toString();
        for (int i=0;i<size();i++){
            if (get(i).getOid()==0)
                continue;
            if(count!=0)
                out.append(',');
            out.append(get(i).getOid());
            count++;
            }
        return out.toString();
        }
    public EntityLinkList(){}
    public EntityLinkList(Class type){
        typeT = type; }
    public EntityLinkList(String ss){
        parseIdList(ss);
        }
    public void parseIdListBinary(String ss){
        clear();
        char cc[] = ss.toCharArray();
        for(int i=0;i<cc.length;){
            long vv=0;
            for(int j=0;j<4;j++,i++){
                vv = (vv<<16) | cc[i] & 0x0FFFF;        // Старшими байтами вперед
                }
            add(new EntityLink<T>(vv));
            }
        }
    public void parseIdList(String ss){
        clear();
        if (ss.length()==0) return;
        if (ss.startsWith("["))
            ss = ss.substring(2);
        if (ss.endsWith("]"))
            ss =ss.substring(0,ss.length()-1);
        if (ss.length()==0) return;
        while(true){
            int idx = ss.indexOf(",");
            if (idx==-1) {
                ss=ss.trim();
                add(new EntityLink<T>(Long.parseLong(ss)));
                return;
                }
            String zz = ss.substring(0,idx).trim();
            add(new EntityLink<T>(Long.parseLong(zz)));
            ss = ss.substring(idx+1);
            }
        }
    public void parseIdListNew(String ss){
        clear();
        if (ss.length()==0) return;
        char cc[] = ss.toCharArray();
        int i=0;
        int k=cc.length-1;      // Последний
        if (cc[i]=='[')
            i++;
        if (cc[k]==']')
            k--;
        int m=0;
        for(int j=i;  j<=k; j++)
            if (cc[j]!=' ')
                cc[m++] = cc[j];
        if (m==0)
            return;
        i=0;
        while(i<m){
            long out = 0;
            while(i<m && cc[i]!=',')
                out = out*10+(int)(cc[i++]-'0');
            add(new EntityLink<T>(out));
            if (i<m) i++;
            }
        }
    public EntityLinkList(ArrayList<Long> idList){
        for(Long ll : idList)
            add(new EntityLink<T>(ll.longValue()));
        }
    public static void main(String ss[]){
        EntityLinkList<Artifact> xx = new EntityLinkList<Artifact>("1,2,3,4,5");
        xx.removeAllRefs();
        }
    public T getRefById(long id){
        for (EntityLink<T> xx : this)
            if (id == xx.getOid())
                return xx.getRef();
        return null;
        }
    public void addOidRef(Entity ent){
        add(new EntityLink<T>(ent.getOid(),(T)ent));
        }
    public void add(long oid){
        add(new EntityLink<T>(oid));
        }
    public String getTitle(){
        return  size()==0 ? "" : (get(0).getTitle()+(size()==1 ? "" : "["+size()+"] "));
    }
    public void sortByTitle(){
        sort(new Comparator<EntityLink<T>>() {
            @Override
            public int compare(EntityLink<T> o1, EntityLink<T> o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }
    public void sortByOid(){
        sort(new Comparator<EntityLink<T>>() {
            @Override
            public int compare(EntityLink<T> o1, EntityLink<T> o2) {
                long vv =  o1.getOid()-o2.getOid();
                if (vv==0) return 0;
                return vv <0 ? -1 : 1;
            }
        });
    }
}
