package romanow.abc.core;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;

public class ServerState extends Entity {
    private boolean serverRun=false;
    private int lastMailNumber=0;                   // Номер последнего письма
    private boolean asteriskMailOn=false;           // Вклчюение asterisk-почты
    private boolean asrteiskDialOn=false;           // Включение asterisk-дозвона
    private boolean locked=false;                   // Блокирован для выполнения админ. операций
    private int requestNum=0;                       // Кол-во обрабатываемых запросов
    private int sessionCount=0;                     // Кол-во активных сессий
    private long timeSum=0;
    private long timeMin=0;
    private long timeMax=0;
    private long timeCount=0;
    private long pid=0;
    private int releaseNumber = 0;
    private int totalGetCount=0;                    // Количество операций чтения
    private int cashGetCount=0;                     // Количество операций чтения из кэша
    private boolean сashEnabled=false;             // Режим кэширования
    public boolean isСashEnabled() {
        return сashEnabled; }
    public void setСashEnabled(boolean сasheEnabled) {
        this.сashEnabled = сasheEnabled; }
    public synchronized int getSessionCount() {
        return sessionCount; }
    public synchronized void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount; }
    public synchronized boolean isLocked() {
        return locked; }
    public synchronized void setLocked(boolean locked) {
        this.locked = locked; }
    public synchronized int getRequestNum() {
        return requestNum; }
    public synchronized void incRequestNum() {
        requestNum++; }
    public synchronized void decRequestNum() {
        requestNum--;
        if (requestNum<0) requestNum=0;
        }
    public void addTimeStamp(long tt) {
        if (timeCount == 0 || tt > timeMax)
            timeMax = tt;
        if (timeCount == 0 || tt < timeMin)
            timeMin = tt;
        timeSum += tt;
        timeCount++;
        }
    public long getPID() {
        return pid;
        }
    public void setPid() {
        pid = Utils.getPID();
        releaseNumber = ValuesBase.env().releaseNumber();
        }
    public long getTimeMin() {
        return timeMin; }
    public long getTimeMax() {
        return timeMax; }
    public long getTimeCount() {
        return timeCount; }
    public long getTimeMiddle() {
        return timeCount==0 ? 0 : timeSum/timeCount; }
    public ServerState(){}
    public void init(){
        timeCount=0;
        timeSum=0;
        timeMax=0;
        timeMin=0;
        }
    public boolean isAsteriskMailOn() {
        return asteriskMailOn; }
    public void setAsteriskMailOn(boolean asteriskMailOn) {
        this.asteriskMailOn = asteriskMailOn; }
    public boolean isAsrteiskDialOn() {
        return asrteiskDialOn; }
    public void setAsrteiskDialOn(boolean asrteiskDialOn) {
        this.asrteiskDialOn = asrteiskDialOn; }
    public int getLastMailNumber() {
        return lastMailNumber; }
    public void setLastMailNumber(int lastMailNumber) {
        this.lastMailNumber = lastMailNumber; }
    public boolean isServerRun() {
        return serverRun; }
    public void setServerRun(boolean serverRun) {
        this.serverRun = serverRun; }
    public int getReleaseNumber() {
        return releaseNumber; }
    public String toString(){
        return "Сервер="+serverRun+" Почта="+asteriskMailOn+" Дозвон="+asrteiskDialOn+" Писем="+lastMailNumber;
        }
    public int getTotalGetCount() {
        return totalGetCount; }
    public void setTotalGetCount(int totalGetCount) {
        this.totalGetCount = totalGetCount; }

    public int getCashGetCount() {
        return cashGetCount;
    }

    public void setCashGetCount(int cashGetCount) {
        this.cashGetCount = cashGetCount;
    }
}
