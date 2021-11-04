package romanow.abc.core.script;

public class CompileError {
    public final int code;
    public final int mode;
    public final String mes;
    public final Lexem lexem;
    public CompileError(int code0,  int mode,Lexem lex, String mes) {
        code = code0;
        this.mode = mode;
        this.mes = mes;
        lexem =  lex;
        }
    public String toString(){
        return code+": "+mes+" "+lexem.toString();
    }
}
