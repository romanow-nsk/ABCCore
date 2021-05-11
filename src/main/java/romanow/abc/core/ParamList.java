package romanow.abc.core;

import java.util.HashMap;

public class ParamList extends HashMap<String,String> {
    public ParamList(){}
    public ParamList add(String name, String value){
        put(name,value);
        return this;
        }
    public ParamList add(String name, int value){
        put(name,""+value);
        return this;
        }
    public ParamList add(String name, boolean bb){
        put(name,""+(bb ? 1 : 0));
        return this;
        }
    public String getParam(String name) throws UniException{
        String ss = get(name);
        if (ss==null)
            throw UniException.user("Нет параметра настроек "+name);
        return ss;
        }
    public int getParamInt(String name) throws UniException{
        String ss = get(name);
        if (ss==null)
            throw UniException.user("Нет параметра настроек "+name);
        try {
            return Integer.parseInt(ss);
            } catch (Exception ee){
                throw UniException.user("Нечисловое значение параметра "+name);
                }
        }
    public boolean getParamBoolean(String name) throws UniException{
        String ss = get(name);
        if (ss==null)
            throw UniException.user("Нет параметра настроек "+name);
        try {
            int vv = Integer.parseInt(ss);
            return vv!=0;
            } catch (Exception ee){
                throw UniException.user("Недопустимое значение параметра "+name);
                }
        }
    public boolean testParam(String name){
        return get(name)!=null;
        }
}
