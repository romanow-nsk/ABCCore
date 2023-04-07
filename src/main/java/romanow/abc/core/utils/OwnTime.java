package romanow.abc.core.utils;

import com.thoughtworks.xstream.XStream;
import lombok.Getter;
import romanow.abc.core.I_ExcelRW;
import romanow.abc.core.I_XStream;
import romanow.abc.core.mongo.DAO;
import romanow.abc.core.mongo.I_MongoRW;

public class OwnTime extends DAO implements I_XStream, I_MongoRW, I_ExcelRW {
    @Getter private long timeInMS=0;
    public OwnTime(long timeInMS) {
        this.timeInMS = timeInMS;
        }
    public OwnTime(){}
    public OwnTime(int hh, int mm, int ss) {
        timeInMS = ((hh*60L+mm)*60+ss)*1000;
        }
    @Override
    public void setAliases(XStream xs) {
        xs.alias("Time",OwnTime.class);
        xs.useAttributeFor("timeInMS",OwnDateTime.class);
        }
    public String toString(){
        long tt = timeInMS/1000;
        String out = String.format(":%02d",tt%60);
        tt/=60;
        out = String.format(":%02d",tt%60)+out;
        tt/=60;
        out = ""+tt+out;
        if (timeInMS%1000!=0)
            out+=String.format(".%04d",timeInMS%1000);
        return out;
        }
    public double toXLSFormat(){
        return timeInMS==0 ? 0 : 24.*60*60*1000/timeInMS;
        }
    public static double toXLSFormat(long timeInMS){
        return timeInMS==0 ? 0 : 24.*60*60*1000/timeInMS;
        }
    public void fromXLSFormat(double dd){
        timeInMS = (long) (dd*24.*60*60*1000);
        }
    public static long fromXLSFormat2(double dd){
        return (long) (dd*24.*60*60*1000);
        }
    public final static void main(String ss[]){
        System.out.println(new OwnTime(1,2,56));
        System.out.println(new OwnTime(34523533));
        }
}
