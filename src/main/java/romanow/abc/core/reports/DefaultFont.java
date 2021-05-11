package romanow.abc.core.reports;

import org.apache.poi.ss.usermodel.Font;

public class DefaultFont implements Font {
    @Override
    public void setFontName(String s) { }
    @Override
    public String getFontName() {
        return "Calibri"; }
    @Override
    public void setFontHeight(short i) { }
    @Override
    public void setFontHeightInPoints(short i) { }
    @Override
    public short getFontHeight() {
        return 11; }
    @Override
    public short getFontHeightInPoints() {
        return 11; }
    @Override
    public void setItalic(boolean b) {}
    @Override
    public boolean getItalic() {
        return false; }
    @Override
    public void setStrikeout(boolean b) { }
    @Override
    public boolean getStrikeout() {
        return false; }
    @Override
    public void setColor(short i) { }
    @Override
    public short getColor() {
        return 0; }
    @Override
    public void setTypeOffset(short i) { }
    @Override
    public short getTypeOffset() {
        return 0; }
    @Override
    public void setUnderline(byte b) { }
    @Override
    public byte getUnderline() {
        return 0; }
    @Override
    public int getCharSet() {
        return 0; }
    @Override
    public void setCharSet(byte b) { }
    @Override
    public void setCharSet(int i) { }
    @Override
    public short getIndex() {
        return 0; }
    @Override
    public void setBoldweight(short i) { }
    //@Override
    //public void setBold(boolean b) { }
    @Override
    public short getBoldweight() {
        return 0; }
    //@Override
    //public boolean getBold() {
    //    return false; }
}
