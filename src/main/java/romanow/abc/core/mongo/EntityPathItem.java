package romanow.abc.core.mongo;

public class EntityPathItem {
    private String className="";    // Имя класса
    private boolean done=false;     // Отметка о прохождении в цепочке
    public EntityPathItem(String className0){
        className=className0;
        }
    public String getClassName() {
        return className; }
    public boolean isDone() {
        return done; }
    public void setDone(boolean done) {
        this.done = done; }
}
