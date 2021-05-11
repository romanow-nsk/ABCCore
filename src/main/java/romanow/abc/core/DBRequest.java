package romanow.abc.core;

import com.google.gson.Gson;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.mongo.DAO;

public class DBRequest extends DAO {
    private String className="";
    private String jsonObject="";
    public DBRequest(String className, String jsonObject) {
        this.className = className;
        this.jsonObject = jsonObject;
        }
    public DBRequest(){}
    public DBRequest(Entity ent, Gson gson){
        put(ent,gson);
        }
    public void put(Entity ent, Gson gson){
        className = ent.getClass().getSimpleName();
        jsonObject = gson.toJson(ent);
        }
    public Entity get(Gson gson) throws UniException{
        Class cc = ValuesBase.EntityFactory.getClassForSimpleName(className);
        if (cc==null)
            throw UniException.bug("Illegal class "+className);
        try {
            Entity ent = (Entity) gson.fromJson(jsonObject, cc);
            return ent;
            } catch(Exception ee){
                throw UniException.bug("Illegal object of"+className);
                }
        }
}
