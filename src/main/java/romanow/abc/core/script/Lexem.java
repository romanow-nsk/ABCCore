package romanow.abc.core.script;
public class Lexem {
    public final char type;
    public final String value;
    public Lexem(char type0, String s0) { type=type0; value=s0;  }
    public Lexem clone(){
        return new Lexem(type,value);
    }
}
