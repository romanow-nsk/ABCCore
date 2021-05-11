package romanow.abc.core.entity.contacts;

import romanow.abc.core.mongo.DAO;
import romanow.abc.core.entity.I_Name;
import romanow.abc.core.entity.I_TypeName;

public class Contact extends DAO implements I_TypeName,I_Name {
    protected String value="";
    public String getValue() {
        return value;
        }
    public void setValue(String value) {
        this.value = value;
        }
    public Contact(String val){
        value = val;
        }
    public Contact(){
        }
    @Override
    public String getName() {
        return !valid() ? "???" : value ;
        }
    @Override
    public String typeName() {
        return "???";
        }
    @Override
    public int typeId() { return -1; }
    @Override
    public Class typeClass() { return Object.class; }
    public boolean valid(){ return false; }
    public boolean parseAndSet(String ss){ return false; }
    public String toString(){ return value; }
    @Override
    public String toStringValue() {
        return value;
        }
    @Override
    public void parseValue(String ss) throws Exception {
        value = ss;
        }

}
