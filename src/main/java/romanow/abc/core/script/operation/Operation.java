package romanow.abc.core.script.operation;

import romanow.abc.core.script.CallContext;
import romanow.abc.core.script.OperationStack;
import romanow.abc.core.script.ScriptRunTimeException;
import romanow.abc.core.script.types.TypeFace;

public abstract class Operation {
    public final int code;
    public final String name;
    public abstract void exec(OperationStack stack, CallContext context) throws ScriptRunTimeException;
    public abstract Operation clone();
    public Operation(int code, String name) {
        this.code = code;
        this.name = name;
        }
    public String toString(){
        return name;
        }

}
