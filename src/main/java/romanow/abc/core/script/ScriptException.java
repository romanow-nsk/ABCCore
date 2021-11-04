package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;

public class ScriptException extends Exception{
    private int type= ValuesBase.SETypeUndef;
    private int mode=ValuesBase.SEModeInfo;
    public int code;
    public ScriptException(int type, int mode, int code, String message) {
        super(message);
        this.type = type;
        this.code = code;
        this.mode = mode;
        }
    public int getType() {
        return type; }
    public int getMode() {
        return mode; }
    public int getCode() {
        return code; }
    public ScriptException clone(String additional){
        return new ScriptException(type,mode,code,getMessage()+": "+additional);
    }

}
