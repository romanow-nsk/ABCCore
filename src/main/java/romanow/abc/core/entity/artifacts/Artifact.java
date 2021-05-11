package romanow.abc.core.entity.artifacts;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.EntityBack;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.core.utils.OwnDateTime;
import org.joda.time.format.DateTimeFormat;

public class Artifact extends EntityBack {
    private int type= ValuesBase.UndefinedType;      // Тип файла
    private String name="";                     // Название артефакта
    private FileNameExt original = new FileNameExt("","");
    private OwnDateTime date=new OwnDateTime(); // Дата/время создания
    private long fileSize=0;                    // Размер файла
    private boolean fileLost=false;             // Файл отсутствует
    //----------------------------------------------------------------------------------------
    public boolean isFileLost() {
        return fileLost; }
    public void setFileLost(boolean fileLost) {
        this.fileLost = fileLost; }
    public Artifact(){
        date=new OwnDateTime();
        }
    @Override
    public String getTitle() {
        return ValuesBase.ArtifactTypeNames[type]+" "+name+" ["+getOriginalName()+"] "; }
    public FileNameExt getOriginal() {
        return original; }
    public void setOriginal(FileNameExt original) {
        this.original = original; }
    public int type(){ return type; }
    public void setType(int type0){ type = type0; }
    public String typeName() { return ValuesBase.ArtifactTypeNames[type]; }
    public String directoryName() { return ValuesBase.ArtifactDirNames[type]; }
    public Artifact(int id){ setOid(id); }
    public String getOriginalName() {
        return original.fileName();
        }
    public String getOriginalExt() {
        return original.getExt();
        }
    public void setOriginalName(String ss){ original.setName(ss);}
    public void setOriginalExt(String ss){ original.setExt(ss);}
    public OwnDateTime getDate() {
        return date;
        }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public long getFileSize() {
        return fileSize;
        }
    public Artifact(String name,long fsize){
        fileSize = fsize;
        original = new FileNameExt("",name);
        }
    public String createArtifactServerPath(){
        return type()+"_"+directoryName()+"/"+createArtifactFileName();
        }
    public String createArtifactFileName(){
        return date.date().toString(DateTimeFormat.forPattern("yyyyMMdd_HHmmss"))+"_"+getOid()+"("+original.fileName()+")"+"."+original.getExt();
        }
    public void setDate(OwnDateTime date) {
        this.date = date; }
    public String toString(){ return name +" "+ typeName()+"_"+createArtifactFileName()+" ["+fileSize+"]"; }
    public String toFullString(){ return super.toFullString()+name +" "+ typeName()+"_"+createArtifactFileName()+" ["+fileSize+"]"; }
    public boolean isFile(){ return true; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    //------------------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document);
        //date.putData("d_",document,0,null);
        original.putData("f_",document,0,null);
    }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res);
        //date.getData("d_",res, 0, null);
        original.getData("f_",res, 0, null);
        //afterLoad();
    }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        //date.getData(row, cnt);
        original.getData(row, cnt);
        //afterLoad();
    }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        //date.putData(row, cnt);
        original.putData(row, cnt);
    }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        //date.putHeader("d_",list);
        original.putHeader("f_",list);
    }
    */
    //--------------------------------------------------------------------------------------------------
}
