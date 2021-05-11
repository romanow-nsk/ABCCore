package romanow.abc.core.utils;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;

public class City extends Entity {
    private String name="";
    private int type= ValuesBase.CTown;
    public City(){}
    public City(String nm){
        name = nm; }
    public City(String nm, int tp){
        name = nm;
        type = tp;}
    public String getName() {
        return name;
        }
    public void setName(String name) {
        this.name = name;
        }
    public int getType() {
        return type;
        }
    public void setType(int type) {
        this.type = type;
        }
    public void load(City src){
        name = src.name;
        type = src.type;
        }
    public int cityType(){return  type & 0x0F; }
    public String getTitle(){ return toString(); }
    public String toFullString() {
        return super.toFullString()+(cityType() == 0 ? "" : ValuesBase.TypesCity[cityType()]) + name;
        }
    public String toString() {
        return cityType() == 0 ? "" : ValuesBase.TypesCity[cityType()] + name;
    }
    @Override
    public String objectName() {
        return name; }
}
