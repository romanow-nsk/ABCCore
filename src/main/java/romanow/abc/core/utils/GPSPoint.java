package romanow.abc.core.utils;

import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.EntityBack;

public class GPSPoint extends EntityBack {
    final static double gradus=111.12;
    private int state= ValuesBase.GeoNone;
    private double geoy=0;           // Широта
    private double geox=0;           // Долгота
    private OwnDateTime gpsTime = new OwnDateTime();
    public int state(){ return state; }
    public double geox() {
        return geox;
    }
    public double geoy() {
        return geoy;
    }
    public OwnDateTime geoTime(){ return gpsTime; }
    public void setCurrentTime(){ gpsTime = new OwnDateTime(); }
    public GPSPoint(){ state = ValuesBase.GeoNone; }
    public GPSPoint(String y0,String x0, boolean exact){
        try {
            geoy=fromStr(y0); geox=fromStr(x0);
            state = exact ? ValuesBase.GeoGPS : ValuesBase.GeoNet;
            } catch(Throwable ee){ state = ValuesBase.GeoNone; }
        }
    public GPSPoint(double y0,double x0, boolean exact){
        geoy=y0; geox=x0;
        state = exact ? ValuesBase.GeoGPS : ValuesBase.GeoNet;
        }
    public GPSPoint(double y0,double x0, boolean exact,long timeMs){
        geoy=y0; geox=x0;
        state = exact ? ValuesBase.GeoGPS : ValuesBase.GeoNet;
        gpsTime = new OwnDateTime(timeMs);
        }
    public int elapsedTimeInSec(){ return geoTime().elapsedTimeInSec(); }
    public GPSPoint(String crd,boolean exact){
        setCoord(crd,exact);
        }
    public void state(int st){
        state = st;
    }
    public boolean gpsValid(){
        return state != ValuesBase.GeoNone;
    }
    public void setCoord(double y, double x, boolean exact){
        geox=x;
        geoy=y;
        state = exact ? ValuesBase.GeoGPS : ValuesBase.GeoNet;
        }
    public GPSPoint  copy(){
        GPSPoint out = new GPSPoint(geoy,geox,true);
        out.state = state;
        out.gpsTime = new OwnDateTime(gpsTime.timeInMS());
        return out;
        }
    //----------- Конвертирование в формат ggmm.xxxxx (градусы-минуты-дробная часть)
    public static double fromStr(String ss) throws Throwable{
        double dd=0;
        dd=Double.parseDouble(ss);
        return dd;
        }
    public static String toStr(double dd){
        String ss=""+dd;
        int k=ss.indexOf(".")+5;
        if (k>ss.length()) k=ss.length();
        return ss.substring(0, k);
        }
    public String toStrX(){
        if (state == ValuesBase.GeoNone)
            return "";
        return toStr(geox);
        }
    public String toStrY(){
        if (state == ValuesBase.GeoNone)
            return "";
        return toStr(geoy);
        }
    public static String convert(double dd){
        String ss=""+(int)dd;
        dd-=(int)dd;
        dd*=60;
        int xx=(int)dd;
        if (xx<10) ss+="0"+xx; else ss+=""+xx;
        String s2=""+dd;
        int k=s2.indexOf(".");
        ss+=s2.substring(k);
        return ss;
        }
    public void copy(GPSPoint src){
        geox=src.geox;
        geoy=src.geoy;
        state = src.state;
        }
    public int diff(GPSPoint T){        // В метрах
        if (state== ValuesBase.GeoNone || T.state== ValuesBase.GeoNone)
            return -1;
        double dx=(geox-T.geox)*gradus*1000*Math.cos(Math.PI*geoy/180);
        double dy=(geoy-T.geoy)*gradus*1000;
        return (int)Math.sqrt(dy*dy+dx*dx);
        }
    public String toString(){
        if (state== ValuesBase.GeoNone)
            return "";
        return toStr(geoy)+","+toStr(geox)+"["+gpsTime+"]";
        }
    public String toShortString(){
        if (state== ValuesBase.GeoNone)
            return "";
        return toStr(geoy)+","+toStr(geox);
        }
    public String toFullString(){
        return super.toFullString()+toShortString();
        }
    public String getTitle(){
        if (state== ValuesBase.GeoNone)
            return "";
        return toStr(geoy)+","+toStr(geox);
    }
    public void moveY(double dd){
        geoy+=dd/1000/gradus;
    }
    public void moveX(double dd){
        geox+=dd/1000/gradus/Math.cos(Math.PI*geoy/180);
    }
    public void setCoord(String crd,boolean exact){
        state = ValuesBase.GeoNone;
        int k=crd.indexOf(",");
        if (k==-1) return;
        double v1=0,v2=0;
        try {
            v1=Double.parseDouble(crd.substring(0,k).trim());
            v2=Double.parseDouble(crd.substring(k+1).trim());
            setCoord(v1, v2,exact);
            } catch(Throwable ee){ state= ValuesBase.GeoNone; }
        }
    //------------------------------------------------------------------------------------------------------------------
    /*
    @Override
    public void putData(String prefix, org.bson.Document document, int level, I_MongoDB mongo) throws UniException {
        putDBValues(prefix,document,level,mongo);
        gpsTime.putData("a_",document,0,null);
    }
    @Override
    public void getData(String prefix, org.bson.Document res, int level, I_MongoDB mongo) throws UniException {
        getDBValues(prefix,res);
        gpsTime.getData("a_",res, 0, null);
        }
    //----------------- Импорт/экспорт Excel ------------------------------------------------------------
    @Override
    public void getData(Row row, ExCellCounter cnt) throws UniException{
        getXMLValues(row, cnt);
        gpsTime.getData(row, cnt);
        }
    @Override
    public void putData(Row row, ExCellCounter cnt) throws UniException{
        putXMLValues(row, cnt);
        gpsTime.putData(row, cnt);
        }
    @Override
    public void putHeader(String prefix, ArrayList<String> list) throws UniException{
        putXMLHeader(prefix,list);
        gpsTime.putHeader("a_",list);
    }
    */
    //--------------------------------------------------------------------------------------------------
    @Override
    public String toStringValue() {
        return toShortString()+"|"+gpsTime.timeInMS();
        }
    @Override
    public void parseValue(String ss) throws Exception {
        int idx=ss.indexOf("|");
        if (idx==-1)
            return;
        setCoord(ss.substring(0,idx),true);
        gpsTime = new OwnDateTime(Long.parseLong(ss.substring(idx+1)));
    }
}
