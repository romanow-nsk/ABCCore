package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public abstract class TypeFace {
    private long word;
    abstract public int type();                                                     // Индекс ТД в ЯОП
    abstract public String typeName();                                              // Имя ТД в ЯОП
    abstract public String typeNameTitle();                                         // Имя ТД внешнее
    abstract public int compare(TypeFace two) throws ScriptRunTimeException;        // Сравнение
    abstract String format(String fmtString) throws ScriptRunTimeException;         // Форматированный ТД
    abstract void  parse(String value) throws ScriptRunTimeException;               // Парсинг из строки, !=null - сообщение об ошибке
    abstract public TypeFace clone();                                               // Клонирование
    public long getWord() {
        return word; }
    public void setWord(long word) {
        this.word = word; }
    public TypeFace(long word) {
        this.word = word; }
    public TypeFace() {
        this.word = 0; }
    public String toString(){
        return typeName()+"/"+typeNameTitle()+" "+word;
        }
    public boolean isIntType(){
        int type = type();
        return type== ValuesBase.DTInt || type==ValuesBase.DTLong || type==ValuesBase.DTShort;
        }
    public boolean isFloatType(){
        int type = type();
        return type== ValuesBase.DTFloat || type==ValuesBase.DTDouble;
    }
}
