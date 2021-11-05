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
            throw new ScriptException(ValuesBase.SEIllegalTypeConvertion,"Ошибка конвертации остатков "+getResultType()+"/"+two.getResultType());
    }
}
