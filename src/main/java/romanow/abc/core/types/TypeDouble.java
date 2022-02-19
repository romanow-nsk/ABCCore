package romanow.abc.core.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class TypeDouble extends TypeFace {
    public TypeDouble(){
        this(0);
    }
    public TypeDouble(double vv){
        setType(ValuesBase.DTDouble);
        setRealValue(vv);
        }
    @Override
    public Object cloneWrapper() {
        return new Double(getRealValue());
    }
    @Override
    public int compare(TypeFace two) throws ScriptException {
        double vv=0;
        if (two.isNumeric())
            vv = two.toDouble();
        else
            throwBug("Недопустимое сравнение: "+this.getTypeName()+"/"+two.getTypeName());
        vv =  vv-two.toDouble();
        if (vv==0) return 0;
        return vv <0 ? -1 : 1;
        }
    @Override
    public String formatTo() throws ScriptException {
        return ""+getRealValue();
        }
    @Override
    public void parse(String ss) throws ScriptException {
        try {
            setRealValue(Double.parseDouble(ss));
        } catch (Exception ee){
            throwFormat("Формат вещественного: "+ss);
            }
        }
    @Override
    public double toDouble() throws ScriptException {
        return getRealValue();
        }
    @Override
    public void fromDouble(double val) throws ScriptException {
        setRealValue(val);
    }
    @Override
    public long toLong() throws ScriptException {
        return (long) getRealValue();
    }
    @Override
    public void fromLong(long val) throws ScriptException {
        setRealValue(val);
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
                if (runTime)
                    setIntValue((long) getRealValue());
                break;
            case ValuesBase.DTGReal:
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
                    setRealValue(two.getIntValue());
                break;
            case ValuesBase.DTGReal:
                if (runTime)
                    setRealValue(two.getRealValue());
                break;
            }
        }
    @Override
    public String toString(){
        return super.toString()+" "+getRealValue();
    }
}
