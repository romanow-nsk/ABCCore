package romanow.abc.core.mongo;

import com.thoughtworks.xstream.XStream;

public class DBXStream extends XStream {
    public DBXStream(){
        ignoreUnknownElements();
        allowTypesByWildcard(new String[] {
                "romanow.abc.core.mongo.**",
            });
        alias("QList", DBQueryList.class);
        alias("QInt", DBQueryInt.class);
        alias("QLong", DBQueryLong.class);
        alias("QString", DBQueryString.class);
        alias("QBoolean", DBQueryBoolean.class);
        alias("QDouble", DBQueryDouble.class);
        useAttributeFor(DBQueryList.class, "mode");
        useAttributeFor(DBQueryInt.class, "value");
        useAttributeFor(DBQueryInt.class, "field");
        useAttributeFor(DBQueryInt.class, "cmpMode");
        useAttributeFor(DBQueryLong.class, "value");
        useAttributeFor(DBQueryLong.class, "field");
        useAttributeFor(DBQueryLong.class, "cmpMode");
        useAttributeFor(DBQueryString.class, "value");
        useAttributeFor(DBQueryString.class, "field");
        useAttributeFor(DBQueryString.class, "cmpMode");
        useAttributeFor(DBQueryDouble.class, "value");
        useAttributeFor(DBQueryDouble.class, "field");
        useAttributeFor(DBQueryDouble.class, "cmpMode");
        useAttributeFor(DBQueryBoolean.class, "value");
        useAttributeFor(DBQueryBoolean.class, "field");
        useAttributeFor(DBQueryBoolean.class, "cmpMode");
        addImplicitCollection(DBQueryList.class, "obj");
    }
}
