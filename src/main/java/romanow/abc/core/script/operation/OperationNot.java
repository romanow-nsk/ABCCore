package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeBoolean;
import romanow.abc.core.script.types.TypeFace;

public class OperationNot extends Operation{
    public OperationNot() {
        super(ValuesBase.ONot,"not");
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptRunTimeException {
        String out="";
        TypeFace one = stack.pop();
        TypeFace two = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        if (one.type()==ValuesBase.DTBoolean){
            boolean vv1 = ((TypeBoolean)one).getValue();
            TypeBoolean res = new TypeBoolean(!vv1);
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        throwException(context,ValuesBase.SREIllegalOperation, this.name + " " + one.typeName() + " " + two.typeName());
        }

    @Override
    public Operation clone() {
        return new OperationNot();
        }
}
