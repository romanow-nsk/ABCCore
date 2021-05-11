package romanow.abc.core.entity;

public class EntityNamed extends Entity {
    private String name="";
    public EntityNamed(){}
    @Override
    public String getName() {
        return name; }
    public void setName(String name) {
        this.name = name; }
    public String getTitle(){ return name; }
    public String toString(){return getOid() +" "+name; }
}
