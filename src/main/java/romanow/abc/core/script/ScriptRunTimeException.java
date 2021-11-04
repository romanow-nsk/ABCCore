package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.script.ScriptException;

public class ScriptRunTimeException extends ScriptException {
    public ScriptRunTimeException(int code, String message) {
        super(ValuesBase.SETypeRunTime, ValuesBase.SEModeFatal, code, message);
    }
    public ScriptRunTimeException(int code, int mode,String message) {
        super(ValuesBase.SETypeRunTime, mode, code, message);
    }
}
