package romanow.abc.core.dll;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.mongo.DAO;

public class DLLField {
    public final String name;
    public final String title;
    public final int type;
    public DLLField(String name, int type, String title) {
        this.name = name;
        this.type = type;
        this.title = title;
        }
    public String toString(){
        return "Параметр: "+name+" "+ ValuesBase.constMap().getGroupMapByValue("DAOType").get(type).name()+" "+title+"\n";
    }
}
