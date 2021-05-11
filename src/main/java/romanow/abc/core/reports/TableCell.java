package romanow.abc.core.reports;

public class TableCell {
    int row=0,col=0;
    String value="";
    int hexBackColor=0;
    int hexTextColor=0;
    boolean selected=false;
    int textSize;
    TableCell(){}
    public int getRow() {
        return row; }
    public int getCol() {
        return col; }
    public String getValue() {
        return value; }
    public int getHexBackColor() {
        return hexBackColor; }
    public int getHexTextColor() {
        return hexTextColor; }
    public boolean isSelected() {
        return selected; }
    public int getTextSize() {
        return textSize; }
}
