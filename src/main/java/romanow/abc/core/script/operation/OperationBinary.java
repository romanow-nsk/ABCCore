package romanow.abc.core.script.operation;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeDouble;
import romanow.abc.core.types.TypeFace;
import romanow.abc.core.types.TypeLong;

public abstract class OperationBinary extends Operation{
    public OperationBinary(int code, String name) {
        super(code,name);
        }
    public abstract void operation(TypeFace one, TypeFace two) throws ScriptException;
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        String out="";
        TypeFace two = stack.pop();
        TypeFace one = stack.pop();
        if (trace)
            out = toString()+" "+one+" "+two;
        int commonGroup = one.getCommonGroup(two);
        if (one.getGroup()!=commonGroup)
            one.convertToGroup(true,commonGroup);
        if (two.getGroup()!=commonGroup)
            two.convertToGroup(true,commonGroup);   // Приведение типов RunTime
        operation(one,two);
        if (trace)
            out+="->"+one;
        stack.push(one);
        }
}
