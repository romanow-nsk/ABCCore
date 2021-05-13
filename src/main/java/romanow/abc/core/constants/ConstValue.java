package romanow.abc.core.constants;

public class ConstValue{
    private String groupName="";    // Группа констант
    private String name="";         // Имя в VALUES
    private String title="...";     // Название по-русски
    private int value;              // Значение
    public ConstValue() {}
    public ConstValue(String gr, String nm, String tt, int vv) {
        groupName=gr;
        name=nm;
        title=tt;
        value=vv;
        }
    public String groupName() {
        return groupName; }
    public String name() {
        return name; }
    public String title() {
        return title; }
    public int value() {
        return value; }
    public String toFullString(){
        return groupName+"."+name+"["+title+"]="+value;
        }
    public String toString(){
        return name+"["+title+"]="+value;
    }
}
