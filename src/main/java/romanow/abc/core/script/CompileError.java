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
        String out =  code+": "+mes+(lexem==null ? "" : " cтрока "+(lexem.strIdx+1)+"["+(lexem.symIdx+1)+"]\n"
                +lexem.src.substring(0,lexem.symIdx)+"|...|"+lexem.src.substring(lexem.symIdx));
        return out;
    }
}
