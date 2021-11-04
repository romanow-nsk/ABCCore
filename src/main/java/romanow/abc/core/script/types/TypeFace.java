package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public abstract class TypeFace {
    private boolean valid=true;
    private String varName="...";
    abstract public int type();                                                     // Индекс ТД в ЯОП
    abstract public String typeName();                                              // Имя ТД в ЯОП
    abstract public String typeNameTitle();                                         // Имя ТД внешнее
    abstract public int compare(TypeFace two) throws ScriptRunTimeException;        // Сравнение
    abstract String format(String fmtString) throws ScriptRunTimeException;         // Форматированный ТД
    public abstract void  parse(String value) throws ScriptRunTimeException;               // Парсинг из строки, !=null - сообщение об ошибке
    abstract public TypeFace clone();                                               // Клонирование
    public TypeFace(TypeFace two){
        valid = two.valid;
        }
    public TypeFace(boolean valid0){
        valid = valid0;
        }
    public TypeFace() {
        valid = true;
        }
    public String toString(){
        return varName+":"+typeName()+"/"+typeNameTitle();
        }
    public boolean isIntType(){
        int type = type();
        return type== ValuesBase.DTInt || type==ValuesBase.DTLong || type==ValuesBase.DTShort;
        }
    public boolean isFloatType(){
        int type = type();
        return type== ValuesBase.DTFloat || type==ValuesBase.DTDouble;
        }
    public boolean isValid() {
        return valid; }
    public void setValid(boolean valid) {
        this.valid = valid; }
    public abstract double toDouble() throws ScriptRunTimeException;
    public abstract void fromDouble(double val) throws ScriptRunTimeException;
    public abstract long toLong() throws ScriptRunTimeException;
    public abstract void fromLong(long val) throws ScriptRunTimeException;
    public String getVarName() {
        return varName; }
    public void setVarName(String varName) {
        this.varName = varName; }
}
