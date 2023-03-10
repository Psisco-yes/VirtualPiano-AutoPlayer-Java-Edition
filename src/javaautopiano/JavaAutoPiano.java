package javaautopiano;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

class mainPrograme extends Thread{
    JPanel panel;
    JTextArea textarea;
    JTextField textfield2;
    
    public mainPrograme(JPanel panele, JTextArea textareae, JTextField textfielde2){
        this.panel = panele;
        this.textarea = textareae;
        this.textfield2 = textfielde2;
    }
    
    public boolean isSymbol(char ch){
        boolean out = false;
        switch(ch){
            case '!', '@', '#', '$', '%', '^', '&', '*', '(' -> out = true;
        }
        return out;
    }
    
    public int convertChar(char ch){
        int out = -1;
        switch(ch){
            case '!':
                out = 49;
                break;
            case '@':
                out = 50;
                break;
            case '#':
                out = 51;
                break;
            case '$':
                out = 52;
                break;
            case '%':
                out = 53;
                break;
            case '^':
                out = 54;
                break;
            case '&':
                out = 55;
                break;
            case '*':
                out = 56;
                break;
            case '(':
                out = 57;
                break;
        }
        return out;
    }
    
    @Override
    public void run(){
        String sheet = textarea.getText();
        int BPM;
        try {
            BPM = Integer.parseInt(textfield2.getText());
        } catch (NumberFormatException exp) {
            BPM = 100;
        }
        int qnote = 60000 / BPM;
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(panel, ex);
        }
        try{
            JOptionPane.showMessageDialog(panel, "Playing in 5s.");
            Thread.sleep(5000);
        } catch (InterruptedException ex){
            JOptionPane.showMessageDialog(panel, ex);
        }
        for(int i=0;i<sheet.length();i++){
            char key = sheet.charAt(i);
            switch(key){
                case '[' -> {
                    int j = 1;
                    ArrayList<Character> keys = new ArrayList<>();
                    while(sheet.charAt(i+j) != ']'){
                        keys.add((char)sheet.charAt(i+j));
                        j++;
                    }
                    try {
                        Thread.sleep(qnote/4);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(panel, ex); 
                    }
                    for (char key1 : keys) {
                        if(Character.compare(key1, ' ') == 0) try {
                            Thread.sleep(qnote/6);
                        } catch (InterruptedException ex) {
                            JOptionPane.showMessageDialog(panel, ex);
                        }
                        int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key1);
                        boolean shift = false;
                        if(Character.isUpperCase(key1) || isSymbol(key1)){
                            shift = true;
                            robot.keyPress(KeyEvent.VK_SHIFT);
                            if(isSymbol(key1)){
                                keyCode = convertChar(key1);
                            }
                        }
                        robot.keyPress(keyCode);
                        robot.keyRelease(keyCode);
                        if(shift){
                            robot.keyRelease(KeyEvent.VK_SHIFT);
                        }
                    }
                    try {
                        Thread.sleep(qnote/4);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(panel, ex); 
                    }
                    i += j;
                }

                case ' ' -> {
                        try {
                            Thread.sleep(qnote/2);
                        } catch (InterruptedException ex) {
                           JOptionPane.showMessageDialog(panel, ex); 
                        }
                    }

                case '|' -> {
                    try {
                        Thread.sleep(qnote);
                    } catch (InterruptedException ex) {

                    }
                }

                default -> {
                    try {
                        Thread.sleep(qnote/4);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(panel, ex);
                    }
                    int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key);
                    boolean shift = false;
                    if(Character.isUpperCase(key) || isSymbol(key)){
                        shift = true;
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        if(isSymbol(key)){
                            keyCode = convertChar(key);
                        }
                    }
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    if(shift){
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    }
                    try {
                        Thread.sleep(qnote/4);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(panel, ex);
                    }
                }

            }
        }
        JOptionPane.showMessageDialog(panel, "Finished!");
    }
}

public class JavaAutoPiano extends Thread{
    private JFrame frame;
    private JPanel panel;
    private JButton button;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JLabel label;
    private JLabel label2;
    private JLabel label3;
    private JTextArea textarea;
    private JTextField textfield;
    private JTextField textfield2;
    private JScrollPane scroll;
    private mainPrograme wontek = null;
    private Thread watek = null;
    private boolean canBeStopped = false;
    
    public JavaAutoPiano(){
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("Paste the music:");
        label2 = new JLabel("File name:");
        label3 = new JLabel("BPM:");
        textarea = new JTextArea(7,1);
        textarea.setLineWrap(true);
        scroll = new JScrollPane(textarea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textfield = new JTextField();
        textfield.setMaximumSize(new Dimension(10000,50));
        textfield2 = new JTextField();
        textfield2.setMaximumSize(new Dimension(10000,50));
        button4 = new JButton("Pause");
        button4.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               try{
                    if(watek.isAlive() && canBeStopped){
                        watek.suspend();
                        canBeStopped = false;
                        button4.setText("Resume");
                        JOptionPane.showMessageDialog(panel, "Playback suspended.");
                    } else {
                         try {
                            JOptionPane.showMessageDialog(panel, "Resuming in 2s.");
                            Thread.sleep(2000);
                            button4.setText("Pause");
                            watek.resume();
                            canBeStopped = true;
                         } catch(Exception ex){
                            JOptionPane.showMessageDialog(panel, "Cannot stop playback.");
                         }
                    }
               } catch(Exception exc){
                   
               }
           }
        });
        button = new JButton("Start");
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!"".equals(textarea.getText())){
                    try{ 
                        if(watek.isAlive())watek.stop();
                    } catch (Exception ex){

                    }
                    wontek = new mainPrograme(panel, textarea, textfield2);
                    watek = new Thread(wontek);
                    try{
                        button4.setText("Pause");
                        watek.start();
                        canBeStopped = true;
                    } catch(Exception ex){
                        watek.stop();
                        JOptionPane.showMessageDialog(panel, "Playback stopped.");
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "No music pasted.");
                }
            }
        });
        button2 = new JButton("Save");
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = textfield.getText();
                String sheet = textarea.getText();
                PrintWriter output = null;
                try {
                    output = new PrintWriter(new FileWriter(title + ".txt"));
                    output.print(sheet);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "File error: " + ex.toString());
                } finally {
                    JOptionPane.showMessageDialog(panel, "Saved successfully. ");
                    output.close();
                }
            }
        });
        button3 = new JButton("Load");
        button3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = textfield.getText();
                String sheet = "";
                BufferedReader input = null;
                String l;
                try {
                    input = new BufferedReader(new FileReader(title + ".txt"));
                    while((l = input.readLine()) != null){
                        sheet += l;
                    }
                    textarea.setText(sheet);
                    JOptionPane.showMessageDialog(panel, "Loaded successfully. ");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, ex.toString());
                } finally {
                    if(input != null) try {
                        input.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(panel, ex);
                    }
                }
            }
        });
        
        
        panel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(scroll);
        panel.add(label3);
        panel.add(textfield2);
        panel.add(button);
        panel.add(button4);
        panel.add(label2);
        panel.add(textfield);
        panel.add(button2);
        panel.add(button3);
        
        
        frame.add(panel,BorderLayout.CENTER);
        frame.setMinimumSize(new Dimension(300,400));
        frame.setMaximumSize(new Dimension(300,400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setTitle("AutoPiano");
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new JavaAutoPiano();
    }
}
