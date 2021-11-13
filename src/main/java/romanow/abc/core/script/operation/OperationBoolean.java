package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeBoolean;
import romanow.abc.core.types.TypeFace;


public abstract class OperationBoolean extends Operation{
    public OperationBoolean(int code, String name) {
        super(code,name);
        }
    public abstract boolean opBoolean(boolean one, boolean two);
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace two = stack.pop();
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        if (one.type()==ValuesBase.DTBoolean && two.type()==ValuesBase.DTBoolean){
            boolean vv1 = ((TypeBoolean)one).getValue();
            boolean vv2 = ((TypeBoolean)two).getValue();
            TypeBoolean res = new TypeBoolean(opBoolean(vv1,vv2));
            if (trace)
                out+="->"+res;
            stack.push(res);
            return;
            }
        throwException(context,ValuesBase.SEIllegalOperation, this.name + " " + one.typeName() + " " + two.typeName());
        }
}
