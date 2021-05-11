package romanow.abc.core.reports;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.base.StringList;

import java.util.ArrayList;

public abstract class ReportBase {
    protected long invertColumnMask=0;              // Инверсная маска столбцов
    public Artifact reportFile = new Artifact();
    public int reportType= ValuesBase.RepOther;
    abstract public void createReportFile(TableData table, String fspec) throws Exception;
    abstract public String getTitle();
    public ReportBase(int type){
        reportType = type;
        }
    public ReportBase(int type, long mask){
        invertColumnMask = mask;
        reportType = type;
        }
    public String getFileExt(){ return null; }
    public String testReportContent(){ return null; }
    public StringList getColNames(){
        StringList out = new StringList();
        ArrayList<TableCol> in = createHeader();
        for(TableCol ss : in)
            out.add(ss.name);
        return out;
        }
    abstract public ArrayList<TableCol> createHeader();
    }
