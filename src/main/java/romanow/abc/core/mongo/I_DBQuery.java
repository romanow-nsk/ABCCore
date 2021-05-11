package romanow.abc.core.mongo;

import com.mongodb.BasicDBObject;

public interface I_DBQuery {
    public final static int ModeAnd=0;
    public final static int ModeOr=1;
    public final static int ModeGTE=0;
    public final static int ModeLT=1;
    public final static int ModeLTE=2;
    public final static int ModeEQ=3;
    public final static int ModeNEQ=4;
    public final static int ModeGT=5;
    public final static String sqlModeList[]={"AND","OR"};
    public final static String modeList[]={"$and","$or"};
    public final static String sqlCmpList[]={">=","<","<","=","!=",">"};
    public final static String cmpList[]={"$gte","$lt","$lte","$eq","$ne","$gt"};
    public BasicDBObject getQuery();
    public String getWhere();
    }
