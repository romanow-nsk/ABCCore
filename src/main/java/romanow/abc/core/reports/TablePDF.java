package romanow.abc.core.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import romanow.abc.core.UniException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TablePDF extends TableData{
    public static final String fontFile="arial.ttf";
    //protected String fontPath="c:/windows/fonts/"+fontFile;
    protected String fontPath=fontFile;     // 657-2 - в каталоге запуска сервера
    protected Font titleFont;
    protected Font cellHeaderFont;
    protected Font cellRegularFont;
    protected Font cellBottomFont;
    protected Font cellLinkFont;
    protected int cellHeight=26;
    protected int cellWidth=20;
    protected int cellWidthFirst=120;
    protected int cellWidthLast=30;
    private PdfPTable table;
    private Document document;
    public void setFontPath(String path){
        fontPath = path;
        }
    @Override
    public String getFileExt() {
        return "pdf"; }
    public TablePDF(){}
    public TablePDF(DocumentParamList ss){
        super(ss);
        }
    @Override
    public void openReport(String titile, String fname) throws UniException {
        super.openReport(title,fname);
        try {
            BaseFont russianFont = BaseFont.createFont(fontPath, "cp1251", BaseFont.EMBEDDED);
            titleFont = new Font(russianFont, 12, Font.BOLD);
            cellHeaderFont = new Font(russianFont, 10, Font.NORMAL);
            cellRegularFont = new Font(russianFont, 8, Font.NORMAL);
            cellBottomFont = new Font(russianFont, 6, Font.NORMAL);
            cellLinkFont = new Font(russianFont, 6, Font.NORMAL);
            if (paramList.isLandscape())
                document = new Document(PageSize.A4.rotate());
            else
                document = new Document();
            document.setMargins(50, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(fname));
            document.open();
            addMetaData(document);
           } catch (Exception ee) { UniException.user(ee); }
        }

    @Override
    public void openPage(String title0, ArrayList<TableCol> cols0, int nrow0, boolean verticalHeader0) {
        super.openPage(title0, cols0, nrow0, verticalHeader0);
        }
    @Override
    public void savePage(){
        try {
            int ncol = cols();
            int nrow = rows();
            document.add(new Paragraph(title, titleFont));
            addEmptyLine(document);
            table = new PdfPTable(ncol);
            table.setWidthPercentage(100);
            int ww[] = new int[ncol];
            for (int i = 0; i < ww.length; i++)
                ww[i] = cols.get(i).size;
            table.setWidths(ww);
            for (int j = 0; j < ncol; j++) {
                addHeaderCell(table, cols.get(j));
                }
            for (int i = 0; i < nrow; i++) {
                for (int j = 0; j < ncol; j++) {
                    TableRowItem item = rowData.get(i);
                    addRegularCell(table, cols.get(j),item, data.get(i).get(j));
                }
            }
            document.add(table);
            addEmptyLine(document);
            for(String ss : bottoms)
                document.add(new Paragraph(ss, cellRegularFont));
            } catch (Exception ee) {
                UniException.user(ee);
                }
        }

    private void addHeaderCell(PdfPTable table, TableCol col) {
        Phrase phrase = new Phrase(col.name, cellHeaderFont);
        if (col.linkIndex!=0)
            phrase.add(new Chunk(""+col.linkIndex,cellLinkFont).setTextRise(5f));
        PdfPCell cell = new PdfPCell(phrase);
        if (verticalHeader) cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (col.hexBackColor!=0)
            cell.setBackgroundColor(new BaseColor(col.hexBackColor));
        //cell.setPaddingBottom(3);
        table.addCell(cell);
        }

    @Override
    public void saveReport() throws UniException {
        try{
            document.close();
            } catch (Exception ee) {
                UniException.user(ee);
                }
        }

    protected void addRegularCell(PdfPTable table, TableCol col, TableRowItem item, TableCell tcell) {
        PdfPCell cell = new PdfPCell(new Phrase(tcell.value, cellRegularFont));
        cell.setFixedHeight(item.nLines*10+3);
        //cell.setPaddingBottom(3);
        int align=TableCol.AlignLeft;
        switch (col.align){
            case TableCol.AlignCenter: align = Element.ALIGN_CENTER; break;
            case TableCol.AlignLeft: align = Element.ALIGN_LEFT; break;
            case TableCol.AlignRight: align = Element.ALIGN_RIGHT; break;
            }
        cell.setHorizontalAlignment(align);

        if (tcell.selected)
            cell.setBackgroundColor(new BaseColor(0xe0e0e0));
        else
        if (tcell.hexBackColor!=0)
            cell.setBackgroundColor(new BaseColor(tcell.hexBackColor));
        table.addCell(cell);
        }

    private void addMetaData(Document document) {
        document.addTitle(title);
        document.addSubject("ВТ НГТУ");
        document.addAuthor("Романов Е.Л.");
        document.addCreator("Романов Е.Л.");
        }
    private void addEmptyLine(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
        }
    public static void generate(TableData generator,String ext) throws UniException {
        ArrayList<TableCol> cols = new ArrayList<>();
        for(int i=0;i<10;i++){
            cols.add(new TableCol("name"+i,20+i*4));
            }
        generator.openReport("Тест","aaa."+ext);
        generator.openPage("Тест 1",cols,10,true);
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                generator.setCellValue(i,j,i*j);
                generator.setCellBackColor(i,j,0xFFFFFFFF);
                }
            }
        generator.savePage();
        generator.openPage("Тест 2",cols,20,false);
        for(int i=0;i<20;i++){
            for(int j=0;j<10;j++){
                generator.setCellValue(i,j,i+j);
                if ((i+j)%2==0)
                    generator.setCellBackColor(i,j,0xFFFFFFFF);
                else
                    generator.setCellBackColor(i,j,0xFFE0E0E0);
                //generator.setCellSelected(i,j,(i+j)%2==0);
                }
            }
        generator.savePage();
        generator.saveReport();
        }
    public  String getPdfContent(String pdfFile) throws IOException{
        PdfReader reader = new PdfReader(pdfFile);
        StringBuffer sb = new StringBuffer();
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            sb.append(strategy.getResultantText());
            }
        reader.close();
        return sb.toString();
        }
    public static void main(String sss[]) throws Exception {
        //generate(new TablePDF(),"pdf");
        //generate(new TableExcel(),"xls");
        //generate(new TableHTML(),"html");
        String ss = new TablePDF().getPdfContent("акт.pdf");
        System.out.println(ss);
    }

}
