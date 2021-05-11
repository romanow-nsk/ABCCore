package romanow.abc.core.reports;

import romanow.abc.core.UniException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TableHTML extends TableData{
    final static int htmlTextSize=2;
    final static int htmlCellWidth=25;
    final static int htmlCellHight=25;
    StringBuffer out=new StringBuffer();
    @Override
    public String getFileExt() {
        return "html"; }
    public void openReport(String title,String fname) throws UniException {
        super.openReport(title,fname);
        out.append("<html><head><meta http-equiv=\"Content-Language\" content=\"en-us\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\">");
        out.append("\n");
        out.append("<title>"+title+"</title>");
        out.append("\n");
        out.append("</head><body><font face=\"Arial\" size=\""+(htmlTextSize+1)+"\">");
        out.append("\n");
        out.append("<b>"+title+"</b><br><br>");
        out.append("\n");
        }
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow, boolean verticalHeader0){
        super.openPage(title0,cols0,nrow,verticalHeader0);
        }
    public void savePage() throws UniException{
        out.append("<table border=\"1\" style=\"border-collapse: collapse\" bordercolor=\"#000000\">");
        out.append("\n");
        String cellHeight=" height=\""+htmlCellHight+"\"";
        String cellSize="width=\""+htmlCellWidth+"\""+cellHeight;
        int ncol = cols();
        for(int i=0;i<ncol;i++){
            out.append("<tr>");
            out.append("\n");
            for(int j=0;j<i;j++)out.append("<td></td>");
            out.append("<td "+cellHeight+" colspan=\""+(ncol-i)+"\"><font face=\"Arial\" size=\""+(htmlTextSize+1)+"\"><b>");
            out.append(cols.get(i).name);
            //for(int j=0;j<cols[i].length();j++){
            //    if (j!=0) out.append("<br>");
            //    out.append(cols[i].charAt(j));
            //    }
            out.append("</b></td>");
            out.append("\n");
            out.append("</tr>");
            out.append("\n");
            }
        int nrow  = rows();
        for(int i=0;i<nrow;i++){
            out.append("<tr>");
            out.append("\n");
            for(int j=0;j<ncol;j++){
                out.append("<td "+cols.get(j).size+" ");
                if (data.get(i).get(j).selected) out.append("bgcolor=\"#CCCCCC\"");
                out.append("><p align=\"center\"><font face=\"Arial\" size=\""+htmlTextSize+"\"><b>");
                out.append(data.get(i).get(j).value);
                out.append("</b></td>");
                out.append("\n");
                }
            out.append("</tr>");
            out.append("\n");
            }
        if (bottoms!=null){
            for(int i=0;i<bottoms.size();i++){
                out.append("<tr><td"+cellHeight+"><font face=\"Arial\" size=\""+htmlTextSize+"\"><b>"+bottoms.get(i)+"</b></td></tr>");
                out.append("\n");
                }
            }
        out.append("</table>");
        }
    public void saveReport() throws UniException{
        try {
            OutputStreamWriter zz=new OutputStreamWriter(new FileOutputStream(fileName),"Windows-1251");
            out.append("</body></html>");
            out.append("\n");
            zz.write(out.toString());
            zz.close();
            } catch (IOException e) { UniException.io(e); }
        }
}
