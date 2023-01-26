package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeInt extends TypeLong {
    public TypeInt(){
        this(0);
        }
    public TypeInt(int vv){
        setType(ValuesBase.DTInt);
        setIntValue(vv);
        }
    @Override
    public Object cloneWrapper() {
        return new Integer((int)getIntValue());
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            if (ss.endsWith("!"))
                setIntValue(Integer.parseInt(ss.substring(0,ss.length()-1),16));
            else
                setIntValue(Integer.parseInt(ss));

            } catch (Exception ee){
                throwFormat("Формат целого: "+ss);
                }
            }
}
