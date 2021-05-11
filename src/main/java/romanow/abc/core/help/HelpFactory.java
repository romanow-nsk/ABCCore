package romanow.abc.core.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HelpFactory {
    private int i=0;
    private HashMap<String,Integer> map = new HashMap<>();
    public void init(){}
    public HelpFactory(){ init(); }
    public void put(String ss){ map.put(ss,i++); }
    public boolean present(String ss){ return map.get(ss)!=null; }
    public int getCode(String ss){
        Integer ii  = map.get(ss);
        return ii==null ? null : ii.intValue();
        }
    public ArrayList<String> createWordList(){
        ArrayList<String> out = new ArrayList<>();
        Object oo[] = map.keySet().toArray();
        for(Object xx : oo)
            out.add((String)xx);
        return out;
    }
}
