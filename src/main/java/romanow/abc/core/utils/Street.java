package romanow.abc.core.utils;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class Street extends Entity {
    private String name="";
    private int type= ValuesBase.SStreet;
    private EntityLink<City> city = new EntityLink<>(City.class);
    private GPSPoint location = new GPSPoint();
    public GPSPoint getLocation() {
        return location; }
    public void setLocation(GPSPoint location) {
        this.location = location; }
    public Street(){}
    public Street(String nm){
        name = nm; }
    public Street(String nm,int tp){
        name = nm;
        type = tp;}
    public Street(String nm,int tp, long cityId){
        name = nm;
        type = tp;
        city.setOid(cityId); }
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
    public void load(Street src){
        name = src.name;
        type = src.type;
        }
    public EntityLink<City> getCity() {
        return city;
        }
    public void setCity(EntityLink<City> city) {
        this.city = city;
        }
    public Street setCity(City city0) {
        city.setOidRef(city0);
        return this;
        }
    public int streetType(){return  (type & 0x0F0)>>4; }
    public int cityType(){ return city.getRef().getType(); }
    public String getTitle(){ return toString(); }
    public String toString(){
        return city.getTitle()+":"+(streetType()==0 ? "" : ValuesBase.TypesStreet[streetType()]+name);
        }
    public String toFullString(){
        return super.toFullString()+city.toFullString()+":"+(streetType()==0 ? "" : ValuesBase.TypesStreet[streetType()]+name);
    }
    public String toShortString(){
        return (streetType()==0 ? "" : ValuesBase.TypesStreet[streetType()]+name);
    }
    //------------------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        location.putData("s_",document,0,null);
        }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res,level,mongo);
        location.getData("s_",res, 0, null);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        location.getData(row, cnt);
        }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        location.putData(row, cnt);
        }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        location.putHeader("s_",list);
        }
    */
}
