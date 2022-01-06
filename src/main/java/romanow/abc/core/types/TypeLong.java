package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;


public class TypeLong extends TypeFace {
    public TypeLong(){
        this(0);
        }
    public TypeLong(long vv){
        setType(ValuesBase.DTLong);
        setIntValue(vv);
        }
    @Override
    public Object cloneWrapper() {
        return new Long(getIntValue());
        }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        if (two.isInteger()){
            long vv =  toLong()-two.toLong();
            if (vv==0) return 0;
            return vv <0 ? -1 : 1;
            }
        if (two.isReal()){
            double vv =  toDouble()-two.toDouble();
            if (vv==0) return 0;
            return vv <0 ? -1 : 1;
            }
        throwBug("Недопустимое сравнение: "+this.getTypeName()+"/"+two.getTypeName());
        return 0;
        }
    @Override
    public String format() throws ScriptException {
        return ""+getIntValue();
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            setIntValue(Long.parseLong(ss));
            } catch (Exception ee){
                throwFormat("Формат целого: "+ss);
                }
            }
    @Override
    public double toDouble() throws ScriptException {
        return getIntValue();
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        setIntValue((long)val);
        }
    @Override
    public long toLong() throws ScriptException {
        return getIntValue();
        }
    @Override
    public void fromLong(long val) throws ScriptException {
        setIntValue(val);
        }
    /*
    @Override
    public void convertToGroup(boolean runTime, int group) throws ScriptException {
        switch (group){
            case ValuesBase.DTGUndefuned:
            case ValuesBase.DTGLogical:
                throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Недопустимое сочетание типов  "+getTypeName()+"<>"+ValuesBase.DTGroupNames[group]);
            case ValuesBase.DTGSymbol:
                if (runTime)
                    setSymbolValue(format());
                break;
            case ValuesBase.DTGInteger:
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
        switch (two.getGroup()){
            case ValuesBase.DTGUndefuned:
            case ValuesBase.DTGLogical:
                throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Недопустимое присваивание  "+getTypeName()+"<>"+two.getTypeName());
            case ValuesBase.DTGSymbol:
                if (runTime)
                    parse(two.getSymbolValue());
                break;
            case ValuesBase.DTGInteger:
                if (runTime)
                    setIntValue(two.getIntValue());
                break;
            case ValuesBase.DTGReal:
                if (runTime)
                    setIntValue(two.toLong());
                break;
            }
        }
    @Override
    public String toString(){
        return super.toString()+" "+getIntValue();
    }
}
