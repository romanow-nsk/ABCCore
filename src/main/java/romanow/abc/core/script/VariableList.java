package romanow.abc.core.script;

import romanow.abc.core.types.TypeFace;

import java.util.HashMap;

public class VariableList extends HashMap<String, TypeFace> {
    public void add(String name, TypeFace var){
        var.setVarName(name);
        put(name,var);
        }
    public String toString(){
        String out="";
        Object oo[] = this.values().toArray();
        for(Object ss : oo)
            out+=ss.toString()+"\n";
        return out;
        }
}
