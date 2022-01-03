package romanow.abc.core.dll;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

import java.util.ArrayList;
import java.util.HashMap;

public class DLLClass {
    public final Class<?> dllClass;
    public final String name;
    public final ArrayList<DLLFunction> functions;
    private HashMap<String,DLLFunction> map = new HashMap<>();
    public DLLClass(Class<?> dllClass, ArrayList<DLLFunction> functions) {
        this.dllClass = dllClass;
        this.name = dllClass.getSimpleName();
        this.functions = functions;
        for(DLLFunction dllFunc : functions)
            map.putIfAbsent(dllFunc.name,dllFunc);
        }
    public DLLFunction get(String name){
        return map.get(name);
        }
    public String toString(){
        String ss =  "Класс: "+name+"\n";
        for(DLLFunction zz : functions)
                ss+=zz.toString();
            return ss;
        }
    // pars[0] - окружение
    public Object invoke(String header, String funName, Object pars[]) throws ScriptException {
        DLLFunction dllFun = map.get(funName);
        if (dllFun==null)
            throw new ScriptException(ValuesBase.SEBug,"Не найдена функция "+ header+"."+funName);
        return dllFun.invoke(header+"."+funName,pars);
        }

}
