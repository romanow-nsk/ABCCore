package romanow.abc.core.entity;

public class Option<T extends Entity> extends Entity{
    private boolean valid=false;
    private String message="";
    private T value=null;
    public Option(){}
    public Option(T val){ value=val; valid=true; }
    public Option(String mes){ message=mes; valid=false; }
    public boolean isValid() {
        return valid;
        }
    public String getMessage() {
        return message;
        }
    public T getValue() {
        return value;
        }
    public String toString(){ return valid ? value.toString() : message; }
}
