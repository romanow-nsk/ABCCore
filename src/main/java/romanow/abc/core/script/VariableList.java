package romanow.abc.core.script;

import romanow.abc.core.script.types.TypeFace;

import java.util.HashMap;

public class VariableList extends HashMap<String, TypeFace> {
    public void add(String name,TypeFace var){
        var.setVarName(name);
        put(name,var);
    }
}
