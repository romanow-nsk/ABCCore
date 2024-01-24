package romanow.abc.core.entity.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import romanow.abc.core.entity.Entity;

@ToString
@NoArgsConstructor
public class WorkSettingsBase extends Entity {
    @Getter @Setter private String MKVersion="1.0.01";
    @Getter @Setter private String dataServerFileDir="d:/temp";
    @Getter @Setter private boolean dataServerFileDirDefault=true;  // Используется текущий каталог запуска
    @Getter @Setter private boolean convertAtrifact=false;
    @Getter @Setter private String mailHost="mail.nstu.ru";
    @Getter @Setter private String mailBox="romanow@corp.nstu.ru";
    @Getter @Setter private String mailPass="";
    @Getter @Setter private String mailSecur="starttls";
    @Getter @Setter private int mailPort=587;
    @Getter @Setter private String mailToSend="romanow@ngs.ru";
    @Getter @Setter private boolean mailNotifycation=false;
    @Getter @Setter private String nodeName="noname";
    @Getter @Setter private int traceLevel=0;                       // Режим трассировки
    @Getter @Setter private int logDepthInDay=30;                   // Глубина архива в днях
    //------------------------------------------------------------------------------------------------------------------
    public boolean isMainServer(){ return false; }
    public static void main(String ss[]){
        System.out.println(new WorkSettingsBase().toString());
    }
}
