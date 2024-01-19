package romanow.abc.core.API;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.DBRequest;
import romanow.abc.core.ErrorList;
import romanow.abc.core.ServerState;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.EntityNamed;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.artifacts.ArtifactList;
import romanow.abc.core.entity.base.BugMessage;
import romanow.abc.core.entity.base.HelpFile;
import romanow.abc.core.entity.base.StringList;
import romanow.abc.core.entity.baseentityes.*;
import romanow.abc.core.entity.notifications.NTMessage;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.Address;
import romanow.abc.core.utils.GPSPoint;
import romanow.abc.core.utils.Pair;

import java.util.ArrayList;

public interface RestAPIBase {
    //======================== API настроек сервера данных
    /** Получить объект настроек сервера данных */
    @GET("/api/worksettings")
    Call<DBRequest> workSettings(@Header("SessionToken") String token);
    /** Получить int-поле настроек по имени */
    @GET("/api/worksettings/get/int")
    Call<JInt> getWorkSettingsInt(@Header("SessionToken") String token, @Query("field") String field);
    /** Получить String-поле настроек по имени */
    @GET("/api/worksettings/get/string")
    Call<JString> getWorkSettingsString(@Header("SessionToken") String token, @Query("field") String field);
    /** Получить boolean-поле настроек по имени */
    @GET("/api/worksettings/get/boolean")
    Call<JBoolean> getWorkSettingsBoolean(@Header("SessionToken") String token, @Query("field") String field);
    /** Обновить объект настроек */
    @POST("/api/worksettings/update")
    Call<JEmpty> updateWorkSettings(@Header("SessionToken") String token, @Body DBRequest workSettings);
    /** Обновить int-поле настроек по имени */
    @POST("/api/worksettings/update/int")
    Call<JEmpty> updateWorkSettings(@Header("SessionToken") String token,@Query("field") String field, @Query("value") int value);
    /** Обновить String-поле настроек по имени */
    @POST("/api/worksettings/update/string")
    Call<JEmpty> updateWorkSettings(@Header("SessionToken") String token,@Query("field") String field, @Query("value") String value);
    /** Обновить boolean-поле настроек по имени */
    @POST("/api/worksettings/update/boolean")
    Call<JEmpty> updateWorkSettings(@Header("SessionToken") String token, @Query("field") String field, @Query("value") boolean value);
    //========================== Универсальный интерфейс бизнес-объектов БД ===================
    /** добавить объект - имя класса-JSON */
    @POST("/api/entity/add")
    Call<JLong> addEntity(@Header("SessionToken") String token, @Body DBRequest body, @Query("level") int level);
    /** обновить объект - имя класса-JSON */
    @POST("/api/entity/update")
    Call<JEmpty> updateEntity(@Header("SessionToken") String token,@Body DBRequest body);
    /** обновить поле объекта имя поля-имя класса-JSON */
    @POST("/api/entity/update/field")
    Call<JEmpty> updateEntityField(@Header("SessionToken") String token,@Query("name") String fieldName, @Body DBRequest body);
    /** получить объект - имя класса-oid */
    @GET("/api/entity/get")
    Call<DBRequest> getEntity(@Header("SessionToken") String token, @Query("classname") String classname, @Query("id") long id, @Query("level") int level);
    /** получить все объекты - имя класса */
    @GET("/api/entity/list")
    Call<ArrayList<DBRequest>> getEntityList(@Header("SessionToken") String token, @Query("classname") String classname, @Query("mode") int mode, @Query("level") int level);
    /** удалить объект - имя класса-oid */
    @POST("/api/entity/remove")
    Call<JBoolean> removeEntity(@Header("SessionToken") String token,@Query("classname") String classname, @Query("id") long id);
    /** получить количество объектов - имя класса */
    @GET("/api/entity/number")
    Call<JInt> getEntityNumber(@Header("SessionToken") String token, @Query("classname") String classname);
    @GET("/api/entity/list/withpaths")  //660
    Call<ArrayList<DBRequest>> getEntityListWithPaths(@Header("SessionToken") String token, @Query("classname") String classname, @Query("mode") int mode, @Query("level") int level,@Query("paths") String paths);
    @GET("/api/entity/get/withpaths")  //660
    Call<DBRequest> getEntityWithPaths(@Header("SessionToken") String token, @Query("classname") String classname, @Query("id") long id,@Query("level") int level,@Query("paths") String paths);
    /** получить последние объекты - имя класса-количество */
    @GET("/api/entity/list/last")
    Call<ArrayList<DBRequest>> getEntityListLast(@Header("SessionToken") String token, @Query("classname") String classname, @Query("number") int number, @Query("level") int level);
    /** получить объекты из диапазона oid */
    @GET("/api/entity/list/byoid")
    Call<ArrayList<DBRequest>> getEntityListByOid(@Header("SessionToken") String token, @Query("classname") String classname, @Query("firstOid") long firstOid, @Query("lastOid") long lastOid, @Query("level") int level);
    /** получить объекты - имя класса-XML-запрос */
    @GET("/api/entity/list/query")
    Call<ArrayList<DBRequest>> getEntityListByQuery(@Header("SessionToken") String token, @Query("classname") String classname, @Query("xmlquery") String xmlQuery, @Query("level") int level);
    /** Установка GPS-координат для адреса */
    @POST("/api/address/setgps")
    Call<JLong> setAddressGPS(@Header("SessionToken") String token, @Query("id") long id, @Body GPSPoint gps);
    //---------------------------- Администрирование ---------------------------------------------------------------------------
    /** ping - запрос клиента */
    @GET("/api/debug/ping")
    Call<JEmpty> ping();
    /** получить отладочный токен по системному паролю */
    @GET("/api/debug/token")
    Call<JString> debugToken(@Query("pass") String pass);
    /** Инициализировать БД сервера */
    @GET("/api/admin/cleardb")
    /** Очистить файлы */
    Call<JString> clearDB(@Header("SessionToken") String token,@Query("pass") String pass);
    @GET("/api/admin/clearfiles")
    Call<JString> clearFiles(@Header("SessionToken") String token,@Query("pass") String pass);
    /** Инициализировать таблицу БД по имени класса бизнес-сущности */
    @GET("/api/admin/cleartable")
    Call<JString> clearTable(@Header("SessionToken") String token,@Query("table") String table, @Query("pass") String pass);
    /** Активность клиента - в ответе - количество неподтвержденных уведомлений */
    @GET("/api/keepalive")
    Call<JInt> keepalive(@Header("SessionToken") String token);
    /** Получить count последних строк лога */
    @GET("/api/debug/consolelog")
    Call<StringList> getConsoleLog(@Header("SessionToken") String token, @Query("count") int count);
    /** Получить лог опросом */
    @GET("/api/debug/consolelog/polling")
    Call<Pair<Long,StringList>> getConsoleLogPolling(@Header("SessionToken") String token, @Query("lastnum") long lastNum);
    /** Добавить объект - описатель бага */
    @POST("/api/bug/add")
    Call<JLong> sendBug(@Header("SessionToken") String token, @Body BugMessage bug);
    /** Получить список багов */
    @GET("/api/bug/list")
    Call<EntityList<BugMessage>> getBugList(@Header("SessionToken") String token, @Query("mode") int mode);
    /** Получить баг по oid */
    @GET("/api/bug/get")
    Call<BugMessage> getBug(@Header("SessionToken") String token, @Query("id") long id);
    /** Экспортировать БД в Excel-файл - артефакт сервера (xls)*/
    @GET("/api/admin/exportdb")
    Call<Artifact> exportDBxls(@Header("SessionToken") String token);
    /** Экспортировать БД в Excel-файл - артефакт сервера (xls/xlsx) */
    @GET("/api/admin/exportdb")
    Call<Artifact> exportDBxlsx(@Header("SessionToken") String token,@Query("xlsx") boolean xlsx, @Query("blocksize") int blocksize);
    /** Экспортировать БД в архив MongoDB */
    @GET("/api/admin/dump")
    Call<Artifact> dump(@Header("SessionToken") String token);
    /** Перезагрузить сервер */
    @POST("/api/admin/reboot")
    Call<JEmpty> rebootServer(@Header("SessionToken") String token, @Query("pass") String pass);
    /** Импортировать БД из Excel-файла - артефакта сервера (oid), ответ - строка лога */
    @POST("/api/admin/importdb")
    Call<JString> importDBxls(@Header("SessionToken") String token, @Query("pass") String pass,@Query("id") long id);
    /** Импортировать БД из архива БД */
    @POST("/api/admin/restore")
    Call<JString> restore(@Header("SessionToken") String token, @Query("pass") String pass,@Query("id") long id);
    /** Обновить сервер загруженным артефактом и перезагрузить */
    @POST("/api/admin/deploy")
    Call<JString> deploy(@Header("SessionToken") String token, @Query("pass") String pass);
    /** Обновить сервер загруженным артефактом, выделить память заданного размера и перезагрузить */
    @POST("/api/admin/deploy")
    Call<JString> deployMB(@Header("SessionToken") String token, @Query("pass") String pass, @Query("mb") int sizeInMb);
    /** Выполнить в режиме командной строки */
    @POST("/api/admin/execute")
    Call<JString> execute(@Header("SessionToken") String token, @Query("pass") String pass,@Query("cmd") String cmd);
    /** Завершение  работы сервера */
    @POST("/api/admin/shutdown")
    Call<JString> shutdown(@Header("SessionToken") String token, @Query("pass") String pass);
    /** Выполнить продолжительную операцию на сервере с опросом в long polling */
    @GET("/api/admin/preparedb")
    Call<ErrorList> prepareDB(@Header("SessionToken") String token,@Query("operation") int operation,@Query("pass") String pass);
    /** long polling продолжительной операции */
    @GET("/api/admin/longpoll")
    Call<ErrorList> longPolling(@Header("SessionToken") String token,@Query("pass") String pass);
    /** Заблокировать login-ы для монопольной операции */
    @POST("/api/admin/lock")
    Call<JEmpty> lock(@Header("SessionToken") String token,@Query("pass") String pass, @Query("on") boolean on);
    /** Тестовая операция при отладке */
    @GET("/api/admin/testcall")
    Call <JString> testCall(@Header("SessionToken") String token,@Query("operation") int operation,@Query("value") String value);
    /** Обновить лог - закрыть текущий и открыть новый */
    @POST("/api/admin/logfile/reopen")
    Call <JEmpty> reopenLogFile(@Header("SessionToken") String token,@Query("pass") String pass);
    /** Получить список файлов-артефактов в каталоге */
    @GET("/api/admin/files/list")
    Call <StringList> getFolder(@Header("SessionToken") String token,@Query("pass") String pass, @Query("folder") String folder);
    /** Получить текущую версию системы */
    @GET("/api/version")
    Call<JString> currentVersion(@Header("SessionToken") String token);
    /** Получить состояние сервера */
    @GET("/api/serverstate")
    Call<ServerState> serverState(@Header("SessionToken") String token);
    /** Удаление по имени сущности и id */
    @POST("/api/entity/delete")
    Call<JBoolean> deleteById(@Header("SessionToken") String token,@Query("entity") String entity, @Query("id") long id);
    /** Восстановление по имени сущности и id */
    @POST("/api/entity/undelete")
    Call<JBoolean> undeleteById(@Header("SessionToken") String token,@Query("entity") String entity, @Query("id") long id);
    /** Получить список констант */
    @GET("/api/const/all")
    Call<ArrayList<ConstValue>> getConstAll(@Header("SessionToken") String token);
    /** Получить список констант (группа) */
    @GET("/api/const/bygroups")
    Call<ArrayList<ArrayList<ConstValue>>> getConstByGroups(@Header("SessionToken") String token);
    /** Режим кэширования сервера */
    @POST("/api/admin/cashmode")
    Call<JEmpty> setCashMode(@Header("SessionToken") String token,@Query("mode") boolean mode,@Query("pass") String pass);
    /** Клонировать БД */
    @POST("/api/admin/copydbto")
    Call<ErrorList> copyDBTo(@Header("SessionToken") String token, @Query("pass") String pass,@Query("port") int port);
    /** Клонировать БД */
    @POST("/api/admin/copydbfrom")
    Call<ErrorList> copyDBFrom(@Header("SessionToken") String token, @Query("pass") String pass,@Query("port") int port);
    //------------------------------------------------------------------------- не используются
    /** Список имен из таблицы по шаблону */
    @GET("/api/names/get")
    Call<EntityList<EntityNamed>> getNamesByPattern(@Header("SessionToken") String token, @Query("entity") String entity, @Query("pattern") String pattern);
    @POST("/api/gps/address")
    Call<GPSPoint> getGPSByAddress(@Header("SessionToken") String token, @Body Address addr);
    //=========================API авторизации  =========================================================
    /** Завершить сеанс */
    @GET("/api/user/logoff")
    Call<JEmpty> logoff(@Header("SessionToken") String token);
    /** Авторизация по Account в теле запроса (тест) */
    @POST("/api/user/login")
    Call<User> login(@Body Account body);
    /** Авторизация по номеру телефона (логину) и паролю */
    @GET("/api/user/login/phone")
    Call<User> login(@Query("phone") String phone, @Query("pass") String pass);
    /** Получить список пользователей */
    @GET("/api/user/list")
    Call<EntityList<User>> getUserList(@Header("SessionToken") String token,  @Query("mode") int mode,@Query("level") int level);
    /** Добавить пользователя */
    @POST("/api/user/add")
    Call<JLong> addUser(@Header("SessionToken") String token,@Body User user);
    /** Обновить пользователя */
    @POST("/api/user/update")
    Call<JEmpty> updateUser(@Header("SessionToken") String token,@Body User user);
    /** Получить пользователя по oid */
    @GET("/api/user/get")
    Call<User> getUserById(@Header("SessionToken") String token,@Query("id") long id,@Query("level") int level);
    @GET("/api/user/server/environment")
    Call<ArrayList<String>> getSetverEnvironment(@Header("SessionToken") String token);
    //=============================API артефактов и файлов  ================================
    /** Получить описатель артефакта по oid  */
    @GET("/api/artifact/get")
    Call<Artifact> getArtifactById(@Header("SessionToken") String token,@Query("id") long id,@Query("level") int level);
    /** Записать файл как артефакт через multipart-запрос http */
    @Streaming
    @Multipart
    @POST("/api/file/update")
    Call<Artifact> update(@Header("SessionToken") String token,@Query("artId") long artId, @Part MultipartBody.Part file);
    /** Получить список артефактов  */
    @GET("/api/artifact/list")
    Call<ArtifactList> getArtifactList(@Header("SessionToken") String token, @Query("mode") int mode, @Query("level") int level);
    /** Записать файл как артефакт через multipart-запрос http */
    @Streaming
    @Multipart
    @POST("/api/file/upload")
    Call<Artifact> upload(@Header("SessionToken") String token,@Query("description") String description,@Query("origname") String origName, @Part MultipartBody.Part file);
    /** Записать файл по имени в корневой каталог сервера или в каталог экземпляра (с именем = порт) */
    @Streaming
    @Multipart
    @POST("/api/file/uploadByName")
    Call<JEmpty> uploadByName(@Header("SessionToken") String token,@Query("fname") String description, @Part MultipartBody.Part file, @Query("root") boolean root);
    /** Читать файл по id артефакта через multipart-запрос http   */
    @Streaming
    @GET("/api/file/load")
    Call<ResponseBody> downLoad(@Header("SessionToken") String token, @Query("id") long id);
    /** Читать файл по имени из корневого каталога сервера или из каталога экземпляра (с именем = порт)  */
    @Streaming
    @GET ("/api/file/loadByName")
    Call<ResponseBody> downLoadByName(@Header("SessionToken") String token,@Query("fname") String fname, @Query("root") boolean root);
    /** Установить имя файла-ориганиала в артефакте */
    @POST("/api/artifact/setname")
    Call<Artifact> setArtifactName(@Header("SessionToken") String token,@Query("id") long id,@Query("name") String name);
    /** Создать объект-артефакт */
    @POST("/api/artifact/create")
    Call<Artifact> createArtifact(@Header("SessionToken") String token,@Query("description") String description,@Query("origname") String origName, @Query("filesize") long filesize);
    /** Конвертация формата  артефакта через внешнее приложение */
    @POST("/api/artifact/convert")
    Call<JEmpty> convertArtifact(@Header("SessionToken") String token,@Query("id") long id);
    /** Получить список объектов-артефактов по условию */
    @GET("/api/artifact/condition/list")
    Call<ArtifactList> getArtifactConditionList(@Header("SessionToken") String token,
                                                @Query("type") int type, @Query("owner") String owner, @Query("namemask") String nameMask, @Query("filenamemask") String fileNameMask,
                                                @Query("size1") long size1, @Query("size2") long size2,
                                                @Query("dateInMS1") long dateMS1, @Query("dateInMS2") long dateMS2);
    /** Добавить ссылку на артефакт из поля-списка артефактов (EntityLinkList) класса */
    @POST("/api/entity/artifactlist/add")
    Call<JEmpty> addArtifactToList(@Header("SessionToken") String token, @Query("classname") String className, @Query("fieldname") String fieldName, @Query("id") long id, @Query("artifactid") long artifactid);
    /** Удалить ссылку на артефакт из поля-списка артефактов (EntityLinkList) класса  */
    @POST("/api/entity/artifactlist/remove")
    Call<JEmpty> removeArtifactFromList(@Header("SessionToken") String token, @Query("classname") String className,@Query("fieldname") String fieldName,@Query("id") long id,@Query("artifactid") long artifactid);
    /** Заменить ссылку на артефакт в поле fieldName класса  */
    @POST("/api/entity/artifact/replace")
    Call<JEmpty> replaceArtifact(@Header("SessionToken") String token,@Query("classname") String className,@Query("fieldname") String fieldName,@Query("id") long id,@Query("artifactid") long artifactid);
    /** Удалить артефакт вместе с файлом */
    @POST("/api/artifact/remove")
    Call<JEmpty> removeArtifact(@Header("SessionToken") String token,@Query("id") long id);
    /** Получить список артефактов помощи - без авторизации */
    @GET("/api/helpfile/list")
    Call<EntityList<HelpFile>> getHelpFileList(@Query("question") String question );
    /** Записать строку в текстовый файл */
    @POST("/api/artifact/fromstring/bug")
    Call<Artifact> createArtifactFromStringBug(@Header("SessionToken") String token,@Query("fileName") String fileName, @Query("text") String text);
    @POST("/api/artifact/fromstring")
    Call<Artifact> createArtifactFromString(@Header("SessionToken") String token,@Query("fileName") String fileName, @Query("base64") boolean base64, @Body JString text);
    //----------------- Для тестирования ----------------------------------------------------------------------
    @Streaming
    @GET ("/api/file/load2")
    Call<ResponseBody> downLoad2(@Header("SessionToken") String token,@Body JString body);
    //================== Уведомления ==================================================
    /** Установить состояние уведомления */
    @POST("/api/notification/setstate")
    Call<JEmpty> setNotificationState(@Header("SessionToken") String token,@Query("id") long id,@Query("state") int state);
    /**  Селекция по типу пользователя (=0-нет), по id пользователя, по состоянию уведомления*/
    @GET("/api/notification/user/list")
    Call<EntityList<NTMessage>> getNotificationUserList(@Header("SessionToken") String token, @Query("userid") long id, @Query("usertype") int type, @Query("state") int state);
    /** Получить количество уведомлений */
    @GET("/api/notification/count")
    Call<JInt> getNotificationCount(@Header("SessionToken") String token);
    /** Добавить уведомление */
    @POST("/api/notification/add")
    Call<JLong> addNotification(@Header("SessionToken") String token,@Body NTMessage body);
    /** Добавить широковещательное уведомление */
    @POST("/api/notification/add/broadcast")
    Call<JInt> addNotificationBroadcast(@Header("SessionToken") String token,@Body NTMessage body);
    /** Обновить уведомление */
    @POST("/api/notification/update")
    Call<JEmpty> updateNotification(@Header("SessionToken") String token,@Body NTMessage body);
    /** Добавить уведомление по oid */
    @GET("/api/notification/get")
    Call<NTMessage> getNotification(@Header("SessionToken") String token, @Query("id") long id);
    /** Получить все уведомления */
    @GET("/api/notification/list")
    Call<EntityList<NTMessage>> getNotificationList(@Header("SessionToken") String token);
    /** Удалить уведомление */
    @POST("/api/notification/remove")
    Call<JBoolean> removeNotification(@Header("SessionToken") String token, @Query("id") long id);
    /** Получить данные аккаунта авторизованного пользователя */
    @GET("/api/user/account/get")
    Call<Account> getOwnAccount(@Header("SessionToken") String token);
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    // В отдельном интерфейсе
}
