package romanow.abc.core.entity.notifications;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.OwnDateTime;

public class NTMessage extends Entity {
    private EntityLink<User> user = new EntityLink<>(User.class);           // Для техника, id по таблице USERов
    private EntityLink<Artifact> artifact=new EntityLink<>(Artifact.class); // Связанный артефакт
    private String message="";                                  // Текст сообщения
    private String header="";                                   // Заголовок сообщения
    private int userSenderType= ValuesBase.UndefinedType;       // Тип (роль) отправителя
    private int userReceiverType= ValuesBase.UndefinedType;     // Тип (роль) приемника
    private long param=0;                                       // id сущности или индекс формы для МК
    private String entityName="";                               // 656 - имя класса прикрепленного объекта
    private OwnDateTime sndTime=new OwnDateTime();              // Дата/время отправки
    private OwnDateTime recTime=new OwnDateTime();              // Дата/время получения
    private int executeMode= ValuesBase.NMUserAck;              // Важное (не сбрасывается при просмотре, выполняется сразу)
    private int state= ValuesBase.NSSend;                       // Состояние приема
    private int type= ValuesBase.UndefinedType;                 // Тип уведомления
    public NTMessage(NTMessage two){
        user = two.user;
        artifact = two.artifact;
        message = two.message;
        header = two.header;
        userSenderType = two.userSenderType;
        userReceiverType = two.userReceiverType;
        param = two.param;
        executeMode = two.executeMode;
        type = two.type;
        state = two.state;
        user.setOid(two.getUser().getOid());
        artifact.setOid(two.getArtifact().getOid());
        }
    public String getTitle(){
        // 656 String ss = user.getTitle()+" "+ ValuesBase.NTypes[type]+": "+sndTime.timeToString()+" "+header;
        String ss = ValuesBase.title("User",userSenderType)+" "+user.getTitle()+" "+ ValuesBase.title("NotificationType",type)+": "+sndTime.timeToString()+" "+header;
        return ss;
        }
    public String toShortString(){
        return getOid()+" "+ ValuesBase.title("NotificationState",state)+" "+ ValuesBase.title("NotificationType",type);
        }
    public String toString(){
        // 656 String ss = toShortString();
        String ss = toShortString()+ " "+ ValuesBase.title("User",userSenderType)+"-->"+ ValuesBase.title("User",userReceiverType);
        if (user.getOid()!=0) ss+=" "+user.getTitle();
        ss+="\n"+sndTime.timeToString()+" "+header+": "+message;
        return ss;
        }
    public NTMessage(){}
    public NTMessage(int type0, int sndType0, int recType0, long userId, String head, String mes){
        type = type0;
        userSenderType = sndType0;
        userReceiverType = recType0;
        user.setOid(userId);
        header = head;
        message=mes;
        }
    public NTMessage(int type0, int sndType0, int recType0, User uu, String head, String mes){
        type = type0;
        userSenderType = sndType0;
        userReceiverType = recType0;
        user.setOid(uu.getOid());
        header = head;
        message=mes;
        }
    public void setEntity(String name, long id){
        entityName=name;
        param=id;
        }
    public String getEntityName() {
        return entityName; }
    public void setEntityName(String entityName) {
        this.entityName = entityName; }
    public String getHeader() {
        return header; }
    public void setHeader(String header) {
        this.header = header; }
    public EntityLink<User> getUser() {
        return user; }
    public EntityLink<Artifact> getArtifact() {
        return artifact; }
    public String getMessage() {
        return message; }
    public void setMessage(String message) {
        this.message = message; }
    public int getUserSenderType() {
        return userSenderType; }
    public void setUserSenderType(int userSenderType) {
        this.userSenderType = userSenderType; }
    public int getUserReceiverType() {
        return userReceiverType; }
    public void setUserReceiverType(int userReceiverType) {
        this.userReceiverType = userReceiverType; }
    public long getParam() {
        return param; }
    public NTMessage setParam(long dataOid) {
        this.param = dataOid;
        return this; }
    public OwnDateTime getSndTime() {
        return sndTime; }
    public void setSndTime(OwnDateTime sndTime) {
        this.sndTime = sndTime; }
    public OwnDateTime getRecTime() {
        return recTime; }
    public void setRecTime(OwnDateTime recTime) {
        this.recTime = recTime; }
    public int getExecuteMode() {
        return executeMode; }
    public NTMessage setExecuteMode(int mode) {
        executeMode = mode;
        return this; }
    public int getState() {
        return state; }
    public void setState(int state) {
        this.state = state; }
    public int getType() {
        return type; }
    public void setType(int type) {
        this.type = type; }
    //==========================================================================================

}
