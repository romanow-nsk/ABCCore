package romanow.abc.core.jdbc;

public class SQLField {
    private String prefix=null;
    public final String name;
    public final String type;
    public final int DAOType;
    public String getPrefix() {
        return prefix; }
    public void setPrefix(String prefix) {
        this.prefix = prefix; }
    public SQLField(String pref, String name, int DAOType, String type) {
        prefix = pref;
        this.name = name;
        this.type = type;
        this.DAOType = DAOType;
        }
    public String name(){
        return prefix==null ? name : prefix+"_"+name;
        }
    public String toString(){
        return name()+" "+type+" "+DAOType;
        }
}
