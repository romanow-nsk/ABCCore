package romanow.abc.core.reports;

import romanow.abc.core.mongo.DAO;

public class TableCell extends DAO {
    public int row=0;
    public int col=0;
    public String value="";
    public int hexBackColor=0;
    public int hexTextColor=0;
    public boolean selected=false;
    public int textSize=0;
    public TableCell(){}
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
