package romanow.abc.core.entity;

import romanow.abc.core.constants.ValuesBase;

public class StateEntity extends Entity{
    private int state= ValuesBase.StateUndefined;
    public int getState() {
        return state;}
    public void setState(int state) {
        this.state = state; }
    public StateEntity(long id0, int state) {
        super(id0);
        this.state = state;}
    public StateEntity(int state) {
        this.state = state; }
    public StateEntity(){}
}
