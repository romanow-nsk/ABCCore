package romanow.abc.core.mongo;

import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.ErrorList;
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

public class KotlinJSConverter {
    public static void createKotlinClassFile(String outPackage,String className, String ss) throws Exception{
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outPackage+"/"+className+".kt"),"UTF-8");
        out.write("package "+outPackage.replace("/",".")+"\n"+ss);
        out.flush();
        out.close();
        }
    public static void createKotlinClassSources(ErrorList errors){
        String outPackage = ValuesBase.KotlinJSPackage;
        File ff = new File(outPackage+"/");
        ff.mkdirs();
        try {
            createKotlinClassFile(outPackage,"JEmpty","class JEmpty {}\n");
            createKotlinClassFile(outPackage,"JInt","class JInt { var value = 0 }\n");
            createKotlinClassFile(outPackage,"JBoolean","class JBoolean { var value = false }\n");
            createKotlinClassFile(outPackage,"JString","class JString { var value = \"\" }\n");
            createKotlinClassFile(outPackage,"JLong","class JLong { var value = 0L}\n");
            createKotlinClassFile(outPackage,"ArtifactList","class ArtifactList : EntityList<Artifact?>(){}\n");
            createKotlinClassFile(outPackage,"ConstList","class ConstList(val group : String?=\"\") : ArrayList<ConstValue>() { }\n");
            createKotlinClassFile(outPackage,"ConstValue","class ConstValue(var groupName:String?=\"\", var name:String?=\"\", var title:String?=\"...\", var className:String?=\"\", var value:Int=0) { }");
            createKotlinClassFile(outPackage,"EntityList","open class EntityList<T : Entity?> : ArrayList<T>() { }");
            createKotlinClassFile(outPackage,"EntityNamed","open class EntityNamed : Entity() { var name = \"\"}\n");
            createKotlinClassFile(outPackage,"EntityLinkList","class EntityLinkList<T : Entity?> : ArrayList<EntityLink<T>?>() {}\n");
            createKotlinClassFile(outPackage,"EntityRefList","class EntityRefList<T : Entity?> : ArrayList<T>() {}\n");
            createKotlinClassFile(outPackage,"EntityLink","class EntityLink<T : Entity?> {\n"+
                    "    var oid: Long = 0L\n"+
                    "    var ref: T? = null\n}\n");
            createKotlinClassFile(outPackage,"Entity","open class Entity{\n" +
                    "    var oid: Long = 0\n" +
                    "    var isValid = true\n" +
                    "}\n");
            createKotlinClassFile(outPackage,"RestAPIBase",createJSAPIFace(RestAPIBase.class,errors));
        } catch (Exception e) {
            errors.addError("Ошибка создания EntityLink...: "+e.toString());
            }
        ArrayList<TableItem> tables = ValuesBase.init().getEntityFactory().classList(true,true);
        for (TableItem item : tables){
            try {
                DAO dao = (DAO)item.clazz.newInstance();
                String ss =  dao.createKotlinClassSource();
                createKotlinClassFile(outPackage,dao.getClass().getSimpleName(),ss);
            } catch (Exception e) {
                errors.addError("Ошибка создания "+item.name+": "+e.toString());
            }
        }
    }
    //-----------------------------------------------------------------------------------------------------------------
    public static void createJSAPIFile(Class api,ErrorList errorList){
        try {
            createKotlinClassFile(ValuesBase.KotlinJSPackage,"RestAPIBase",createJSAPIFace(api,errorList));
            } catch (Exception e) {
                errorList.addError("Ошибка создания "+api.getSimpleName()+": "+e.toString());
                }
        }
    //------------------------------------------------------------------------------------------------------------------
    public static String procMethod(String name, String url, Method method, ErrorList errors){
        //--------------------- Образец---------------------------
        //suspend fun keepAlive(token:String): JInt {
        //    val response = window
        //            .fetch("localhost:4567/api/keepalive", RequestInit("get",Headers().append("SessionToken",token)))
        //            .await()
        //            .text()
        //            .await()
        //    return response as JInt
        //}
        String par1 = "suspend fun ";
        String genericType="";
        Type type = method.getReturnType();
        //TODO - результат Call<T> - параметр типа извлечь
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArguments.length!=0){
                genericType = typeArguments[0].getTypeName();
                int idx = genericType.lastIndexOf(".");
                if (idx!=-1)
                    genericType = genericType.substring(idx+1);
                }
            }
        par1 +=method.getName()+"(ip:String,port:int";
        String par2 = "";
        String par3="";
        String out = name +" "+url+" "+method.getName()+" "+genericType;
        for(Parameter parameter : method.getParameters()){
            String ss="";
            if (parameter.isAnnotationPresent(Header.class)){
                Header header = (Header)parameter.getAnnotation(Header.class);
                ss = "header="+header.value()+" ";
                if (par3.length()==0)
                    par3="Headers()";
                par3 +=".append(\""+header.value()+"\","+header.value();
                par1+=","+header.value()+":"+parameter.getType().getSimpleName();
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
                par1+=","+query.value()+":"+parameter.getType().getSimpleName();
                }
            if (parameter.isAnnotationPresent(Part.class)){
                Part part = (Part) parameter.getAnnotation(Part.class);
                ss="part="+part.value();
                }
            out+=" "+parameter.getName()+":"+ss+":"+parameter.getType().getSimpleName();
            }
        par1 += ") JEmpty {\n    val response = window\n        .fetch(ip+\":\"+port+\""+url+par2+"\"";
        par1 +=",RequestInit(\""+name+"\"";
        if (par3.length()!=0)
            par1 += ","+par3;
        par1+=")))\n        .await().text().await()\n    return response as JInt\n    }\n";
        return par1;
        }
    public static String createJSAPIFace(Class apiFace, ErrorList errors){
        if (!apiFace.isInterface()){
            errors.addError(apiFace.getSimpleName()+" - не интерфейс");
            return null;
            }
        String out="";
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
        return out;
    }
}
