package romanow.abc.core;

import romanow.abc.core.mongo.DAO;

public class OidString extends DAO {
    public final String mes;
    public final long oid;
    public final int errorCount;
    public OidString(long oid, int errCount,String mes) {
        this.mes = mes;
        this.oid = oid;
        this.errorCount = errCount;
        }
    public OidString() {
        this(0,0,"");
        }
    public boolean valid(){ return errorCount==0;}
    public String toString(){
        return  "oid="+oid +" Ошибок: "+errorCount+"\n"+mes;
    }
}
