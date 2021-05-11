package romanow.abc.core.entity.baseentityes;

import romanow.abc.core.mongo.DAO;

public class JBoolean extends DAO {
    public JBoolean(){}
    private boolean value=false;
    public JBoolean(boolean val){
        value = val;
        }
    public boolean value(){ return value; }
}
