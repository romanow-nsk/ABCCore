package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeFactory extends ArrayList<TypeFace> {
    private HashMap<Integer, TypeFace> mapCode=new HashMap<>();
    private HashMap<String, TypeFace> mapName=new HashMap<>();
    private void addToMaps(TypeFace operation){
        mapCode.put(operation.getType(),operation);
        mapName.put(operation.getTypeName(),operation);
        }
    public TypeFactory() {
        addToMaps(new TypeBoolean(false));
        addToMaps(new TypeShort((short) 0));
        addToMaps(new TypeInt(0));
        addToMaps(new TypeLong(0));
        addToMaps(new TypeFloat(0));
        addToMaps(new TypeDouble(0));
        addToMaps(new TypeVoid());
        addToMaps(new TypeString(""));
        }
    public TypeFace getByCode(int code) throws ScriptException {
        TypeFace operation = mapCode.get(code);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEBug,"Недопустимый ТД, код: "+code);
        return operation.cloneVar();
        }
    public TypeFace getByName(String name) throws ScriptException {
        TypeFace operation = mapName.get(name);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEBug,"Недопустимый ТД: "+name);
        return operation.cloneVar();
        }
    public int getCodeByName(String name) throws ScriptException {
        TypeFace operation = mapName.get(name);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEBug,"Недопустимый ТД: "+name);
        return operation.getType();
    }
}
