package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptCompileException;

import java.util.ArrayList;
import java.util.HashMap;

public class OperationFactory extends ArrayList<Operation> {
    private HashMap<Integer,Operation> mapCode=new HashMap<>();
    private HashMap<String,Operation> mapName=new HashMap<>();
    private void addToMaps(Operation operation){
        mapCode.put(operation.code,operation);
        mapName.put(operation.name,operation);
        }
    public OperationFactory() {
        addToMaps(new OperationNOP());
        addToMaps(new OperationJmp(0));
        addToMaps(new OperationPush(null));
        addToMaps(new OperationAdd());
        }
    public  Operation getByCode(int code) throws ScriptCompileException {
        Operation operation = mapCode.get(code);
        if (operation==null)
            throw new ScriptCompileException(ValuesBase.SREIllegalOperation, ValuesBase.SEModeError,"Недопустимая операция, код: "+code);
        return operation.clone();
        }
    public  Operation getByName(String name) throws ScriptCompileException {
        Operation operation = mapName.get(name);
        if (operation==null)
            throw new ScriptCompileException(ValuesBase.SREIllegalOperation, ValuesBase.SEModeError,"Недопустимая операция, код: "+name);
        return operation.clone();
        }
}
