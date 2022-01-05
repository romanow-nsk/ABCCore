package romanow.abc.core.script;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.operation.Operation;

import java.util.ArrayList;

public class FunctionCode extends ArrayList<Operation> {
    @Getter @Setter private int resultType= ValuesBase.DTGUndefuned;
    public void setResultTypeByGroup(int group) {
        this.resultType = ValuesBase.DTGTypes[group];
        }
    public int getResultGroup(){
        return ValuesBase.DTGroup[resultType];
        }
    public String getResultGroupName(){
        return ValuesBase.DTGroupNames[getResultGroup()];
        }
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
    public boolean isResultNumetic(){
        return getResultGroup()==ValuesBase.DTGReal || getResultGroup()==ValuesBase.DTGInteger;
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
    //------------------- Проверка на разрешение присваивания к типу this=two
    public boolean isSetEnableByType(int type){
        return isSetEnable(ValuesBase.DTGroup[type]);
        }
    public boolean isSetEnable(int group2){
        int group1 = getResultGroup();
        if (group1==ValuesBase.DTGUndefuned || group2==ValuesBase.DTGUndefuned)
            return false;
        if (group1==group2)
            return true;
        if (group1==ValuesBase.DTGSymbol)
            return true;
        if (group1==ValuesBase.DTGLogical)
            return false;
        if (group1==ValuesBase.DTGReal && (group2==ValuesBase.DTGLogical))
            return false;
        if (group1==ValuesBase.DTGInteger && (group2==ValuesBase.DTGLogical))
            return false;
        return true;
        }
}
