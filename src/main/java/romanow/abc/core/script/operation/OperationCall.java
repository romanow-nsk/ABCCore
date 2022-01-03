package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import romanow.abc.core.script.functions.FunctionCall;

public class OperationCall extends Operation{
    public final String funName;
    public OperationCall(String fName) {
        super(ValuesBase.OCall, "call");
        funName = fName;
        }
    @Override
    public void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptException {
        if (trace) setTrace(toString());
        FunctionCall call = context.getFunctionMap().get(funName);
        if (call==null)
            throwException(context,ValuesBase.SEIllegalDT, "Недопустимый ТД  " + funName);
        call.call(context);
        }
    @Override
    public Operation clone() {
        return new OperationCall(funName);
    }
    @Override
    public String toString(){
        return super.toString()+" "+funName;
    }}
