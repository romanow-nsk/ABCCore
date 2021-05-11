package romanow.abc.core.reports;

import romanow.abc.core.UniException;

import java.util.ArrayList;

public interface I_Report {
    public void openReport(String title,String fname) throws UniException;
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow, boolean verticalHeader0);
    public void savePage() throws UniException;
    public void saveReport() throws UniException;
    public boolean setCellValue(int rowIdx,int colIdx, String text) throws UniException;
    public boolean setCellValue(int rowIdx,int colIdx, boolean bb) throws UniException;
    public boolean setCellValue(int rowIdx,int colIdx, double dd) throws UniException;
    public boolean setCellValue(int rowIdx,int colIdx, int dd) throws UniException;
    public boolean setCellValue(int rowIdx,int colIdx, long dd) throws UniException;
    public boolean setCellBackColor(int rowIdx, int colIdx, int hexColor) throws UniException;
    public boolean setCellTextColor(int rowIdx, int colIdx, int hexColor) throws UniException;
    public boolean setCellTextSize(int rowIdx, int colIdx, int size) throws UniException;
    public boolean setCellFont(int rowIdx, int colIdx, DefaultFont font) throws UniException;
    public boolean setCellSelected(int rowIdx, int colIdx, boolean sel) throws UniException;
    public String getFileExt();
    }
