package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeString extends TypeFace {
    public TypeString(){
        this("");
        }
    public TypeString(String vv){
        setType(ValuesBase.DTString);
        setSymbolValue(vv);
        }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.getType()==ValuesBase.DTString)
            return getSymbolValue().compareTo(((TypeString)two).getSymbolValue());
        throwBug("Недопустимое сравнение: "+this.getTypeName()+"/"+two.getTypeName());
        return 0;
        }
    @Override
    public String formatTo() throws ScriptException {
        return getSymbolValue();
        }
    @Override
    public void parse(String ss) throws ScriptException {
        setSymbolValue(ss);
        }
    public double toDouble() throws ScriptException{
        try {
            return Double.parseDouble(getSymbolValue());
            } catch (Exception ee){
                throwFormat("Недопустимое приведение к double "+getSymbolValue());
                return 0;
                }
        }
    public void fromDouble(double val) throws ScriptException{
        setSymbolValue(""+val);
        }
    @Override
    public long toLong() throws ScriptException {
        try {
            return Long.parseLong(getSymbolValue());
        } catch (Exception ee){
            throwFormat("Недопустимое приведение к long "+getSymbolValue());
            return 0;
            }
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        setSymbolValue(""+val);
        }
    /*
    @Override
    public void convertToGroup(boolean runTime,int group) throws ScriptException {
        switch (group){
            case ValuesBase.DTGUndefuned:
            case ValuesBase.DTGLogical:
                throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Недопустимое сочетание типов  "+getTypeName()+"<>"+ValuesBase.DTGroupNames[group]);
            case ValuesBase.DTGSymbol:
                break;
            case ValuesBase.DTGInteger:
                if (runTime)
                    setIntValue(getIntValue());
                break;
            case ValuesBase.DTGReal:
                if (runTime)
                    setRealValue(getIntValue());
                break;
            }
        setTypeByGroup(group);
        }
     */
    @Override
    public void setValue(boolean runTime, TypeFace two) throws ScriptException {
        if (runTime)
            setSymbolValue(two.formatTo());
        }
    @Override
    public Object cloneWrapper() {
        return new String(getSymbolValue());
        }
    @Override
    public String toString(){
        return super.toString()+" "+getSymbolValue();
    }
        }
