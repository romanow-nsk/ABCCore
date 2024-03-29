package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

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
        addToMaps(new OperationPushVar(null));
        addToMaps(new OperationAdd());
        addToMaps(new OperationSub());
        addToMaps(new OperationMul());
        addToMaps(new OperationDiv());
        addToMaps(new OperationJmpFalse(0));
        addToMaps(new OperationJmpTrue(0));
        addToMaps(new OperationEQ());
        addToMaps(new OperationNE());
        addToMaps(new OperationLE());
        addToMaps(new OperationLT());
        addToMaps(new OperationGE());
        addToMaps(new OperationGT());
        addToMaps(new OperationAnd());
        addToMaps(new OperationOr());
        addToMaps(new OperationNot());
        addToMaps(new OperationXor());
        addToMaps(new OperationAndWord());
        addToMaps(new OperationOrWord());
        addToMaps(new OperationNotWord());
        addToMaps(new OperationXorWord());
        addToMaps(new OperationPow());
        addToMaps(new OperationConvertType(0));
        addToMaps(new OperationCall(""));
    }
    public  Operation getByCode(int code) throws ScriptException {
        Operation operation = mapCode.get(code);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEIllegalOperation, ValuesBase.SEModeError,"Недопустимая операция, код: "+code);
        return operation.clone();
        }
    public  Operation getByName(String name) throws ScriptException {
        Operation operation = mapName.get(name);
        if (operation==null)
            throw new ScriptException(ValuesBase.SEIllegalOperation, ValuesBase.SEModeError,"Недопустимая операция, код: "+name);
        return operation.clone();
        }
}
