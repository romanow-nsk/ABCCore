package romanow.abc.core.entity.baseentityes;

import romanow.abc.core.mongo.DAO;

public class JAnswer extends DAO {
    private boolean success=true;
    private String message="";
    public JAnswer(){}
    public JAnswer(boolean bb, String ss){
        success = bb;
        message = ss;
        }
    public boolean success() {
        return success;
        }
    public String message() {
        return message;
        }
}
