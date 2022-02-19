package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeBoolean extends TypeFace {
    public TypeBoolean(){
        this(false);
        }
    public TypeBoolean(boolean vv){
        setType(ValuesBase.DTBoolean);
        setBoolValue(vv);
        }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.getType()!=ValuesBase.DTBoolean)
            throwBug("Недопустимое сравнение: "+this.getTypeName()+"/"+two.getTypeName());
        long vv = toLong() - two.toLong();
        return vv==0 ? 0 :(vv<0 ? -1 : 1);
        }
    @Override
    public String formatTo() throws ScriptException {
        return ""+isBoolValue();
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            setBoolValue(Boolean.parseBoolean(ss));
            } catch (Exception ee){
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public Object cloneWrapper() throws ScriptException {
        return new Boolean(isBoolValue());
        }
    @Override
    public double toDouble() throws ScriptException {
        return isBoolValue() ? 1: 0;
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        setBoolValue(val!=0);
        }
    @Override
    public long toLong() throws ScriptException {
        return isBoolValue() ? 1: 0;
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        setBoolValue(val!=0);
        }
    /*
    @Override
    public void convertToGroup(boolean runTime,int group) throws ScriptException{
        if (group!=ValuesBase.DTGLogical)
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка приведения типа "+getTypeName()+"->"+ValuesBase.DTGroupNames[group]);
        }
    */
    @Override
    public void setValue(boolean runTime, TypeFace two) throws ScriptException {
        if (!two.isLogical())
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации  "+getTypeName()+"->"+two.getTypeName());
        if (runTime)
            setBoolValue(two.isBoolValue());
        }
    @Override
    public String toString(){
        return super.toString()+" "+isBoolValue();
        }
}
