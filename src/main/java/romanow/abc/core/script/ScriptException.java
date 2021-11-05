package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;

public class ScriptException extends Exception{
    private int mode=ValuesBase.SEModeInfo;
    public int code;
    public ScriptException(int mode, int code, String message) {
        super(message);
        this.code = code;
        this.mode = mode;
        }
    public ScriptException(int code, String message) {
        super(message);
        this.code = code;
        this.mode = ValuesBase.SEModeInfo;
        }
    public int getMode() {
        return mode; }
    public int getCode() {
        return code; }
    public ScriptException clone(String additional){
        return new ScriptException(mode,code,getMessage()+": "+additional);
    }

}
