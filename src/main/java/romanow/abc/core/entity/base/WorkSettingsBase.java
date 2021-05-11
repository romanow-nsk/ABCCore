package romanow.abc.core.entity.base;

import romanow.abc.core.entity.Entity;

public class WorkSettingsBase extends Entity {
    private String MKVersion="1.0.01";
    private String dataServerFileDir="d:/temp";
    private boolean dataServerFileDirDefault=true;  // Используется текущий каталог запуска
    private boolean convertAtrifact=false;
    private String mailHost="mail.nstu.ru";
    private String mailBox="romanow@corp.nstu.ru";
    private String mailPass="";
    private String mailSecur="starttls";
    private int mailPort=587;
    private String mailToSend="romanow@ngs.ru";
    private boolean mailNotifycation=false;
    private String nodeName="noname";
    public WorkSettingsBase(){}
    //------------------------------------------------------------------------------------------------------------------
    public String getNodeName() {
        return nodeName; }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName; }
    public boolean isDataServerFileDirDefault() {
        return dataServerFileDirDefault; }
    public void setDataServerFileDirDefault(boolean dataServerFileDirDefault) {
        this.dataServerFileDirDefault = dataServerFileDirDefault; }
    public String getMKVersion() {
        return MKVersion; }
    public void setMKVersion(String MKVersion) {
        this.MKVersion = MKVersion; }
    public boolean isConvertAtrifact() {
        return convertAtrifact; }
    public void setConvertAtrifact(boolean convertAtrifact) {
        this.convertAtrifact = convertAtrifact; }
    public void setDataServerFileDir(String dataServerFileDir) {
        this.dataServerFileDir = dataServerFileDir; }
    public String getDataServerFileDir() {
        return dataServerFileDir; }
    public String getMailHost() {
        return mailHost; }
    public void setMailHost(String mailHost) {
        this.mailHost = mailHost; }
    public String getMailBox() {
        return mailBox; }
    public void setMailBox(String mailBox) {
        this.mailBox = mailBox; }
    public String getMailPass() {
        return mailPass; }
    public void setMailPass(String mailPass) {
        this.mailPass = mailPass; }
    public String getMailSecur() {
        return mailSecur; }
    public void setMailSecur(String mailSecur) {
        this.mailSecur = mailSecur; }
    public int getMailPort() {
        return mailPort; }
    public void setMailPort(int mailPort) {
        this.mailPort = mailPort; }
    public String getMailToSend() {
        return mailToSend; }
    public void setMailToSend(String mailToSend) {
        this.mailToSend = mailToSend; }
    public boolean isMailNotifycation() {
        return mailNotifycation; }
    public void setMailNotifycation(boolean mailNotifycation) {
        this.mailNotifycation = mailNotifycation; }
}
