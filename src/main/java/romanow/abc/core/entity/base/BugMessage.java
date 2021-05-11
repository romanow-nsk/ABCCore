package romanow.abc.core.entity.base;

import romanow.abc.core.I_ExcelRW;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.mongo.I_MongoRW;
import romanow.abc.core.utils.OwnDateTime;

public class BugMessage extends Entity implements I_MongoRW, I_ExcelRW {
    private EntityLink<User> user = new EntityLink<>(User.class);
    private OwnDateTime date=new OwnDateTime();
    private String message="";
    public BugMessage(long userId, OwnDateTime date, String message) {
        this.user.setOid(userId);
        this.date = date;
        this.message = message;
        }
    public BugMessage(long userId, String message) {
        this.user.setOid(userId);
        this.message = message;
    }    public BugMessage(String message) {
        this.message = message;
        }
    public BugMessage() {}
    public String getTitle(){ return date+ user.getTitle(); }
    public String toString(){ return getTitle()+"\n"+message; }
    //------------------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        date.putData("d_",document,0,null);
    }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res);
        date.getData("d_",res, 0, null);
    }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        date.getData(row, cnt);
    }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        date.putData(row, cnt);
    }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        date.putHeader("d_",list);
        }
    */
    //--------------------------------------------------------------------------------------------------
    public EntityLink<User> getUserId() {
        return user;
        }
    public OwnDateTime getDate() {
        return date;
        }
    public String getMessage() {
        return message;
        }
}
