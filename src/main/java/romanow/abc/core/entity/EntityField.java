package romanow.abc.core.entity;

import java.lang.reflect.Field;

public class EntityField {
    public final String name;
    public final int type;
    public final Field field;
    public String value="";
    public String genericName="";
    public String fieldClassName="";
    public EntityField(){
        name="...";
        type=-1;
        field=null;
    }
    public EntityField(String nm, int tp, Field fld0){
        name=nm;
        type=tp;
        field=fld0;
        }
    public EntityField(String nm, int tp, String value0){
        name=nm;
        type=tp;
        field=null;
        value = value0;
        }
    public EntityField(String nm, int tp, Field fld0, String genericName0, String fclass, String value0){
        name=nm;
        type=tp;
        field=fld0;
        genericName = genericName0;
        value = value0;
        fieldClassName = fclass;
        }
    public EntityField(EntityField src) {
        name = src.name;
        type = src.type;
        field = src.field;
        genericName = src.genericName;
        value = src.value;
        }
}
