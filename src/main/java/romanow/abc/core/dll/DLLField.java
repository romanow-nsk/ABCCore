package romanow.abc.core.dll;

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
        return "Параметр: "+name+" "+ DAO.dbTypes[type]+" "+title+"\n";
    }
}
