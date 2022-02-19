package romanow.abc.core.script;

import lombok.Getter;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.functions.FunctionCall;
import romanow.abc.core.script.operation.*;
import romanow.abc.core.types.TypeFactory;
import romanow.abc.core.types.TypeInt;

import java.util.HashMap;

public class CallContext {
    public final FunctionCode code;
    @Getter private Object callEnvironment;
    @Getter private ErrorList errorList = new ErrorList();
    @Getter private StringBuffer traceList = new StringBuffer();
    @Getter private OperationStack stack = new OperationStack();
    @Getter private VariableList variables = new VariableList();
    @Getter private HashMap<Integer, ConstValue> errorsMap;
    @Getter private HashMap<String, FunctionCall> functionMap = new HashMap<>();
    @Getter private TypeFactory typeFaces = new TypeFactory();
    @Getter private int ip=0;
    public void trace(String ss){
        traceList.append(ss+"\n");
        }
    public int getIP() {
        return ip; }
    public void incIP(){ ip++; }
    public void jump(int n){ ip+=n; }
    private boolean isFinish(){
        return getIP()==code.size();
        }
    public void reset(){ ip=0; }
    public CallContext(FunctionCode code,VariableList list,HashMap<String, FunctionCall> functionMap0,Object env) {
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SError");
        this.code = code;
        variables = list;
        functionMap = functionMap0;
        callEnvironment = env;
        errorList.clear();
        reset();
        }
    public CallContext(Syntax syntax,Object env) {
        errorsMap = ValuesBase.constMap.getGroupMapByValue("SError");
        this.code = syntax.getCodeBase();
        variables = syntax.getVariables();
        functionMap = syntax.getFunctionMap();
        callEnvironment = env;
        reset();
    }
    public void call(boolean trace) throws ScriptException {
        while(!isFinish()){
            Operation operation = code.get(ip);
            String mes = "ip="+ip+" oper="+operation.toString();
            if (trace)
                trace(mes);
            ip++;
            try {
                operation.exec(stack,this,trace);
                if (trace){
                    trace("-----------------------------------");
                    String ss = operation.getTrace();
                    if (ss.length()!=0)
                        trace(ss);
                    trace(variables.toString());
                    trace(stack.toString());
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
    public HashMap<String, FunctionCall> getFunctionMap() {
        return functionMap; }
    public Object getCallEnvironment() {
        return callEnvironment;}
    //---------------------------------------------------------------------------------------------------
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
        CallContext context = new CallContext(code,list,new HashMap<>(),null);
        context.call(true);
    }
}
