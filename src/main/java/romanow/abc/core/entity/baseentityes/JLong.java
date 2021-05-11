package romanow.abc.core.entity.baseentityes;

import com.google.gson.Gson;
import romanow.abc.core.mongo.DAO;

public class JLong extends DAO {
    private long value=0;
    public JLong(){}
    public JLong(long val){
        value = val;
        }
    public long getValue(){ return value; }
    public void setValue(long value) {
        this.value = value;
        }
    public String toString(){ return ""+value; }
    public static void main(String ss[]){
        System.out.println(new Gson().toJson(new JLong(123)));
        System.out.println(new Gson().toJson(new JEmpty()));
    }
}
