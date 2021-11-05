package romanow.abc.core.constants;

import romanow.abc.core.ServerState;
import romanow.abc.core.UniException;
import romanow.abc.core.entity.EntityIndexedFactory;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.artifacts.ReportFile;
import romanow.abc.core.entity.base.BugMessage;
import romanow.abc.core.entity.base.HelpFile;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.contacts.Contact;
import romanow.abc.core.entity.contacts.Mail;
import romanow.abc.core.entity.contacts.Phone;
import romanow.abc.core.entity.contacts.PhoneList;
import romanow.abc.core.entity.notifications.NTMessage;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.Person;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.help.HelpFactory;
import romanow.abc.core.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ValuesBase {
    protected static I_Environment env=null;                // Окружение приложения
    public static I_Environment env(){
        return env;
        }
    public final static EntityIndexedFactory EntityFactory = new EntityIndexedFactory();
    public final static ConstMap constMap = new ConstMap();
    public static String title(String group,int cid){
        return constMap.title(group,cid);
        }
    public static ArrayList<ConstValue> title(String group){  return constMap.getValuesList(group); }
    //------------------------------------------------------------------------------------------------------
    private final static int abcReleaseNumber=1;
    public final static String week[] = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    public final static String mnt[] = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    public final static String dataServerIP = "localhost";
    public final static int dataServerPort = 4567;
    public final static String ClientVersion = "1.0.01";        // Версия ПО
    public final static String ServerVersion = "1.0.01";        // Версия ПО
    public final static String dataServerFileDir = "d:/temp";
    public final static String webServerWebLocation = "d:/temp/webserver";
    public final static String mongoStartCmd = "\"c:/Program Files/MongoDB/Server/3.0/bin/mongod\" --dbpath " + dataServerFileDir + "/mongo";
    public final static int FileBufferSize = 8192;              // РАзмер буфера передачи файла
    public final static int FTPFileNotFound = -1;
    public final static int mongoServerPort = 27017;
    public final static String mongoServerIP = "127.0.0.1";
    public final static String DeployScriptName="deploy";

    public final static String GeoCoderCity = "Новосибирск";
    public final static int ConsoleLogSize = 1000;              // Количество строк лога
    public final static int CKeepALiveTime=10;                  // Интервал времени проверки соединения
    public final static String DebugTokenPass="pi31415926";     // Пароль отладочного токена
    public final static int PopupListMaxSize=25;                // Максимальный размер выпадающего списка
    public final static int ServerRebootDelay=10;               // Задержка сервера при перезагрузке
    public final static int HTTPTimeOut=60;                     // Тайм-аут клиента
    public final static int BackgroundOperationMaxDelay=300;    //
    public final static int MongoDBType=0;
    public final static int MongoDBType36=1;
    public final static int FatalExceptionStackSize=20;         // Стек вызовов при исключении
    public final static int PopupMessageDelay=6;                // Тайм-аут всплывающего окна
    public final static int PopupLongDelay=20;                  // Тайм-аут всплывающего окна

    public final static String SQLDBUser="UnityDataServer";
    public final static String SQLDBPass="Fireplace311";
    public final static String SQLDBIP="localhost";
    public final static String SQLDBPort="3306";
    public final static String SQLDBPort2="3307";
    public final static String bashPath="/bin/";                // путь к bash для Linux
    public final static User superUser=new User(ValuesBase.UserSuperAdminType, "Система", "", "", "UnityDataserver", "pi31415926","9130000000");

    public static void init(){
        try {
            Class cc = env().applicationClass(ClassNameValues);
            constMap.createConstList(cc);
            } catch (UniException ee) {
                System.out.println("Ошибка построения карты констант "+ee);
                }
        }
    //---------------------------------------------------------------------------------------------------
    public final static int ClassNameValues = 0;
    public final static int ClassNameWorkSettings = 1;
    public final static int ClassNameDataServer = 2;
    public final static int ClassNameConsoleServer = 3;
    public final static int ClassNameCabinet = 4;
    public final static int ClassNameClient = 5;
    public final static int ClassNameConsoleClient = 6;
    public final static int ClassNameKioskClient = 7;
    private  final static String abcClassNames[]={
            "romanow.abc.core.constants.ValuesBase",
            "romanow.abc.core.entity.base.WorkSettingsBase",
            "romanow.abc.dataserver.DataServer",
            "romanow.abc.dataserver.ConsoleServer",
            "romanow.abc.desktop.Cabinet",
            "romanow.abc.desktop.Client",
            "",""};
    public final static int AppNameSubjectArea = 0;
    public final static int AppNameDBName = 1;
    public final static int AppNameDBUser = 2;
    public final static int AppNameDBPass = 3;
    public final static int AppNameAPK = 4;
    public final static int AppNameServerJar = 5;
    public final static int AppNameModulePackage = 6;
    public final static int AppNameIconPath = 7;
    public final static int AppNameTitle = 8;
    private  final static String abcAppNames[]={
        "ABCEmpty",
        "abc",
        "abc",
        "abc",
        "ABSEmptyClient.apk",
        "ABCDataserver.jar",
        "romanow.abc.desktop.module",
        "/drawable/lecture.png",
        "Пустой фреймворк ABC"
        };
    protected static Class createApplicationClass(int type, String names[]) throws UniException {
            if (type<0 || type>=names.length)
                throw UniException.bug("Ошибка создания системного класса: индекс="+type);
        try {
            Class cc = Class.forName(names[type]);
            return cc;
            } catch (ClassNotFoundException e) {
                throw UniException.bug("Ошибка создания системного класса: не найден "+names[type]);
                }
            }
    protected static Object createApplicationObject(int type, String names[]) throws UniException {
            Class cc = createApplicationClass(type,names);
        try {
            return cc.newInstance();
            } catch (Exception e) {
                throw UniException.bug("Ошибка создания объекта класса "+names[type]+": "+e.toString());
                }
            }
    //---------------------------------------------------------------------------------------------------
    static {
        env = new I_Environment() {
            @Override
            public User superUser() {
                return new User(ValuesBase.UserSuperAdminType, "Система", "", "", "ABCDataserver", "pi31415926","9130000000");
                }
            @Override
            public Class applicationClass(int classType) throws UniException {
                return createApplicationClass(classType,abcClassNames);
                }
            @Override
            public Object applicationObject(int classType) throws UniException {
                return createApplicationObject(classType,abcClassNames);
                }

            @Override
            public String applicationName(int nameNype) {
                return abcAppNames[nameNype];
                }
            @Override
            public String applicationClassName(int classType) {
                return abcClassNames[classType];
                }
            @Override
            public int releaseNumber() { return abcReleaseNumber; }
            @Override
            public WorkSettingsBase currentWorkSettings() { return new WorkSettingsBase(); }
            };
        //--------------------------------------------------------------------------------------------------------------
        EntityFactory.put(new TableItem("Настройки_0", WorkSettingsBase.class));
        EntityFactory.put(new TableItem("Метка GPS", GPSPoint.class));
        EntityFactory.put(new TableItem("Адрес", Address.class));
        EntityFactory.put(new TableItem("Артефакт", Artifact.class));
        EntityFactory.put(new TableItem("Пользователь", User.class));
        EntityFactory.put(new TableItem("Уведомление", NTMessage.class)
                .add("type").add("state").add("r_timeInMS").add("s_timeInMS")
                .add("userSenderType").add("userReceiverType").add("param"));   // 651 - в БД
        EntityFactory.put(new TableItem("Улица", Street.class));
        EntityFactory.put(new TableItem("Нас.пункт", City.class));
        EntityFactory.put(new TableItem("Ошибка", BugMessage.class));
        EntityFactory.put(new TableItem("Персоналия", Person.class));
        EntityFactory.put(new TableItem("Отчет", ReportFile.class));
        EntityFactory.put(new TableItem("Состояние", ServerState.class));
        EntityFactory.put(new TableItem("Подсказка", HelpFile.class));                     // Сборка 586
        EntityFactory.put(new TableItem("Контакт", Contact.class,false));          // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Спец.файла", FileNameExt.class,false));   // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Дата/время", OwnDateTime.class,false));   // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("E-mail", Mail.class,false));              // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Телефон", Phone.class,false));            // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Телефоны", PhoneList.class,false));       // Сборка 623 - не таблица
        EntityFactory.put(new TableItem("Сумма", MoneySum.class,false));           // Сборка 636
        EntityFactory.put(new TableItem( "Аккаунт", Account.class));                        // Сборка 637
        }
    //------------- Префикся для встроенных объектов ------------------------------------------
    public final static HashMap<String,String> PrefixMap = new HashMap<>();
    static  {
        PrefixMap.put("BugMessage.date","d");               //636
        PrefixMap.put("Artifact.date","d");                 //636
        PrefixMap.put("Artifact.original","f");             //636
        PrefixMap.put("Person.phone","p");                  //636
        PrefixMap.put("Person.mail","m");                   //636
        PrefixMap.put("User.phone","p");                    //636
        PrefixMap.put("User.mail","m");                     //636
        PrefixMap.put("GPSPoint.gpsTime","a");              //636
        PrefixMap.put("Street.location","s");               //636
        PrefixMap.put("Account.loginPhone","p");            //636
        PrefixMap.put("AccountData.loginPhone","p");        //637
        PrefixMap.put("NTMessage.sndTime","s");             //651
        PrefixMap.put("NTMessage.recTime","r");             //651
        PrefixMap.put("Street.location","s");               //636
        }
    //------------- Аутентификация и сессия ---------------------------------------------------
    public final static int SessionTokenLength = 32;                // Размер сессионного ключа
    public final static int SessionSilenceTime = 30 * 60;           // Время молчания до разрыва сессии (сек)
    public final static int SessionCycleTime = 30;                  // Цикл проверки сессией (сек)
    public final static int ClockCycleTime = 30;                    // Цикл проверки событий (процессы) (сек)
    public final static String SessionHeaderName = "SessionToken";  // Параметр заголовка - id сессии
    public final static String JWTSessionSecretKey = "FireFighterTopSecret";
    public final static boolean JWTSessionMode = true;
    //------------  Режим чтения таблиц ----------------------------------------------------
    public final static int GetAllModeActual = 0;                   // Только актуальные
    public final static int GetAllModeTotal = 1;                    // Все
    public final static int GetAllModeDeleted = 2;                  // Удаленные
    public final static int DefaultNoAutoAPILevel = 3;              // Уровень загрузки по умолчанию для функций API (не автоматические)
    //------------------------ Типы улиц и нас пунктов -------------------------------------------------
    @CONST(group = "TownType", title = "Город")
    public final static int CTown = 1;
    @CONST(group = "TownType", title = "Поселок")
    public final static int CCountry = 2;
    public final static String TypesCity[] = {"", "г.", "пос."};
    @CONST(group = "StreetType", title = "Улица")
    public final static int SStreet = 0x10;
    @CONST(group = "StreetType", title = "Проспект")
    public final static int SProspect = 0x20;
    @CONST(group = "StreetType", title = "Шоссе")
    public final static int SWay = 0x30;
    @CONST(group = "StreetType", title = "Площадь")
    public final static int SPlace = 0x40;
    public final static String TypesStreet[] = {"", "ул.", "пр.", "ш.","пл."};
    @CONST(group = "HomeType", title = "Дом")
    public final static int HHome = 0x100;
    @CONST(group = "HomeType", title = "Корпус")
    public final static int HCorpus = 0x200;
    public final static String TypesHome[] = {"", "д.", "корп."};
    @CONST(group = "OfficeType", title = "Офис")
    public final static int OOffice = 0x1000;
    @CONST(group = "OfficeType", title = "Кабинет")
    public final static int OCabinet = 0x2000;
    @CONST(group = "OfficeType", title = "Квартира")
    public final static int OQuart = 0x3000;
    public final static String TypesOffice[] = {"", "оф.", "к.", "кв."};
    //----------------------- Операции над EntityLink при Update (Put)---------------------------
    @CONST(group = "Operation", title = "Нет операции")
    public final static int OperationNone = 0;
    @CONST(group = "Operation", title = "Добавить")
    public final static int OperationAdd = 1;     // Добавить ref и записать oid
    @CONST(group = "Operation", title = "Изменить")
    public final static int OperationUpdate = 2;  // Обновить по ref
    public final static boolean DeleteMode = false;
    public final static boolean UndeleteMode = true;
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Гость")
    public final static int UserGuestType = 0;
    @CONST(group = "User", title = "Суперадмин")
    public final static int UserSuperAdminType = 1;
    @CONST(group = "User", title = "Администратор")
    public final static int UserAdminType = 2;
    //------------------- Вид уведомления  -------------------------------------------------------------------------
    @CONST(group = "NotificationType", title = "Не определено")
    public final static int NTUndefined = 0;
    @CONST(group = "NotificationType", title = "Управление клиентом")       // Внешнее управление клиентом - param
    public final static int NTUserAction = 1;
    @CONST(group = "NotificationType", title = "Действие")                  // Действие над объектом (class,id)
    public final static int NTObjectAction = 2;
    @CONST(group = "NotificationType", title = "Изменение состояния")       // Изменение сорстояния клиента - источника
    public final static int NTUserStateChanged = 3;
    @CONST(group = "NotificationType", title = "Сообщение")                 // Сообщение от клиента - источника
    public final static int NTUserMessage = 4;
    @CONST(group = "NotificationType", title = "Событие")                   // Событие на объекте (class,id)
    public final static int NTObjectEvent = 5;
    @CONST(group = "NotificationType", title = "Система")                   //
    public final static int NTSystemEvent = 6;
    //------------------- Состояние уведомлнния  -------------------------------------------------------------------------
    @CONST(group = "NotificationState", title = "Не определено")
    public final static int NSUndefined = 0;
    @CONST(group = "NotificationState", title = "Передано")
    public final static int NSSend = 1;
    @CONST(group = "NotificationState", title = "Просмотрено")
    public final static int NSReceived = 2;
    @CONST(group = "NotificationState", title = "В работе")
    public final static int NSInProcess = 3;
    @CONST(group = "NotificationState", title = "Закрыто")
    public final static int NSClosed = 4;
    //--------------------------------------------------------------------------------------------------
    public final static int GeoNone = 0;                  // Координаты недоступны
    public final static int GeoNet = 1;                   // Координаты от сети (вышек)
    public final static int GeoGPS = 2;                   // Координаты от GPS
    //--------------------------------------------------------------------------------------------------
    public final static int HTTPOk = 200;
    public final static int HTTPAuthorization = 401;
    public final static int HTTPNotFound = 404;
    public final static int HTTPRequestError = 400;
    public final static int HTTPException = 500;
    public final static int HTTPServiceUnavailable = 503;  // Сервис недоступен
    //----------------------------------------------------------------------
    public final static int UndefinedType = 0;            // Неопределенный тип
    //----------------- Типы артефактов-------------------------------------
    @CONST(group = "ArtifactType", title = "Не определен")
    public final static int ArtifactNoType = 0;
    @CONST(group = "ArtifactType", title = "Фото")
    public final static int ArtifactImageType = 1;
    @CONST(group = "ArtifactType", title = "Видео")
    public final static int ArtifactVideoType = 2;
    @CONST(group = "ArtifactType", title = "Аудио")
    public final static int ArtifactAudioType = 3;
    @CONST(group = "ArtifactType", title = "Текст")
    public final static int ArtifactTextType = 4;
    @CONST(group = "ArtifactType", title = "Документ")
    public final static int ArtifactDocType = 5;
    @CONST(group = "ArtifactType", title = "Прочее")
    public final static int ArtifactOtherType = 6;
    public final static String ArtifactDirNames[] = {"-----", "Image", "Video", "Audio", "Text", "Document", "Other"};
    public final static HashMap<String,String> ConvertList = new HashMap<String,String>();
        static {
            ConvertList.put("3gp","mpg");
            }
    //-------------------- Тип сохраняемого отчета --------------------------------------------------------------------
    @CONST(group = "ReportType", title = "Нет")
    public final static int ReportNO = 0;
    @CONST(group = "ReportType", title = "pdf")
    public final static int ReportPDF = 1;
    @CONST(group = "ReportType", title = "xms")
    public final static int ReportXML = 2;
    @CONST(group = "ReportType", title = "html")
    public final static int ReportHTML = 3;
    //----------------------- Типы выполнения уведомлений ---------------------------------------------
    @CONST(group = "NotificationMode", title = "Принудительно")
    public final static int NMHard = 0;
    @CONST(group = "NotificationMode", title = "После завершения операции")
    public final static int NMDeffered = 1;
    @CONST(group = "NotificationMode", title = "С подтверждением")
    public final static int NMUserAck = 2;
    //-------------------- Словарь подсказок ------------------------------------------------------------------------
    @CONST(group = "HelpTopic", title = "Уведомление")
    public final static int HelpNotify = 1;
    @CONST(group = "HelpTopic", title = "Аудио")
    public final static int HelpAudio = 2;
    @CONST(group = "HelpTopic", title = "Видео")
    public final static int HelpVideo = 3;
    @CONST(group = "HelpTopic", title = "Фото")
    public final static int HelpPhoto = 4;
    @CONST(group = "HelpTopic", title = "GPS")
    public final static int HelpGPS = 5;
    @CONST(group = "HelpTopic", title = "Общее")
    public final static int HelpCommon = 6;
    @CONST(group = "HelpTopic", title = "Сеть")
    public final static int HelpNetwork = 7;
    @CONST(group = "HelpTopic", title = "Настройки")
    public final static int HelpSettings = 8;
    //----------------------- Отчеты  ---------------------------------------------
    @CONST(group = "Report", title = "Прочее")
    public final static int RepOther = 0;
    //------------------------ Препарирования БД --------------------------------------
    @CONST(group = "DBOperation", title = "Очистка контента (заявки)")
    public final static int DBOClearContent1 = 0;
    @CONST(group = "DBOperation", title = "Обратные ссылки")
    public final static int DBOBackLinks = 1;
    @CONST(group = "DBOperation", title = "Сжать таблицы")
    public final static int DBOSquezzyTables = 2;
    @CONST(group = "DBOperation", title = "Обновить поля")
    public final static int DBORefreshFields = 3;
    @CONST(group = "DBOperation", title = "Сбор мусора")
    public final static int DBOCollectGarbage = 4;
    @CONST(group = "DBOperation", title = "Наличие артефактов")
    public final static int DBOTestArtifacts = 5;
    @CONST(group = "DBOperation", title = "Тест - задержка 60 сек")
    public final static int DBOTestDelay = 7;
    //------------- Уровни событий -----------------
    @CONST(group = "EventLevel", title = "Информация")
    public final static int ELInfo=0;
    @CONST(group = "EventLevel", title = "Предупреждение")
    public final static int ELWarning=1;
    @CONST(group = "EventLevel", title = "Ошибка")
    public final static int ELError=2;
    @CONST(group = "EventLevel", title = "Авария")
    public final static int ELFailure=3;
    @CONST(group = "EventLevel", title = "Крах")
    public final static int ELCrash=4;
    //------------- Типы и подтипы событий -----------------
    @CONST(group = "EventType", title = "Не определено")
    public final static int EventNone=0;            //
    @CONST(group = "EventType", title = "Уведомление")
    public final static int EventNotifycation=1;    // Уведомление
    @CONST(group = "EventType", title = "Событие сервера")
    public final static int EventHostServer=2;      // Событие сервера СУ АГЭУ
    @CONST(group = "EventType", title = "Внешнее событие")
    public final static int EventExternal=3;        // Внешнее событие
    @CONST(group = "EventType", title = "Системное")
    public final static int EventSystem=4;          // Системное - сервер данных
    //------------------------ Источники артефактов ------------------------------------------------------
    public final static String ArtifactParentList[] = {"ReportFile", "User"};
    //---------------------  Типы данных ЯОП -------------------------------------------------------------
    @CONST(group = "DType", title = "void")
    public final static int DTVoid=0;
    @CONST(group = "DType", title = "short")
    public final static int DTShort=1;
    @CONST(group = "DType", title = "int")
    public final static int DTInt=2;
    @CONST(group = "DType", title = "long")
    public final static int DTLong=3;
    @CONST(group = "DType", title = "float")
    public final static int DTFloat=4;
    @CONST(group = "DType", title = "double")
    public final static int DTDouble=5;
    @CONST(group = "DType", title = "boolean")
    public final static int DTBoolean=6;
    @CONST(group = "DType", title = "char")
    public final static int DTChar=7;
    @CONST(group = "DType", title = "date")
    public final static int DTDateTime=8;
    @CONST(group = "DType", title = "array")
    public final static int DTArray=9;
    //---------------------  Операции ЯОП -----------------------------------------------------------------
    @CONST(group = "Operation", title = "nop")
    public final static int ONOP=0;
    @CONST(group = "Operation", title = "add")
    public final static int OAdd=1;
    @CONST(group = "Operation", title = "sub")
    public final static int OSub=2;
    @CONST(group = "Operation", title = "mul")
    public final static int OMul=3;
    @CONST(group = "Operation", title = "div")
    public final static int ODiv=4;
    @CONST(group = "Operation", title = "mod")
    public final static int OMod=5;
    @CONST(group = "Operation", title = "and")
    public final static int OAnd=6;
    @CONST(group = "Operation", title = "or")
    public final static int OOr=7;
    @CONST(group = "Operation", title = "not")
    public final static int ONot=8;
    @CONST(group = "Operation", title = "eq")
    public final static int OEqual=9;
    @CONST(group = "Operation", title = "neq")
    public final static int ONoEqual=10;
    @CONST(group = "Operation", title = "le")
    public final static int OLE=11;
    @CONST(group = "Operation", title = "lt")
    public final static int OLT=12;
    @CONST(group = "Operation", title = "gt")
    public final static int OGT=13;
    @CONST(group = "Operation", title = "ge")
    public final static int OGE=14;
    @CONST(group = "Operation", title = "jmp")
    public final static int OJmp=15;
    @CONST(group = "Operation", title = "jfalse")
    public final static int OJmpFalse=16;
    @CONST(group = "Operation", title = "jtrue")
    public final static int OJmpTrue=17;
    @CONST(group = "Operation", title = "return")
    public final static int OReturn=18;
    @CONST(group = "Operation", title = "push")
    public final static int OPush=19;
    @CONST(group = "Operation", title = "pushVar")
    public final static int OPushVar=20;
    @CONST(group = "Operation", title = "pop")
    public final static int OPop=21;
    @CONST(group = "Operation", title = "pow")
    public final static int OPow=22;
    //------------------- Типы ошибок ЯОП -----------------------------------------------------------------
    @CONST(group = "SEType", title = "Не определено")
    public final static int SETypeUndef=0;
    @CONST(group = "SEType", title = "Компиляция")
    public final static int SETypeCompile=1;
    @CONST(group = "SEType", title = "Связывание")
    public final static int SETypeLink=2;
    @CONST(group = "SEType", title = "Исполнение")
    public final static int SETypeRunTime=2;
    @CONST(group = "SEMode", title = "Информация")
    public final static int SEModeInfo=0;
    @CONST(group = "SEMode", title = "Предупреждение")
    public final static int SEModeWarning=1;
    @CONST(group = "SEMode", title = "Ошибка")
    public final static int SEModeError=2;
    @CONST(group = "SEMode", title = "Крах")
    public final static int SEModeFatal=3;
    //------------------------ Коды ошибок runtime ---------------------------------------------------------
    @CONST(group = "SRError", title = "Переполнение стека")
    public final static int SREStackOver=1;
    @CONST(group = "SRError", title = "Стек пуст")
    public final static int SREStackEmpty=2;
    @CONST(group = "SRError", title = "Выход за границы стека")
    public final static int SREStackLimits=3;
    @CONST(group = "SRError", title = "Формат целого")
    public final static int SREIntFormat=4;
    @CONST(group = "SRError", title = "Форматирование целого")
    public final static int SREIntOutFormat=5;
    @CONST(group = "SRError", title = "Недопустимая операция для ТД")
    public final static int SREIllegalOperation=6;
    @CONST(group = "SRError", title = "Ошибка конвертации float/int")
    public final static int SREFloatConvertation=7;
    @CONST(group = "SRError", title = "Недопустимое сравнение для ТД")
    public final static int SREIllegalCompare=8;
    @CONST(group = "SRError", title = "Переменная не определена")
    public final static int SREIllegalVariable=9;
    @CONST(group = "SRError", title = "Недопустимый ТД")
    public final static int SREIllegalDT=10;
    //--------------------------Коды ошибок компиляции ----------------------------------------------------
    @CONST(group = "SCError", title = "Недопустимый код ошибки")
    public final static int SCENoCode=0;
    @CONST(group = "SCError", title = "Не найден конец текста")
    public final static int SCENoEOF=1;
    @CONST(group = "SCError", title = "Пропущего имя переменной ")
    public final static int SCENoVarName=2;
    @CONST(group = "SCError", title = "Переменная не определена")
    public final static int SCEVarNotDef=3;
    @CONST(group = "SCError", title = "Повторное определение переменной")
    public final static int SCEVarMultiply=4;
    @CONST(group = "SCError", title = "Ошибка списка переменных")
    public final static int SCEVarListFormat=5;
    @CONST(group = "SCError", title = "Не найден символ")
    public final static int SCELexemLost=6;
    @CONST(group = "SCError", title = "Недопустимый оператор")
    public final static int SCEIllegalOperator=7;
    @CONST(group = "SCError", title = "Недопустимое условие")
    public final static int SCEIllegalCondition=8;
    @CONST(group = "SCError", title = "Формат константы")
    public final static int SCEConstFormat=9;
    @CONST(group = "SCError", title = "Синтаксическая ошибка")
    public final static int SCEIllegalSyntax=10;
    //-----------------------------------------------------------------------------------------------------
    public static void main(String a[]){
        ValuesBase.init();
        System.out.println(ValuesBase.title("User",ValuesBase.UserAdminType));
        System.out.print(ValuesBase.constMap.toString());
        }
}
