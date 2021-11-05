package romanow.abc.core.script;

import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.operation.*;
import romanow.abc.core.script.types.TypeInt;

import java.util.ArrayList;
import java.util.HashMap;

public class CallContext {
    public final FunctionCode code;
    private OperationStack stack = new OperationStack();
    private VariableList variables = new VariableList();
    private HashMap<Integer, ConstValue> errorsMap;
    private int ip=0;
    public int getIP() {
        return ip; }
    public void incIP(){ ip++; }
    public void jump(int n){ ip+=n; }
    private boolean isFinish(){
        return getIP()==code.size();
        }
    public void reset(){ ip=0; }
    public CallContext(FunctionCode code,VariableList list) {
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SError");
        this.code = code;
        variables = list;
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
                operation.exec(stack,this,trace);
                if (trace){
                    System.out.println("-----------------------------------");
                    String ss = operation.getTrace();
                    if (ss.length()!=0)
                        System.out.println(ss);
                    System.out.println(variables);
                    System.out.println(stack);
                    }
                } catch (ScriptException e) {
                    throw e.clone(mes);
                }
            }
        }
    public FunctionCode getCode() {
        return code; }
    public OperationStack getStack() {
        return stack; }
    public VariableList getVariables() {
        return variables; }
    public int getIp() {
        return ip; }
    public HashMap<Integer, ConstValue> getErrorsMap() {
        return errorsMap; }

    public static void main(String ss[]) throws ScriptException {
        FunctionCode code = new FunctionCode();
        code.add(new OperationPush(new TypeInt(5)));
        code.add(new OperationPush(new TypeInt(6)));
        code.add(new OperationNOP());
        code.add(new OperationAdd());
        code.add(new OperationPushVar("a"));
        code.add(new OperationAdd());
        VariableList list = new VariableList();
        list.add("a",new TypeInt(22));
        CallContext context = new CallContext(code,list);
        context.call(true);
    }
}
