package romanow.abc.core.utils;

import romanow.abc.core.mongo.DAO;

public class MoneySum extends DAO {
    private int money=0;
    transient private I_Money back=null;
    public MoneySum(){}
    public MoneySum(int sum){
        money = sum;
        }
    public MoneySum(int sum, I_Money bb){
        money = sum;
        back = bb;
    }
    public int getSum(){ return money; }
    public MoneySum(int rr, int kk){
        money = rr*100+kk;
    }
    public void setRub(int vv){
        money = money%100 + vv*100;
        if (back!=null)
            back.changed(money);
        }
    public void setKop(int vv){
        money = money/100*100 + vv;
        if (back!=null)
            back.changed(money);
        }
    public int getRub(){
        return money/100;
        }
    public int getKop(){
        return money%100;
        }
    public String toString(){
        if (money%100==0)
            return String.format("%dр.",money/100);
        return String.format("%d.%2dр.",money/100,money%100);
        }
    public MoneySum add(MoneySum two){
        money+=two.money;
        if (back!=null)
            back.changed(money);
        return this;
        }
    public MoneySum add(MoneySum two, boolean sub){
        if (sub)
            money-=two.money;
        else
            money+=two.money;
        if (back!=null)
            back.changed(money);
        return this;
        }
    public MoneySum setDiff(MoneySum two, MoneySum xx){
        money = two.money - xx.money;
        if (back!=null)
            back.changed(money);
        return this;
        }
    public MoneySum sub(MoneySum two){
        money-=two.money;
        if (back!=null)
            back.changed(money);
        return this;
        }
    public MoneySum mul(double kk){
        money=(int)(money*kk);
        if (back!=null)
            back.changed(money);
        return this;
        }
    public MoneySum div(double kk){
        money=(int)(money/kk);
        if (back!=null)
            back.changed(money);
        return this;
        }
    public void setSum(int sum0){
        money = sum0;
        if (back!=null)
            back.changed(money);
        }
    public void clear(){
        money=0;
        if (back!=null)
            back.changed(money);
        }
    //------------------------------------------------------------------------------------------------------
    @Override
    public String toStringValue() {
        return ""+money;
        }
    @Override
    public void parseValue(String ss) throws Exception {
        money = Integer.parseInt(ss);
        }

    public static void main(String ss[]){
        System.out.println(new MoneySum(125,6));
    }
}
