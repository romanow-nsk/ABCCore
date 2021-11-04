package romanow.abc.core.script;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;

public class MyBasic extends JFrame implements WindowListener{
    //-------------------- Компилятор и экзекутор -----------------------------
    public void windowClosed(WindowEvent e){ dispose(); System.exit(0); }
    public void windowClosing(WindowEvent e){ dispose(); System.exit(0); }
    public void windowActivated(WindowEvent e){ }
    public void windowDeactivated(WindowEvent e){ }
    public void windowDeiconified(WindowEvent e){ }
    public void windowIconified(WindowEvent e){ }
    public void windowOpened(WindowEvent e){ }
    JFrame THIS=this;
    Syn SS=new Syn();
    Exec exec = new Exec();
    public MyBasic() {
        setTitle("Компилятор+интерпретатор Basic-подобного языка");
        this.addWindowListener(this);
        try {
            jbInit();
        } catch (Exception ex) {ex.printStackTrace();}
    resize(450,350);
    show();
    }

    void showVM(){
        if (exec.IP>=exec.PROG.size()) exec.init();
        IP.setText(""+exec.IP);
        COM.setText((String)exec.PROG.get(exec.IP));
        STACK.append("IP:"+exec.IP+" "+(String)exec.PROG.get(exec.IP)+"\nСтек: ");
        for (int i=exec.sp; i>=0; i--) STACK.append(""+exec.ST[i]+" ");
        STACK.append("\n");
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        jButton1.setBounds(new Rectangle(235, 18, 101, 23));
        jButton1.addActionListener(new ActionListener(){
            //-----------------------------------------------------------------
            public void actionPerformed(ActionEvent ee){
                String ss;
                do {
                    if (step.isSelected()) showVM();
                    ss=exec.oneStep(IN.getText());
                    if (ss!=null) STACK.append(ss);
                } while (!exec.suspend && exec.IP<exec.PROG.size() && !step.isSelected());
            }
        });
        COM.setBounds(new Rectangle(79, 44, 153, 23));
        jLabel2.setText("Команда");
        jLabel2.setBounds(new Rectangle(24, 44, 66, 23));
        STACK.setBounds(new Rectangle(24, 73, 409, 212));
        jButton2.setBounds(new Rectangle(321, 47, 111, 23));
        jButton2.addActionListener(new ActionListener(){
            //-----------------------------------------------------------------
            public void actionPerformed(ActionEvent ee){
                FileDialog dlg=new FileDialog(THIS,"Текст программы",FileDialog.LOAD);
                dlg.setFile("*.txt");
                dlg.show();
                String ss=dlg.getDirectory()+dlg.getFile();
                String s2=ss.substring(0,ss.indexOf("."));
                SS.compile(s2);
                STACK.setText("");
                if (SS.nerror!=0) STACK.append("Ошибки:\n"+SS.story);
                else {
                    exec.load(s2 + "_bin.txt");
                    STACK.append("Внутрениий код\n");
                    for (int i = 0; i < exec.PROG.size(); i++)
                        STACK.append("" + i + ": " + (String) exec.PROG.get(i) +"\n");
                }
            }
        });
        jLabel1.setText("Адрес");
        jLabel1.setBounds(new Rectangle(27, 22, 73, 19));
        IP.setBounds(new Rectangle(78, 18, 39, 22));
        button1.setBounds(new Rectangle(236, 45, 81, 23));
        button1.addActionListener(new ActionListener(){
            //-----------------------------------------------------------------
            public void actionPerformed(ActionEvent ee){
                exec.init();
                STACK.setText("");
            }
        });
        jLabel3.setText("Ввод");
        jLabel3.setBounds(new Rectangle(122, 20, 48, 20));
        IN.setText("0");
        IN.setBounds(new Rectangle(153, 19, 78, 23));
        step.setSelected(false);
        step.setText("по шагам");
        step.setBounds(new Rectangle(340, 20, 90, 21));
        this.getContentPane().add(jLabel1);
        this.getContentPane().add(jLabel2);
        this.getContentPane().add(STACK);
        this.getContentPane().add(jLabel3);
        this.getContentPane().add(jButton2);
        this.getContentPane().add(IN);
        this.getContentPane().add(jButton1);
        this.getContentPane().add(button1);
        this.getContentPane().add(step);
        this.getContentPane().add(COM);
        this.getContentPane().add(IP);

    }
    public static void main(String[] args) {
        new MyBasic();
   }

    Button jButton1 = new Button("Выполнение");
    JTextField IP = new JTextField();
    JLabel jLabel1 = new JLabel();
    JTextField COM = new JTextField();
    JLabel jLabel2 = new JLabel();
    TextArea STACK = new TextArea();
    Button jButton2 = new Button("Компиляция");
    Button button1 = new Button("Сброс");
    JLabel jLabel3 = new JLabel();
    JTextField IN = new JTextField();
    JCheckBox step = new JCheckBox();
}


