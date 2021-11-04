package romanow.abc.core.script;

import romanow.abc.core.script.operation.Operation;
import romanow.abc.core.script.operation.OperationAdd;
import romanow.abc.core.script.operation.OperationNOP;
import romanow.abc.core.script.operation.OperationPush;
import romanow.abc.core.script.types.TypeInt;

import java.util.ArrayList;

public class CallContext {
    public final FunctionCode code;
    private OperationStack stack = new OperationStack();
    private int ip=0;
    public int getIP() {
        return ip; }
    public void incIP(){ ip++; }
    public void jump(int n){ ip+=n; }
    private boolean isFinish(){
        return getIP()==code.size();
        }
    public void reset(){ ip=0; }
    public CallContext(FunctionCode code) {
        this.code = code;
        reset();
        }
    public void call(boolean trace) throws ScriptException {
        while(!isFinish()){
            Operation operation = code.get(ip);
            String mes = "ip="+ip+" oper="+operation.toString();
            if (trace)
                System.out.println(mes);
            ip++;
            try {
                operation.exec(stack,this);
                if (trace)
                    System.out.println(stack);
                } catch (ScriptRunTimeException e) {
                    throw e.clone(mes);
                }
            }
        }
    public static void main(String ss[]) throws ScriptException {
        FunctionCode code = new FunctionCode();
        code.add(new OperationPush(new TypeInt(5)));
        code.add(new OperationPush(new TypeInt(6)));
        code.add(new OperationNOP());
        code.add(new OperationAdd());
        CallContext context = new CallContext(code);
        context.call(true);
    }
}
