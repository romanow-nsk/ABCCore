package romanow.abc.core.script.types;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptRunTimeException;

public class TypeInt extends TypeFace{
    private long val;
    public TypeInt(long word) {
        super(word); }
    @Override
    public int type() {
        return ValuesBase.DTInt; }
    @Override
    public String typeName() {
        return "int"; }
    @Override
    public String typeNameTitle() {
        return "целое"; }
    @Override
    public int compare(TypeFace two) {
        return 0;
        }
    @Override
    public String format(String fmtString) throws ScriptRunTimeException {
        try {
            return String.format(fmtString, val);
            } catch (Exception ee){
                throw new ScriptRunTimeException(ValuesBase.SREIntOutFormat,ValuesBase.SEModeWarning,"Форматирование целого: "+fmtString);
                }
        }
    @Override
    public void parse(String value) throws ScriptRunTimeException {
        try {
            val = Integer.parseInt(value);
            } catch (Exception ee){
                throw new ScriptRunTimeException(ValuesBase.SREIntFormat,"Формат целого: "+value);
                }
            }
    @Override
    public TypeFace clone() {
        return new TypeInt(val);
        }
}
