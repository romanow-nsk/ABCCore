/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.core;

/**
 *
 * @author romanow
 */
public interface I_Notify {
    public void notify(String mes);
    public boolean isFinish();
    public void setProgress(int proc);
    public void onClose();
}
