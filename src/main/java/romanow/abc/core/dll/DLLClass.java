package romanow.abc.core.dll;

import java.util.ArrayList;

public class DLLClass {
    public final Class<?> dllClass;
    public final String name;
    public final ArrayList<DLLFunction> functions;
    public DLLClass(Class<?> dllClass, ArrayList<DLLFunction> functions) {
        this.dllClass = dllClass;
        this.name = dllClass.getSimpleName();
        this.functions = functions;
        }
    public String toString(){
        String ss =  "Класс: "+name+"\n";
        for(DLLFunction zz : functions)
                ss+=zz.toString();
            return ss;
        }
}
