package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeShort extends TypeLong {
    public TypeShort(){
        this(0);
        }
    public TypeShort(int vv){
        setType(ValuesBase.DTShort);
        setIntValue(vv);
        }
    @Override
    public Object cloneWrapper() {
        return new StringBuffer((int)getIntValue());
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            if (ss.endsWith("!"))
                setIntValue(Short.parseShort(ss.substring(0,ss.length()-1),16));
            else
                setIntValue(Short.parseShort(ss));
            } catch (Exception ee){
                throwFormat("Формат целого: "+ss);
                }
            }
}
