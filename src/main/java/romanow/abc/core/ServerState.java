package romanow.abc.core;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.constants.I_Environment;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;

public class ServerState extends Entity {
    @Getter @Setter private boolean serverRun=false;
    @Getter @Setter private boolean locked=false;                   // Блокирован для выполнения админ. операций
    @Getter @Setter private int requestNum=0;                       // Кол-во обрабатываемых запросов
    @Getter @Setter private int sessionCount=0;                     // Кол-во активных сессий
    @Getter @Setter private long timeSum=0;
    @Getter @Setter private long timeMin=0;
    @Getter @Setter private long timeMax=0;
    @Getter @Setter private long timeCount=0;
    @Getter @Setter private long pid=0;
    @Getter @Setter private int releaseNumber = 0;
    @Getter @Setter private String version="";
    @Getter @Setter private int totalGetCount=0;                   // Количество операций чтения
    @Getter @Setter private int cashGetCount=0;                    // Количество операций чтения из кэша
    @Getter @Setter private boolean сashEnabled=false;             // Режим кэширования
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
    public void setPid() {
        pid = Utils.getPID();
        I_Environment env =  ValuesBase.env();
        releaseNumber = env.releaseNumber();
        version = env.applicationName(ValuesBase.AppNameSubjectArea)+": "+env.applicationName(ValuesBase.AppNameTitle);
        }
    public long getTimeMiddle() {
        return timeCount==0 ? 0 : timeSum/timeCount; }
    public ServerState(){}
    public void init(){
        timeCount=0;
        timeSum=0;
        timeMax=0;
        timeMin=0;
        }
    public String toString(){
        return "Сервер="+serverRun;
        }
}
