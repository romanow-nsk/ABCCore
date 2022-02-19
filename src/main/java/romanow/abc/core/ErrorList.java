package romanow.abc.core;

import lombok.Getter;
import lombok.Setter;

public class ErrorList {
    @Getter
    @Setter
    private int errCount=0;
    @Getter @Setter private String info="";
    public void addError(String ss){
        errCount++;
        info+=ss+"\n";
    }
    public void clear(){
        errCount=0;
        info="";
    }
    public boolean valid(){ return errCount==0; }
    public ErrorList addError(ErrorList two){
        errCount += two.errCount;
        if (two.info.length()!=0)
            info+=two.info;
        return this;
    }
    public ErrorList addInfo(String ss){
        info+=ss+"\n";
        return this;
    }
    public String toString(){
        return (errCount ==0 ? "" : "Ошибок: "+errCount+"\n")+info;
    }

}
