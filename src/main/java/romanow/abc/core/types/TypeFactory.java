package romanow.abc.core.types;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeFactory extends ArrayList<TypeFace> {
    private HashMap<Integer, TypeFace> mapCode=new HashMap<>();
    private HashMap<String, TypeFace> mapName=new HashMap<>();
    private void addToMaps(TypeFace operation){
        mapCode.put(operation.type(),operation);
        mapName.put(operation.typeName(),operation);
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
    public TypeFace getByCode(int code) throws UniException {
        TypeFace operation = mapCode.get(code);
        if (operation==null)
            throw UniException.bug("Недопустимый ТД, код: "+code);
        return operation.clone();
        }
    public TypeFace getByName(String name) throws UniException {
        TypeFace operation = mapName.get(name);
        if (operation==null)
            throw UniException.bug("Недопустимый ТД: "+name);
        return operation.clone();
    }
    public int getCodeByName(String name) throws UniException {
        TypeFace operation = mapName.get(name);
        if (operation==null)
            throw UniException.bug("Недопустимый ТД: "+name);
        return operation.type();
    }
}
