package romanow.abc.core.dll;

import java.util.ArrayList;

public class DLLModule{
    public final String name;
    public final ArrayList<DLLClass> classes;
    public DLLModule(String name, ArrayList<DLLClass> classes) {
        this.name = name;
        this.classes = classes;
        }
    public String toString(){
        String ss =  "Модуль: "+name+"\n";
        for(DLLClass zz : classes)
            ss+=zz.toString();
        return ss;
    }
}
