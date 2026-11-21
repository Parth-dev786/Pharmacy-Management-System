package com.college.pharmacy;
import javax.swing.*;
public class PharmacyApp {
    public static void main(String[] args){
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}
        DataStore store;
        try{store=new DataStore();}catch(Exception ex){JOptionPane.showMessageDialog(null,ex.getMessage(),"Startup error",JOptionPane.ERROR_MESSAGE);return;}
        new ExpiryMonitor(store).start();
        SwingUtilities.invokeLater(()->new PharmacyFrame(store).setVisible(true));
    }
}
