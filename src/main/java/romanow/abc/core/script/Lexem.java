package romanow.abc.core.script;

import romanow.abc.core.utils.Pair;

public class Lexem extends LexState{
    public final char type;
    public String value;
    public final int backSymCount;
    public int strIdx;
    public int symIdx;
    public String src;
    public Lexem(char type0, int back, String s0) {
        type=type0; value=s0;
        backSymCount = back;
        }
    public Lexem(char type0, String s0) {
        type=type0; value=s0;
        backSymCount = 0;
        }
    public void setSrc(int strIdx0, int symIdx0, String src0){
        strIdx = strIdx0;
        symIdx = symIdx0;
        src = src0;
        }
    public Lexem clone(){
        return new Lexem(type,backSymCount,value);
    }
    public String toString(){
        return type+":"+value+"["+backSymCount+"] "+strIdx+"/"+symIdx+" "+src;
        }
    @Override
    public LexState onEvent(char sym) {
        return null; }
}
