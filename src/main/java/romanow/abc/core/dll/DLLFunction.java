package romanow.abc.core.dll;

import romanow.abc.core.mongo.DAO;

import java.util.ArrayList;

public class DLLFunction {
    public final String name;
    public final int resType;
    public final String title;
    public final ArrayList<DLLField> params;
    public DLLFunction(String name, int result, ArrayList<DLLField> params, String title) {
        this.name = name;
        this.resType = result;
        this.params = params;
        this.title = title;
        }
    public String toString(){
        String ss =  "Функция: "+name+" "+ DAO.dbTypes[resType]+" "+title+"\n";
        for(DLLField zz : params)
            ss+=zz.toString();
        return ss;
        }
}
