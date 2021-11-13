package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.types.TypeFace;

import java.util.ArrayList;

public class OperationStack extends ArrayList<TypeFace> {
    public final static int StackLimit=10000;
    private int stackLimit=StackLimit;
    public OperationStack(int stackLimit0) {
        super(stackLimit0);
        this.stackLimit = stackLimit;
        }
    public OperationStack() {
        super(StackLimit);
        }
    public int sp(){ return size()-1; }
    public void push(TypeFace operand) throws ScriptException {
        if (sp()>=stackLimit)
            throw new ScriptException(ValuesBase.SEStackOver,"Переполнение стека операндов");
        add(operand);
        }
    public TypeFace getData() throws ScriptException{
        if (sp()==-1)
            throw new ScriptException(ValuesBase.SEStackEmpty,"Пустой стек");
        return get(sp());
        }
    public TypeFace pop() throws ScriptException{
        if (sp()==-1)
            throw new ScriptException(ValuesBase.SEStackEmpty,"Пустой стек");
        return remove(sp());
        }
    public TypeFace getData(int n) throws ScriptException{
        if (sp()-n<0 || n>0)
            throw new ScriptException(ValuesBase.SEStackLimits,"Границы стека "+sp()+" offset="+n);
        return get(sp()-n);
        }
    public String toString(){
        String out="Стек операндов:";
        for(int sp=0, i=size()-1;i>=0; i--,sp--)
            out+="\n["+sp+"] "+get(i);
        return out;
        }
}
