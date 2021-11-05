package romanow.abc.core.script.operation;

import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptException;
import static romanow.abc.core.constants.ValuesBase.*;

public abstract class Operation {
    public final int code;
    public final String name;
    private String trace="";
    public abstract void exec(OperationStack stack, CallContext context,boolean trace) throws ScriptException;
    public void throwException(CallContext context, int code, String str) throws ScriptException{
        ConstValue cc  = context.getErrorsMap().get(code);
        if (cc==null){
            if (code!= ValuesBase.SENoCode)
                throwException(context,code,"Недопустимый код ошибки: "+code+" "+str);
            }
        else
            throw new ScriptException(cc.value(),cc.title()+": "+str);
        }
    public String getTrace() {
        return trace; }
    public void setTrace(String ss){
        trace = ss; }
    public abstract Operation clone();
    public Operation(int code, String name) {
        this.code = code;
        this.name = name;
        }
    public String toString(){
        return name;
        }

}
