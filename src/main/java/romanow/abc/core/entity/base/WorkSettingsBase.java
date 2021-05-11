package romanow.abc.core.entity.base;

import romanow.abc.core.entity.Entity;

public class WorkSettingsBase extends Entity {
    private String MKVersion="1.0.01";
    private String dataServerFileDir="d:/temp";
    private boolean dataServerFileDirDefault=true;  // Используется текущий каталог запуска
    private boolean convertAtrifact=false;
    public WorkSettingsBase(){}
    //------------------------------------------------------------------------------------------------------------------
    public boolean isDataServerFileDirDefault() {
        return dataServerFileDirDefault; }
    public void setDataServerFileDirDefault(boolean dataServerFileDirDefault) {
        this.dataServerFileDirDefault = dataServerFileDirDefault; }
    public String getMKVersion() {
        return MKVersion; }
    public void setMKVersion(String MKVersion) {
        this.MKVersion = MKVersion; }
    public boolean isConvertAtrifact() {
        return convertAtrifact; }
    public void setConvertAtrifact(boolean convertAtrifact) {
        this.convertAtrifact = convertAtrifact; }
    public void setDataServerFileDir(String dataServerFileDir) {
        this.dataServerFileDir = dataServerFileDir; }
    public String getDataServerFileDir() {
        return dataServerFileDir; }
}
