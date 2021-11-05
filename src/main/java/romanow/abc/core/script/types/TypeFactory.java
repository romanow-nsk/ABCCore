package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.operation.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeFactory extends ArrayList<TypeFace> {
    private HashMap<Integer,TypeFace> mapCode=new HashMap<>();
    private void addToMaps(TypeFace operation){
        mapCode.put(operation.type(),operation);
        }
    public TypeFactory() {
        addToMaps(new TypeBoolean(false));
        addToMaps(new TypeShort((short) 0));
        addToMaps(new TypeInt(0));
        addToMaps(new TypeLong(0));
        addToMaps(new TypeFloat(0));
        addToMaps(new TypeDouble(0));
        addToMaps(new TypeVoid());
        }
    public  TypeFace getByCode(int code) throws ScriptException {
        TypeFace operation = mapCode.get(code);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEIllegalDT, ValuesBase.SEModeError,"Недопустимый ТД, код: "+code);
        return operation.clone();
        }
}
