package romanow.abc.core.entity.artifacts;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;

public class ReportFile extends Entity {
    public ReportFile(){}
    private String reportHeader="";
    private int reportType= ValuesBase.RepOther;
    private boolean archive=false;
    private EntityLink<Artifact> artifact = new EntityLink<>(Artifact.class);
    public String getReportHeader() {
        return reportHeader; }
    public void setReportHeader(String reportHeader) {
        this.reportHeader = reportHeader; }
    public EntityLink<Artifact> getArtifact() {
        return artifact; }
    public void setArtifact(EntityLink<Artifact> artifact) {
        this.artifact = artifact; }
    public ReportFile(String reportHeader) {
        this.reportHeader = reportHeader;
        }
    public ReportFile(String reportHeader, long oid) {
        this.reportHeader = reportHeader;
        artifact.setOid(oid);
        }
    public ReportFile(String reportHeader, Artifact art) {
        this.reportHeader = reportHeader;
        artifact.setOidRef(art);
    }
    public int getReportType() {
        return reportType; }
    public void setReportType(int reportType) {
        this.reportType = reportType; }
    public boolean isArchive() {
        return archive; }
    public void setArchive(boolean archive) {
        this.archive = archive; }
    public String getTitle(){ return reportHeader; }
    public String toShortString(){ return reportHeader+" от "+artifact.getRef().getDate().dateTimeToString(); }
    public String toString(){ return reportHeader+" "+artifact.toString(); }
}
