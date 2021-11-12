package romanow.abc.core.dll;



import romanow.abc.core.UniException;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

    /**
     * Загружаем файлы из заданного jar-архива
     * Классы должны относится к заданному пакету - пример валидации при загрузке
     *
     * @author Pavel
     *
     */

    public class JarClassLoader extends ClassLoader {
        private HashMap<String, Class<?>> cache = new HashMap<String, Class<?>>();
        private String jarFileName;
        private String packageName;
        public JarClassLoader(String jarFileName, String packageName) {
            this.jarFileName = jarFileName;
            this.packageName = packageName;
            }
        public JarClassLoader(String jarFileName) {
            this.jarFileName = jarFileName;
            this.packageName = getDefaultPackageName();
            }
        public String getDefaultPackageName(){
            return "romanow."+ ValuesBase.env().applicationName(ValuesBase.AppNameDBName)+".dll";
            }
        public String loadClasses(){
            String mes = "";
            try {
                JarFile jarFile = new JarFile(jarFileName);
                Enumeration entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    // Одно из назначений хорошего загрузчика - валидация классов на этапе загрузки
                    String className = jarEntry.getName().replace('/', '.');
                    if (className.startsWith(packageName) && className.endsWith(".class")) {
                        byte[] classData = loadClassData(jarFile, jarEntry);
                        if (classData != null) {
                            String ss1 = className.substring(0, className.length() - 6);
                            Class<?> clazz = defineClass(ss1, classData, 0, classData.length);
                            cache.put(clazz.getSimpleName(), clazz);
                        }
                    }
                }
            }
            catch (IOException IOE) {
                return  jarFileName+" не найден";
                }
            return null;
            }

        private DLLField createParameterDescription(int type,Annotation ann[]){
            for(Annotation aa : ann){
                String ss = aa.annotationType().getSimpleName();
                if (ss.equals("PARAM")){
                    PARAM param = (PARAM) aa;
                    return new DLLField(param.name(),type,param.title());
                    }
                }
            return new DLLField("...",type,"...");
            }
        public Pair<String,DLLModule> getClassesList(){
            String out="";
            ArrayList<DLLClass> list = new ArrayList<>();
            for(String cName : getClassNames()){
                Class<?> clazz = cache.get(cName);
                Class faces[] = clazz.getInterfaces();
                boolean found=false;
                for(Class xx : faces)
                    if (xx.getSimpleName().equals("I_DLL")){
                        found=true;
                        break;
                    }
                if (!found){
                    out+=cName+" не является DLL-модулем\n";
                    continue;
                    }
                Method methods[] = clazz.getMethods();
                ArrayList<DLLFunction> funs = new ArrayList<>();
                for (Method method : methods){
                    String tName = method.getReturnType().getName();
                    if (!method.isAnnotationPresent(METHOD.class))
                        continue;
                    String title = ((METHOD) method.getAnnotation(METHOD.class)).title();
                    int resIdx = DAO.getFieldType(tName);
                    if (resIdx==-1){
                        out+="Недопустимый тип результата: "+cName+"."+method.getName()+": "+tName+"\n";
                        continue;
                        }
                    Class typeList[] = method.getParameterTypes();
                    Annotation params[][] = method.getParameterAnnotations();
                    if (typeList.length==0){
                        out+="Список параметров пуст: "+cName+"."+method.getName()+"\n";
                        continue;
                        }
                    if (!typeList[0].getSimpleName().equals("MetaExternalSystem")){
                        out+="Нет ссылки на MetaExternalSystem:  "+cName+"."+method.getName()+"\n";
                        continue;
                        }
                    boolean valid=true;
                    ArrayList<DLLField> ff = new ArrayList<>();
                    for(int i=1;i<typeList.length;i++){
                        int typeIdx = DAO.getFieldType(typeList[i].getSimpleName());
                        if (typeIdx==-1){
                            valid = false;
                            out+="Недопустимый тип параметра: "+cName+"."+method.getName()+": индекс "+i+"\n";
                            continue;
                            }
                        ff.add(createParameterDescription(typeIdx,params[i]));
                        }
                    if (valid) {
                        DLLFunction function = new DLLFunction(method.getName(), resIdx, ff, title);
                        funs.add(function);
                        }
                    }
                list.add(new DLLClass(clazz,funs));
                }
            return new Pair<>(out.length()==0 ? null : out,new DLLModule(jarFileName,list));
            }
        public ArrayList<String> getClassNames(){
            ArrayList<String> out = new ArrayList<>();
            Object oo[] = cache.keySet().toArray();
            for(Object zz : oo)
                out.add((String) zz);
            return out;
            }

        private byte[] loadClassData(JarFile jarFile, JarEntry jarEntry) throws IOException {
            long size = jarEntry.getSize();
            if (size == -1 || size == 0)
                return null;
            byte[] data = new byte[(int)size];
            InputStream in = jarFile.getInputStream(jarEntry);
            in.read(data);
            return data;
            }
        public static void main(String ss[]) throws UniException {
            JarClassLoader loader = new JarClassLoader("out/artifacts/ESS2DLL_jar/ESS2DLL.jar","romanow.ess2.dll");
            loader.loadClasses();
            System.out.println(loader.getDefaultPackageName());
            System.out.println(loader.getClassNames());
        }
}
