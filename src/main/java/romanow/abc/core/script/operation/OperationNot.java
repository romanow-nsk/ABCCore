package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeBoolean;
import romanow.abc.core.types.TypeFace;

public class OperationNot extends Operation{
    public OperationNot() {
        super(ValuesBase.ONot,"not");
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one;
        if (one.type()==ValuesBase.DTBoolean){
            boolean vv1 = ((TypeBoolean)one).getValue();
            TypeBoolean res = new TypeBoolean(!vv1);
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        throwException(context,ValuesBase.SEIllegalOperation, this.name + " " + one.typeName());
        }

    @Override
    public Operation clone() {
        return new OperationNot();
        }
}
