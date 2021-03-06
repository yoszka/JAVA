/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakthesleep;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Tomek
 */
public class MainClass extends javax.swing.JFrame {
    //private Thread thread;
    MyTimerTask timerTask;// = new MyTimerTask();
    private Timer timer1;// = new Timer();
    private int licznik = 0;
    private static int coIleMinut = 1;
    private boolean timerWorks = false;
    

    /**
     * Creates new form MainClass
     */
    public MainClass() {        
        initComponents();        
        SpinnerNumberModel numSpin = new SpinnerNumberModel(1, 1, 60,1);      // value, min, max, step
        jSpinner1.setModel(numSpin);
    }
    
    /**
     * Task to do when timer runs
     */
    class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            try
            {
                licznik++;  
                int minLeft = ((coIleMinut*60) - licznik)/60;
                int secLeft = ((coIleMinut*60) - licznik)%60;                                                                

                System.out.println("licznik: "+licznik);
                jLabel1.setText(minLeft+":"+((secLeft<10)?"0":"")+secLeft);
                if(licznik == (coIleMinut*60))
                {
                    licznik = 0;
                    PointerInfo pi = MouseInfo.getPointerInfo();
                    Point p = pi.getLocation();                     // Automatic import Alt+Shift+I
                    Robot robot = new Robot();
                    robot.mouseMove((int)p.getX(), (int)p.getY());
                    System.out.println("Event kursora myszy wysłany. Pozycja X: "+ p.getX()+", Y: "+ p.getY());
                }                                     
            }
            catch(HeadlessException | AWTException e){ 
                throw new RuntimeException("Robot się zepsuł", e);
            } 
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jToggleButton1.setText("START");
        jToggleButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton1StateChanged(evt);
            }
        });

        jLabel1.setText("0:00");

        jSpinner1.setValue(1);
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSpinner1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton1StateChanged
        if(jToggleButton1.isSelected())
        {
            if(!timerWorks)                                                      // Run time loop only when stopped
            {
                timerWorks = true;
                jToggleButton1.setText("STOP");
                                
                try
                {
                    timer1 = new Timer();
                    timerTask = new MyTimerTask();
                    System.out.println("Timer sheduled. timer1 = "+timer1);
                    timer1.schedule(timerTask, 1000, 1000);
                }
                catch(Exception e){ 
                    timerWorks = false;
                    jToggleButton1.setText("START");
                    jToggleButton1.setSelected(false);
                    throw new RuntimeException("Timer się zepsuł", e);
                }                 
            }
            
        }
        else
        {
            if(timerWorks)
            {
                jToggleButton1.setText("START");  
                if(timer1 != null)
                {
                    timer1.cancel();
                }
                System.out.println("Timer canceled. timer1 = "+timer1);
                timerWorks = false;
                jLabel1.setText("0:00");
                licznik = 0;
            }
        }
    }//GEN-LAST:event_jToggleButton1StateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        coIleMinut = Integer.parseInt(jSpinner1.getValue().toString());
        
    }//GEN-LAST:event_jSpinner1StateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainClass().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
