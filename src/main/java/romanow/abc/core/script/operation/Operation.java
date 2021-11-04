package romanow.abc.core.script.operation;

import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeFace;

public abstract class Operation {
    public final int code;
    public final String name;
    private String trace="";
    public abstract void exec(OperationStack stack, CallContext context, boolean trace) throws ScriptRunTimeException;
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
