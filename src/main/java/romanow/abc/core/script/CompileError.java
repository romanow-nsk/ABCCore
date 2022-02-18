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
        String out =  code+": "+mes+" cтрока "+(lexem.strIdx+1)+"["+(lexem.symIdx+1)+"]\n"+lexem.src+"\n";
        for(int i=0;i<lexem.symIdx;i++)
            out+="_";
        out+="^";
        return out;
    }
}
