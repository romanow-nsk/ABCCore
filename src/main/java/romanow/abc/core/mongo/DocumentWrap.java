package romanow.abc.core.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;

public class DocumentWrap extends Document {
    private DBObject getSrc;
    private BasicDBObject putSrc;
    public DocumentWrap(DBObject oo){
        getSrc = oo;
        }
    public DocumentWrap(BasicDBObject oo){
        putSrc = oo;
        }
    @Override
    public Object get(Object key) {
        return getSrc.get((String)key);
        }
    @Override
    public Object put(String key, Object value) {
        return  putSrc.put(key,value);
        }
}
