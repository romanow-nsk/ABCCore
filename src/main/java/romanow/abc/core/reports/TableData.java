package romanow.abc.core.reports;

import romanow.abc.core.UniException;

import java.util.ArrayList;

public class TableData implements I_Report{
    protected DocumentParamList paramList = new DocumentParamList();
    protected boolean verticalHeader=false;
    protected String fileName;
    protected int tableType=0;
    protected String title="";
    ArrayList<String> bottoms = new ArrayList<>();
    protected ArrayList<TableCol> cols=new ArrayList<>();
    protected ArrayList<ArrayList<TableCell>> data =new ArrayList();
    protected ArrayList<TableRowItem> rowData = new ArrayList<>();
    //----------------------------------------------------------------------------
    public ArrayList<ArrayList<TableCell>> data(){ return  data; }
    public ArrayList<String> bottoms(){ return bottoms; }
    public ArrayList<TableCol> columns(){ return cols; }
    public int getTableType(){ return tableType; }
    public void setTableType(int vv){ tableType=vv; }
    public boolean isValid(){ return cols.size()!=0; }
    public int cols(){ return  cols.size(); }
    public int rows(){ return  data.size(); }
    public TableData(){ }
    public TableData(DocumentParamList ss){ paramList=ss; }
    public void addBottom(String ss){ bottoms.add(ss); }
    public int nextRow(){
        ArrayList<TableCell> ros = new ArrayList<>();
        for(int i=0;i<cols.size();i++)
            ros.add(new TableCell());
        data.add(ros);
        TableRowItem item = new TableRowItem();
        item.nLines = paramList.getnLines();
        rowData.add(item);
        return data.size();
        }
    @Override
    public void openReport(String title, String fname) throws UniException {
        fileName = fname;
        }
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow, boolean verticalHeader0){
        verticalHeader = verticalHeader0;
        title = title0;
        cols = cols0;
        data.clear();
        while(nrow--!=0)
            nextRow();
            }
    @Override
    public boolean setCellValue(int rowIdx, int colIdx, String text) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).value = text;
        return true;
        }
    @Override
    public boolean setCellValue(int rowIdx, int colIdx, int text) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).value = ""+text;
        return true;
    }
    @Override
    public boolean setCellValue(int rowIdx, int colIdx, long text) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).value = ""+text;
        return true;
    }
    @Override
    public boolean setCellValue(int rowIdx, int colIdx, boolean bb) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).value = bb ? "+" : "-";
        return true;
        }
    @Override
    public boolean setCellValue(int rowIdx, int colIdx, double dd) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).value = ""+String.format("%5.3f",dd);
        return true;
        }
    @Override
    public boolean setCellBackColor(int rowIdx, int colIdx, int hexColor) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).hexBackColor = hexColor;
        return true;
        }
    @Override
    public boolean setCellTextColor(int rowIdx, int colIdx, int hexColor) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).hexTextColor = hexColor;
        return true;
        }
    @Override
    public boolean setCellTextSize(int rowIdx, int colIdx, int size) throws UniException {
        if (rowIdx >=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).textSize = size;
        return true;
        }
    @Override
    public boolean setCellFont(int rowIdx, int colIdx, DefaultFont font) throws UniException {
        return false;
        }

    @Override
    public boolean setCellSelected(int rowIdx, int colIdx, boolean sel) throws UniException {
        if (rowIdx>=rows()) return false;
        if (colIdx>=cols()) return false;
        data.get(rowIdx).get(colIdx).selected = sel;
        return true;
        }

    @Override
    public String getFileExt() {
        return ""; }

    @Override
    public void savePage() throws UniException {
        }
    @Override
    public void saveReport() throws UniException {
        }
    public void removeCols(long mask){
        int size = cols.size();
        int idx, rIdx;
        long mm = mask;
        for(idx=rIdx=0;idx<size;idx++){
            boolean shift = (mm & 1)!=0;
            mm >>=1;
            if (shift){
                cols.remove(rIdx);
                }
            else
                rIdx++;
            }
        for(ArrayList<TableCell> row : data){
            mm = mask;
            for(idx=rIdx=0;idx<size;idx++){
                boolean shift = (mm & 1)!=0;
                mm >>=1;
                if (shift){
                    row.remove(rIdx);
                    }
                else
                    rIdx++;
                }
            }
        }
}