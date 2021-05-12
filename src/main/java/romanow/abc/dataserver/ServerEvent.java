package romanow.abc.dataserver;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.OwnDateTime;

public class ServerEvent extends Entity {      // Общий класс для всех аварий, событий....
    private int type=ValuesBase.EventNone;          // Тип события
    private int level=0;                        // Уровень события
    private String title="";                    // Заголовок
    private String comment="";                  // Содержание
    private OwnDateTime arrivalTime = new OwnDateTime();     // Время наступления
    private OwnDateTime endTime = new OwnDateTime(false);// Время окончания
    private EntityLink<Artifact> artifact = new EntityLink<>(Artifact.class);
    private EntityLink<User> user = new EntityLink<>(); // Пользователь, связанный с событием
    public ServerEvent(){}
    public ServerEvent(int type, String title) {
        this.type = type;
        this.title = title;
        }
    public int getType() {
        return type; }
    public int getLevel() {
        return level; }
    @Override
    public String getTitle() {
        return title; }
    public String getComment() {
        return comment; }
    public ServerEvent(int type, int level, String title, String comment) {
        this.level = level;
        this.type = type;
        this.title = title;
        this.comment = comment;
        }
    public ServerEvent setUserId(long oid) {
        user.setOid(oid);
        return this; }
    public ServerEvent setType(int type) {
        this.type = type;
        return this; }
    public ServerEvent setLevel(int level) {
        this.level = level;
        return this; }
    public ServerEvent setTitle(String title) {
        this.title = title;
        return  this; }
    public ServerEvent setComment(String comment) {
        this.comment = comment;
        return this; }
    public OwnDateTime getArrivalTime() {
        return arrivalTime; }
    public ServerEvent setArrivalTime(OwnDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this; }
    public OwnDateTime getEndTime() {
        return endTime; }
    public ServerEvent setEndTime(OwnDateTime endTime) {
        this.endTime = endTime;
        return this; }
    public ServerEvent setEndTime() {
        this.endTime = new OwnDateTime();
        return this; }
    public EntityLink<User> getUser() {
        return user; }
    public String toString(){
        return ValuesBase.title("EventType",type)+"["+ValuesBase.title("EventLevel",level)+"] "+title+(comment.length()!=0 ? "\n"+comment : "");
        }
    public EntityLink<Artifact> getArtifact() {
        return artifact; }
    public ServerEvent setAtrifact(long artId){
        artifact.setOid(artId);
        return this;
    }
}
