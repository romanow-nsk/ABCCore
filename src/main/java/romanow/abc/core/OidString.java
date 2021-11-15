package romanow.abc.core;

import romanow.abc.core.mongo.DAO;

public class OidString extends DAO {
    private String mes="";
    private long oid;
    public OidString(String mes, long oid) {
        this.mes = mes;
        this.oid = oid;
        }
    public OidString() {
        this("",0);
        }
    public boolean valid(){ return oid!=0;}
}
