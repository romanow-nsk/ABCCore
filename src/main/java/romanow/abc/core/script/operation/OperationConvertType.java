package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.functions.FunctionCall;

public class OperationConvertType extends Operation{
    public final int group;
    public OperationConvertType(int group0) {
        super(ValuesBase.OConvert, "convertType");
        group = group0;
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        context.getStack().getData().convertToGroup(true,group);
        }
    @Override
    public Operation clone() {
        return new OperationConvertType(group);
    }
    @Override
    public String toString(){
        return super.toString()+" "+ValuesBase.DTGroupNames[group];
    }}
