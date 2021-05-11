package romanow.abc.core.API;

import romanow.abc.core.entity.artifacts.ArtifactTypes;
import romanow.abc.core.utils.FileNameExt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

public class RestAPICommon {
    //--------------------- Выгрузка файла - общая часть
    public static MultipartBody.Part createMultipartBody(FileNameExt fname) {
        if (fname == null) return null;
        return createMultipartBody(fname.fullName(),fname.getExt());
        }
    public static MultipartBody.Part createMultipartBody(String fname, String ext){
        if (fname == null) return null;
        File file = new File(fname);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = ArtifactTypes.getMimeType(ext);
        //System.out.println(type);
        MediaType mType = MediaType.parse(type);
        //System.out.println(mType);
        RequestBody requestFile = RequestBody.create(mType, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "", requestFile);
        return body;
    }
}
