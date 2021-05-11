package romanow.abc.core.entity;

import com.google.gson.Gson;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;

import java.util.ArrayList;

public class EntityList<T extends Entity> extends ArrayList<T> {
    public EntityList(){}
    public Entity getById(long userId){
        for (Entity uu : this)
            if (uu.getOid()==userId)
                return uu;
        return null;
        }
    public int getIdxById(long userId){
        int idx=0;
        for (Entity uu : this){
            if (uu.getOid()==userId)
                return idx;
            idx++;
            }
        return -1;
    }
    public EntityList(EntityList<Entity> src){
        for(Entity ent : src)
            add((T)ent);
        }
    public ArrayList<Entity> convert(){
        ArrayList<Entity> out = new ArrayList<>();
        for (Entity uu : this)
            out.add(uu);
        return out;
        }
    public void add(EntityList<T> two){
        for(T ent : two)
            add(ent);
        }
    public String toString(){
        String out="";
        for(int i=0;i<size(); i++){
            Entity uu = get(i);
            if (i!=0) out+="\n";
            out+=uu.getTitle();
            }
        return out;
        }
    public String toShortString(){
        String out="";
        for(int i=0;i<size(); i++){
            Entity uu = get(i);
            if (i!=0) out+="\n";
            out+=uu.toShortString();
            }
        return out;
        }
    public String toFullString(){
        String out="";
        for(int i=0;i<size(); i++){
            Entity uu = get(i);
            if (i!=0) out+="\n";
            out+=uu.toFullString();
        }
        return out;
        }
    public String toNameString(){
        String out="";
        for(int i=0;i<size(); i++){
            Entity uu = get(i);
            if (i!=0) out+=", ";
            out+=uu.objectName();
            }
        return out;
    }
    public void sortByTitle(){
        sort(new I_Compare() {
            @Override
            public int compare(Entity one, Entity two) {
                return one.getTitle().compareTo(two.getTitle());
                }
            });
        }
    public void sortById(){
        sort(new I_Compare() {
            @Override
            public int compare(Entity one, Entity two) {
                return (int)(one.getOid()-two.getOid());
            }
        });
    }
    public void sort(I_Compare cmp){
        sort(cmp,false);
        }
    public void sort(I_Compare cmp, boolean dec){
        if (size()<=1) return;
        Entity tt[] = new Entity[size()];
        this.toArray(tt);
        for(int i=1;i<tt.length;i++){
            for(int j=i; j>0 && cmp.compare(tt[j],tt[j-1])<0;j--){
                Entity cc = tt[j]; tt[j]=tt[j-1]; tt[j-1]=cc;
            }
        }
        clear();
        for(int i=0;i<tt.length;i++)
            add((T) (dec ? tt[tt.length-i-1] :tt[i]));
        }
    public void load(ArrayList<DBRequest> list) throws UniException {
        clear();
        Gson gson = new Gson();
        for(DBRequest vv : list){
            Entity ent = vv.get(gson);
            add((T) ent);
            }

        }
    }
