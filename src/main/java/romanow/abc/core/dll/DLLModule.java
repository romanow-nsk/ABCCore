package romanow.abc.core.dll;

import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

import java.util.ArrayList;
import java.util.HashMap;

public class DLLModule{
    public final String name;
    public final ArrayList<DLLClass> classes;
    private HashMap<String,DLLClass> map = new HashMap<>();
    public DLLModule(String name, ArrayList<DLLClass> classes) {
        this.name = name;
        this.classes = classes;
        for(DLLClass dllClass : classes)
            map.putIfAbsent(dllClass.name,dllClass);
        }
    public DLLClass get(String name){
        return map.get(name);
        }
    // pars[0] - окружение
    public Object invoke(String className, String funName, Object pars[]) throws ScriptException {
        DLLClass dllClass = map.get(className);
        if (dllClass==null)
            throw new ScriptException(ValuesBase.SEBug,"Не найден класс "+ name+"."+className);
        return dllClass.invoke(name+"."+className,funName,pars);
        }
    public DLLModule() {
        this.name = "";
        this.classes = new ArrayList<>();
        }
    public String toString(){
        String ss =  "Модуль: "+name+"\n";
        for(DLLClass zz : classes)
            ss+=zz.toString();
        return ss;
    }
}
