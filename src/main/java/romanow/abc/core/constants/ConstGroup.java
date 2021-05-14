package romanow.abc.core.constants;

import java.util.ArrayList;
import java.util.HashMap;

public class ConstGroup {
    public final String group;
    private HashMap<Integer,ConstValue> map = new HashMap<>();
    public ConstGroup(String group) {
        this.group = group; }
    public ConstValue get(int key){ return map.get(key); }
    public void put(ConstValue cc){
        int key = cc.value();
        ConstValue vv = get(key);
        if (vv!=null)
            map.remove(key);
        map.put(key,cc);
        }
    public ConstList createList(){
        ConstList out = new ConstList("");
        Object oo[] = map.values().toArray();
        for(Object vv : oo)
            out.add((ConstValue)vv );
        return out;
        }
    public String toString(){
        String out = "Group "+group+"\n";
        for(ConstValue cc : createList())
            out+=cc.toString()+"\n";
        return out;
        }
}
