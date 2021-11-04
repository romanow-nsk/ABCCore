package romanow.abc.core.script;
public class Lexem {
    public final char type;
    public final String value;
    public int strIdx;
    public int symIdx;
    public String src;
    public Lexem(char type0, String s0) { type=type0; value=s0;  }
    public void setSrc(int strIdx0, int symIdx0, String src0){
        strIdx = strIdx0;
        symIdx = symIdx0;
        src = src0;
        }
    public Lexem clone(){
        return new Lexem(type,value);
    }
    public String toString(){
        return type+":"+value+" "+strIdx+"/"+symIdx+" "+src;
    }
}
