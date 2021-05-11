package romanow.abc.core;

import com.google.gson.Gson;
import romanow.abc.core.entity.Entity;

/*** Сериализатор JSON в потоки сервлета */
public class JSON {
    private Gson gson;
    public JSON(){
        gson = new Gson();
        }
    public Gson gson(){ return gson; }
    public String put(Object oo){
        return oo.getClass().getName()+"$"+gson.toJson(oo)+"\n";
        }
    public String putOnly(Object oo){
        return gson.toJson(oo)+"\n";
    }
    public Object get(String ss) throws UniException{
        int idx = ss.indexOf('$');
        if (idx==-1) throw UniException.bug("Format error");
        String name = ss.substring(0,idx);
        ss = ss.substring(idx+1);
        Object oo = null;
        try {
            Class zz = Class.forName(name);
            oo = gson.fromJson(ss, zz);
            } catch(ClassNotFoundException ee){
                throw UniException.bug("Wrong class "+name);
                }
        return oo;
        }
    public Entity get(String ss,Class zz) throws UniException{
        Object oo = null;
        try {
            oo = gson.fromJson(ss, zz);
            if (!(oo instanceof Entity))
                throw UniException.bug("Wrong class "+zz.getName());
                } catch(Exception ee){
            throw UniException.bug("Wrong class "+zz.getName());
            }
        return (Entity) oo;
        }
    public static void main(String argv[]){
        System.out.println(new Gson().toJson("123"));
    }
}
