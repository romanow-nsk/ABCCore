package romanow.abc.core.mongo;

import romanow.abc.core.UniException;

import java.util.HashMap;

public interface I_MongoRW {
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException;
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo,
        HashMap<String,String> paths, RequestStatistic statistic) throws UniException;
}
