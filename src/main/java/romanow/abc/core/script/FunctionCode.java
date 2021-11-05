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
        for(Operation oo : this)
            out+=oo.toString()+"\n";
        return out;
        }
}
