package romanow.abc.core.mongo;

import retrofit2.http.*;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.ErrorList;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.TableItem;
import romanow.abc.core.constants.ValuesBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExportKotlin {
    public static void createKotlinClassFile(String path,String outPackage,String className, String ss) throws Exception{
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path+outPackage+"/"+className+".kt"),"UTF-8");
        out.write("package "+outPackage.replace("/",".")+"\n"+ss);
        out.flush();
        out.close();
        }
    public static void createKotlinClassSources(String path,ErrorList errors){
        if (path.length()!=0)
            path+="/";
        String outPackage = ValuesBase.KotlinJSPackage;
        File ff = new File(path+outPackage+"/");
        ff.mkdirs();
        try {
            createKotlinClassFile(path,outPackage,"R",DAO.classHeader+"R<T>(var mes:String=\"\",var data:T?=null) {\n"+
                    "fun valid():Boolean { return mes.length==0 }}");
            createKotlinClassFile(path,outPackage,"IntegerList",DAO.classHeader+"IntegerList : ArrayList<Int?>()\n");
            createKotlinClassFile(path,outPackage,"OidString",DAO.classHeader+"OidString {\n    val mes: String = \"\"\n    val oid: Long = 0\n    val errorCount = 0\n}\n");
            createKotlinClassFile(path,outPackage,"ErrorList",DAO.openClassHeader+"ErrorList {\n    val errCount = 0\n    val info = \"\"\n}\n");
            createKotlinClassFile(path,outPackage,"CallResult",DAO.classHeader+"CallResult : ErrorList() {\n    val state = 0\n}\n");
            createKotlinClassFile(path,outPackage,"ResponseBody",DAO.classHeader+"ResponseBody {}\n");
            createKotlinClassFile(path,outPackage,"JEmpty",DAO.classHeader+"JEmpty {}\n");
            createKotlinClassFile(path,outPackage,"JInt",DAO.classHeader+"JInt { var value = 0 }\n");
            createKotlinClassFile(path,outPackage,"JBoolean",DAO.classHeader+"JBoolean { var value = false }\n");
            createKotlinClassFile(path,outPackage,"JString",DAO.classHeader+"JString { var value = \"\" }\n");
            createKotlinClassFile(path,outPackage,"JLong",DAO.classHeader+"JLong { var value = 0L}\n");
            createKotlinClassFile(path,outPackage,"ArtifactList",DAO.classHeader+"ArtifactList : EntityList<Artifact?>(){}\n");
            createKotlinClassFile(path,outPackage,"StringList",DAO.classHeader+"StringList : ArrayList<String?>(){}\n");
            createKotlinClassFile(path,outPackage,"ConstList",DAO.classHeader+"ConstList(val group : String?=\"\") : ArrayList<ConstValue>() { }\n");
            createKotlinClassFile(path,outPackage,"ConstValue",DAO.classHeader+"ConstValue(var groupName:String?=\"\", var name:String?=\"\", var title:String?=\"...\", var className:String?=\"\", var value:Int=0) { }");
            createKotlinClassFile(path,outPackage,"EntityList","open class EntityList<T> : ArrayList<T>() { }");
            createKotlinClassFile(path,outPackage,"EntityNamed","open class EntityNamed : Entity() { var name = \"\"}\n");
            createKotlinClassFile(path,outPackage,"EntityLink",DAO.classHeader+"EntityLink<T> {\n"+
            //--------------------- Замена на прямые ArrayList -----------------------------------------------------------------------
            //createKotlinClassFile(path,outPackage,"EntityLinkList",DAO.classHeader+"EntityLinkList<T : Entity?> : ArrayList<EntityLink<T>?>() {}\n");
            //createKotlinClassFile(path,outPackage,"EntityRefList",DAO.classHeader+"EntityRefList<T : Entity?> : ArrayList<T>() {}\n");
                    "    var oid: Long = 0L\n"+
                    "    var ref: T? = null\n}\n");
            createKotlinClassFile(path,outPackage,"Entity","open class Entity{\n" +
                    "    var oid: Long = 0\n" +
                    "    var isValid = true\n" +
                    "}\n");
            createKotlinClassFile(path,outPackage,"RestAPIBase",createJSAPIFace(RestAPIBase.class,errors));
            createJSAPIFile(path,RestAPIBase.class,errors);
        } catch (Exception e) {
            errors.addError("Ошибка создания EntityLink...: "+e.toString());
            }
        ArrayList<TableItem> tables = ValuesBase.init().getEntityFactory().classList(true,true);
        for (TableItem item : tables){
            try {
                DAO dao = (DAO)item.clazz.newInstance();
                String ss =  dao.createKotlinClassSource();
                createKotlinClassFile(path,outPackage,dao.getClass().getSimpleName(),ss);
            } catch (Exception e) {
                errors.addError("Ошибка создания "+item.name+": "+e.toString());
            }
        }
    }
    //-----------------------------------------------------------------------------------------------------------------
    public static void createJSAPIFile(String path,Class api,ErrorList errorList){
        try {
            createKotlinClassFile(path,ValuesBase.KotlinJSPackage,api.getSimpleName(),createJSAPIFace(api,errorList));
            } catch (Exception e) {
                errorList.addError("Ошибка создания "+api.getSimpleName()+": "+e.toString());
                }
        }
    //------------------------------------------------------------------------------------------------------------------
    public final static String apiHeader="\n"+
            "import kotlinx.browser.window\n"+
            "import kotlinx.coroutines.await\n"+
            "import org.w3c.fetch.Headers\n"+
            "import org.w3c.fetch.RequestInit\n\n"+
            "import kotlinx.serialization.Serializable\n" +
            "import kotlinx.serialization.decodeFromString\n" +
            "import kotlinx.serialization.json.Json\n" +
            "\n";
    //-------------------------------------------------------------------------------------------------------------------
    private static String stripOne(String ss){
        int idx = ss.lastIndexOf(".");
        if (idx!=-1)
            ss = ss.substring(idx+1);
        return ss;
        }
    private static String stripFullClassPath(String ss){
        int idx1=ss.indexOf("<");
        if (idx1==-1){
            return stripOne(ss);
            }
        int idx2 = ss.lastIndexOf(">");
        String ss1 = stripOne(ss.substring(0,idx1));
        String ss2 = stripFullClassPath(ss.substring(idx1+1,idx2));
        return  ss1+"<"+ss2+">";
        }
    public static String procMethod(String name, String url, Method method, ErrorList errors){
        //--------------------- Образец---------------------------
        //suspend fun keepAlive(token:String): JInt {
        //    val response = window
        //            .fetch("127.0.0.1:4567/api/keepalive", RequestInit("get",Headers().append("SessionToken",token)))
        //            .await()
        //            .text()
        //            .await()
        //    return response as JInt
        //}
        String par1 = "    suspend fun ";
        String genericType="";
        String paramList = "";
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length!=0){
                genericType = typeArguments[0].getTypeName();
                genericType = stripFullClassPath(genericType);
                }
            }
        par1 +=method.getName()+"(";
        String par2 = "";
        String par3="";
        String out = name +" "+url+" "+method.getName()+" "+genericType;
        for(Parameter parameter : method.getParameters()){
            String ss="";
            String typeName = Utils.toUpperFirst(parameter.getType().getSimpleName());
            if (parameter.isAnnotationPresent(Header.class)){
                Header header = (Header)parameter.getAnnotation(Header.class);
                ss = "header="+header.value()+" ";
                par3 +="        headers.append(\""+header.value()+"\","+header.value()+")\n";
                if (paramList.length()!=0)
                    paramList +=",";
                paramList += header.value()+":"+typeName;
                }
            if (parameter.isAnnotationPresent(Body.class)){
                Body body = (Body)parameter.getAnnotation(Body.class);
                ss="body";
                }
            if (parameter.isAnnotationPresent(Query.class)){
                Query query = (Query) parameter.getAnnotation(Query.class);
                ss="query="+query.value();
                par2 += par2.length()==0 ? "?" : "&";
                par2 += query.value()+"=\"+"+query.value()+"+\"";
                if (paramList.length()!=0)
                    paramList +=",";
                paramList += query.value()+":"+typeName;
                }
            if (parameter.isAnnotationPresent(Part.class)){
                Part part = (Part) parameter.getAnnotation(Part.class);
                ss="part="+part.value();
                }
            out+=" "+parameter.getName()+":"+ss+":"+ typeName;
            }
        par1 += paramList+") : R<"+genericType+"> {\n";
        if (par3.length()!=0) {
            par1 += "        val headers = Headers()\n";
            par1 += par3;
            }
        par1 +="        val res = window\n        .fetch(\"http://\"+ip+\":\"+port+\""+url+par2+"\"";
        par1 +=",RequestInit(\""+name+"\"";
        if (par3.length()!=0)
            par1 += ",headers";
        par1+="))\n            .await()\n" +
                "        if (!res.ok)\n"+
                "            return R(res.statusText+\" \"+res.text().await(),null)\n" +
                "        val res2 = res.text().await()\n" +
                "        val format = Json { ignoreUnknownKeys = true }\n" +
                "        return R(\"\",format.decodeFromString<"+genericType+">(res2))\n" +
                "    }\n";
        return par1;
        }
    public static String createJSAPIFace(Class apiFace, ErrorList errors){
        if (!apiFace.isInterface()){
            errors.addError(apiFace.getSimpleName()+" - не интерфейс");
            return null;
            }
        String out=apiHeader+"class "+apiFace.getSimpleName()+" (var ip: String = \"127.0.0.1\", var port: Int = 4567){\n";
        for(Method method : apiFace.getMethods()){
            if (method.isAnnotationPresent(GET.class)){
                GET get = (GET) method.getAnnotation(GET.class);
                out+=procMethod("get",get.value(),method,errors);
                }
            if (method.isAnnotationPresent(POST.class)){
                POST get = (POST) method.getAnnotation(POST.class);
                out+=procMethod("post",get.value(),method,errors);
            }
            }
        return out+"}\n";
        }
    public static void exrortKotlinErrors(String path, ErrorList errorList){
        try {
            System.out.println(errorList.toString());
            File ff = new File(path+"ExportKotlinErrors.kt");
            ff.delete();
            if (errorList.valid())
                return;
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path+"ExportKotlinErrors.kt"), "UTF-8");
            out.write(errorList.toString());
            out.flush();
            out.close();
            } catch (Exception ee) {
                System.out.println("ExportKotlinErrors.kt: " + ee.toString());
                }
            }
    //----------------------------------------------------------------------------------------------------
    public static void main(String aa[]){
        ValuesBase.init();
        ErrorList errorList = new ErrorList();
        createKotlinClassSources("",errorList);
        exrortKotlinErrors("",errorList);
        }
}
