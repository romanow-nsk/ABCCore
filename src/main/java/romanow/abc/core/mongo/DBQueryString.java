package romanow.abc.core.mongo;

import com.mongodb.BasicDBObject;

public class DBQueryString implements I_DBQuery{
    public final int cmpMode;
    public final String field;
    public final String value;
    public DBQueryString(int cmpMode, String field, String value) {
        this.cmpMode = cmpMode;
        this.field = field;
        this.value = value; }
    public DBQueryString(String field, String value) {
        this.cmpMode = ModeEQ;
        this.field = field;
        this.value = value; }
    @Override
    public BasicDBObject getQuery() {
        if (cmpMode==ModeEQ)
            return new BasicDBObject(field,value);
        else
            return new BasicDBObject(field, new BasicDBObject(cmpList[cmpMode], value));
        }
    @Override
    public String getWhere() {
        return field+ sqlCmpList[cmpMode]+"\'"+value+"\'";
    }
}
