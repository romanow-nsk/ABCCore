package romanow.abc.core.script.operation;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeBoolean;
import romanow.abc.core.types.TypeFace;

public abstract class OperationCompare extends Operation{
    public OperationCompare(int code, String name) {
        super(code,name);
        }
    public abstract boolean opCompare(int res);
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace two = stack.pop();
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        int rez = one.compare(two);
        TypeBoolean res = new TypeBoolean(opCompare(rez));
        if (trace)
            out+="->"+res;
        stack.push(res);
        }
}
