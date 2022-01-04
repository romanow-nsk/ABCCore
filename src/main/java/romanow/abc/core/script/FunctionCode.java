package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.operation.Operation;

import java.util.ArrayList;

public class FunctionCode extends ArrayList<Operation> {
    private int resultType= ValuesBase.DTVoid;
    public int getResultType() {
        return resultType; }
    public void setResultType(int resultType) {
        this.resultType = resultType; }
    public FunctionCode add(FunctionCode two){
        for(Operation operation : two)
            add(operation);
        return this;
        }
    public FunctionCode addOne(Operation oo){
            add(oo);
        return this;
        }
    public String toString(){
        String out="";
        for(int i=0;i<size();i++){
            out+=""+i+": "+get(i).toString()+"\n";
            }
        return out;
        }
    public boolean isResultFloat(){
        return resultType==ValuesBase.DTFloat || resultType==ValuesBase.DTDouble;
        }
    public boolean isResultInt(){
        return resultType==ValuesBase.DTShort || resultType==ValuesBase.DTInt || resultType==ValuesBase.DTLong;
        }
    public boolean isResultNumetic(){
        return isResultFloat() || isResultInt();
        }
    public void convertResultTypes(FunctionCode two,boolean onlyNumeric) throws ScriptException {
        if (getResultType()==ValuesBase.DTBoolean){
            if (two.getResultType()!=ValuesBase.DTBoolean || onlyNumeric)
                throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации boolean");
            }
        else
        if (isResultNumetic() && two.isResultNumetic()){
            if (isResultFloat() || two.isResultFloat())
                setResultType(ValuesBase.DTDouble);
            else
                setResultType(ValuesBase.DTLong);
            }
        else
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации "+getResultType()+"/"+two.getResultType());
        }
    public static int convertResultTypes(int type1, int type2, boolean onlyNumeric) throws ScriptException{
        if (type1==ValuesBase.DTString && type2==ValuesBase.DTString)
            return ValuesBase.DTString;
        if (type1==ValuesBase.DTString || type2==ValuesBase.DTString)
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации String");
        if (onlyNumeric && type1==ValuesBase.DTBoolean && type2==ValuesBase.DTBoolean)
            return ValuesBase.DTBoolean;
        if (onlyNumeric && (type1==ValuesBase.DTBoolean || type2==ValuesBase.DTBoolean))
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации boolean");
        if (!onlyNumeric && type1==ValuesBase.DTBoolean)
            type1 = ValuesBase.DTInt;
        if (!onlyNumeric && type2==ValuesBase.DTBoolean)
            type2 = ValuesBase.DTInt;
        if (type1==ValuesBase.DTShort || type1==ValuesBase.DTInt || type1==ValuesBase.DTLong)
            type1 = ValuesBase.DTLong;
        if (type1==ValuesBase.DTFloat) type1 = ValuesBase.DTDouble;
        if (type2==ValuesBase.DTShort || type2==ValuesBase.DTInt || type2==ValuesBase.DTLong)
            type2 = ValuesBase.DTLong;
        if (type2==ValuesBase.DTFloat) type2 = ValuesBase.DTDouble;
        if (type1!=type2)
            return ValuesBase.DTDouble;
        return type1;
        }
}
