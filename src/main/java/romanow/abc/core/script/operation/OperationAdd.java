package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeFace;

public class OperationAdd extends Operation{
    public OperationAdd() {
        super(ValuesBase.OAdd, "add");
        }
    @Override
    public void exec(OperationStack stack, CallContext context) throws ScriptRunTimeException {
        TypeFace one = stack.pop();
        TypeFace two = stack.pop();
        if (!one.isIntType() || !two.isIntType())
            throw  new ScriptRunTimeException(ValuesBase.SREIllegalOperation, "Операция "+this.name+" "+one.typeName()+" "+two.typeName());
        one.setWord(one.getWord()+two.getWord());
        stack.push(one);
        }
    @Override
    public Operation clone() {
        return new OperationAdd();
        }
}
