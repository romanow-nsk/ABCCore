package romanow.abc.core.script.operation;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.types.TypeFace;

public class OperationSave extends Operation{
    private String name;
    public OperationSave(String name0) {
        super(ValuesBase.OSave,"save");
        name = name0;
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        if (trace)
            setTrace(toString());
        TypeFace one = stack.pop();
        try{
            TypeFace var = context.getVariables().get(name);
            if (var==null)
                throwException(context,ValuesBase.SEVarNotDef, this.name);
                var.setValue(true,one);
            }
        catch (ScriptException ee){
            throwException(context,ValuesBase.SEIllegalTypeConvertion, this.name + " " + one.getTypeName());
            }
    }
    @Override
    public Operation clone() {
        return new OperationSave(name);
        }
    public String toString(){
        return super.toString()+" "+name;
    }
}
