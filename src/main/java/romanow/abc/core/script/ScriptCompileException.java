package romanow.abc.core.script;

import romanow.abc.core.constants.ValuesBase;

public class ScriptCompileException extends ScriptException {
    public ScriptCompileException(int mode, int code, String message) {
        super(ValuesBase.SETypeCompile, mode, code, message);
    }
}
