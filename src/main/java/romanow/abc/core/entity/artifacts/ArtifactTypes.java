package romanow.abc.core.entity.artifacts;

import romanow.abc.core.constants.ValuesBase;

import java.net.URLConnection;
import java.util.HashMap;

public class ArtifactTypes {
    private static HashMap<String,Integer> extTypes = new HashMap<>();
    static {
        extTypes.put("mp3",new Integer(ValuesBase.ArtifactAudioType));
        extTypes.put("wav",new Integer(ValuesBase.ArtifactAudioType));
        extTypes.put("3gpp",new Integer(ValuesBase.ArtifactAudioType));
        extTypes.put("doc",new Integer(ValuesBase.ArtifactDocType));
        extTypes.put("docx",new Integer(ValuesBase.ArtifactDocType));
        extTypes.put("pdf",new Integer(ValuesBase.ArtifactDocType));
        extTypes.put("txt",new Integer(ValuesBase.ArtifactTextType));
        extTypes.put("mpg",new Integer(ValuesBase.ArtifactVideoType));
        extTypes.put("mp4",new Integer(ValuesBase.ArtifactVideoType));
        extTypes.put("3gp",new Integer(ValuesBase.ArtifactVideoType));
        extTypes.put("jpg",new Integer(ValuesBase.ArtifactImageType));
        extTypes.put("png",new Integer(ValuesBase.ArtifactImageType));
        }
    public static String getMimeType(String ext){
        String type = URLConnection.getFileNameMap().getContentTypeFor("a."+ext);
        return type!=null ? type : "application/"+ext;
        }
    public static int getArtifactType(String ext){
        Integer type = extTypes.get(ext.toLowerCase());
        return type!=null ? type.intValue() : ValuesBase.ArtifactOtherType;
        }
    }
