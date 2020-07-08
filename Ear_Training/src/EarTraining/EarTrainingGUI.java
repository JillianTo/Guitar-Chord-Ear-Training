/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EarTraining;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.KEY_LOCATION_RIGHT;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 *
 * @author jilli
 */
public class EarTrainingGUI {
    // primitives
    private static int windowWidth;
    private static int windowHeight;
    private static int chordNum = 6;
    private int attempts;
    private int correct;
    
    // sound objects
    private AudioInputStream audioInputStream;
    private Clip clip;
    
    // JSwing objects
    private JButton chkAnsBtn;
    private JButton optionsBtn;
    private JButton playBtn;
    private JLabel statsLbl;
    private JTextField ansFld;
    private JCheckBox aChk = new JCheckBox("A", true);
    private JCheckBox dChk = new JCheckBox("D", true);
    private JCheckBox eChk = new JCheckBox("E", true);
    private JCheckBox amChk = new JCheckBox("Am", true);
    private JCheckBox dmChk = new JCheckBox("Dm", true);
    private JCheckBox emChk = new JCheckBox("Em", true);
    private JCheckBox gChk = new JCheckBox("G", true);
    private JCheckBox cChk = new JCheckBox("C", true);
    private JCheckBox g7Chk = new JCheckBox("G7", true);
    private JCheckBox c7Chk = new JCheckBox("C7", true);
    private JCheckBox b7Chk = new JCheckBox("B7", true);
    private JCheckBox fMaj7Chk = new JCheckBox("Fmaj7", true);
    private JFrame options;
    private JFrame player;
    
    // other objects
    private String chord;
    private KeyListener spaceListener;
    private MouseListener chkListener;
    
    // constructor
    public EarTrainingGUI(int windowWidth, int windowHeight) {
        // initializations
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        attempts = 0;
        correct = 0;

        // instantiations
        chord = chooseChord();
        
        // audio instantiations 
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(chord+".wav").getAbsoluteFile());
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // JSwing instantiations
        chkAnsBtn = new JButton("Check Answer (Enter)");
        optionsBtn = new JButton("Options");
        playBtn = new JButton("Play Chord (Right Arrow)");
        statsLbl = new JLabel(correct + "/" + attempts + " correct (100%)");
        ansFld = new JTextField();
        options = new JFrame("Options");
        player = new JFrame("Single Sound Recognition");
        
        // setup audio system
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // check answers button setup
        chkAnsBtn.setBounds((int)(windowWidth*0.5), (int)(windowHeight*0.7), (int)(windowWidth*0.4), (int)(windowHeight*0.1));
        chkAnsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ansFld.getText().equals("cheat")) {
                    JOptionPane.showMessageDialog(player, chord);
                } else {
                    attempts++;
                    if(ansFld.getText().replaceAll(" ","").toUpperCase().equals(chord.replaceAll(" ","").toUpperCase())) {
                        chord = chooseChord();
                        clip.stop();
                        clip.close();
                        try {
                            audioInputStream = AudioSystem.getAudioInputStream(new File(chord+".wav").getAbsoluteFile());
                        } catch (UnsupportedAudioFileException ex) {
                            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            clip.open(audioInputStream);
                        } catch (LineUnavailableException ex) {
                            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ansFld.setText("");
                        correct++;
                        statsLbl.setText(correct + "/" + attempts + " correct (" + (correct*100/attempts) + "%)");
                        JOptionPane.showMessageDialog(player, "Correct!");
                    } else {
                        statsLbl.setText(correct + "/" + attempts + " correct (" + (correct*100/attempts) + "%)");
                        JOptionPane.showMessageDialog(player, "Incorrect, try again");
                    }
                }
            }
        });
        
        // options button setup
        optionsBtn.setBounds((int)(windowWidth*0.7), (int)(windowHeight*0.05), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        optionsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setVisible(true);
            }
        });
        
        // play button setup
        playBtn.setBounds((int)(windowWidth*0.25), (int)(windowHeight*0.1), (int)(windowWidth*0.4), (int)(windowHeight*0.3));
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clip.setMicrosecondPosition(0);
                clip.start();
            }
        });
        
        // statsLbl label setup
        statsLbl.setBounds((int)(windowWidth*0.6), (int)(windowHeight*0.8), (int)(windowWidth*0.3), (int)(windowHeight*0.1));
        
        // answer text field setup
        ansFld.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.7), (int)(windowWidth*0.4), (int)(windowHeight*0.1));
        ansFld.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    ansFld.setText("");
            }
        });
        
        // check boxes setup
        aChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.05), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        dChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.15), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        eChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.25), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        amChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.35), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        dmChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.45), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        emChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.55), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        gChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.65), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        cChk.setBounds((int)(windowWidth*0.05), (int)(windowHeight*0.75), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        g7Chk.setBounds((int)(windowWidth*0.25), (int)(windowHeight*0.05), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        c7Chk.setBounds((int)(windowWidth*0.25), (int)(windowHeight*0.15), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        b7Chk.setBounds((int)(windowWidth*0.25), (int)(windowHeight*0.25), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        fMaj7Chk.setBounds((int)(windowWidth*0.25), (int)(windowHeight*0.35), (int)(windowWidth*0.2), (int)(windowHeight*0.1));
        
        // plays audio when right arrow key is pressed
        spaceListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            // plays audio when right arrow key is pressed
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };
        
        // resets chord when options are changed
        chkListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                chord = chooseChord();
                clip.stop();
                clip.close();
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File(chord+".wav").getAbsoluteFile());
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip.open(audioInputStream);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EarTrainingGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
            
        };
        
        // add components to options frame
        options.add(aChk);
        options.add(dChk);
        options.add(eChk);
        options.add(amChk);
        options.add(dmChk);
        options.add(emChk);
        options.add(gChk);
        options.add(cChk);
        options.add(g7Chk);
        options.add(c7Chk);
        options.add(b7Chk);
        options.add(fMaj7Chk);
        
        // setup options frame
        options.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        options.setSize(windowWidth, windowHeight);
        options.setLayout(null);
        options.setVisible(false);
        
        // add components to player frame
        player.add(chkAnsBtn);
        player.add(optionsBtn);
        player.add(playBtn);
        player.add(statsLbl);
        player.add(ansFld);
        
        // add listeners
        ansFld.addKeyListener(spaceListener);
        player.addKeyListener(spaceListener);
        aChk.addMouseListener(chkListener);
        dChk.addMouseListener(chkListener);
        eChk.addMouseListener(chkListener);
        amChk.addMouseListener(chkListener);
        dmChk.addMouseListener(chkListener);
        emChk.addMouseListener(chkListener);
        gChk.addMouseListener(chkListener);
        cChk.addMouseListener(chkListener);
        g7Chk.addMouseListener(chkListener);
        c7Chk.addMouseListener(chkListener);
        b7Chk.addMouseListener(chkListener);
        fMaj7Chk.addMouseListener(chkListener);
        
        // setup player frame
        player.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player.setSize(windowWidth, windowHeight);
        player.setLayout(null);
        player.setVisible(true);
        player.getRootPane().setDefaultButton(chkAnsBtn);
        player.setFocusable(true);
        ansFld.requestFocusInWindow();
        
    }
    
    // chooses random audio file
    private String chooseChord() {
        // primitives
        int chordCount = 0;
        int randomNum;
        
        // select chord
        if(aChk.isSelected())
            chordCount++;
        if(dChk.isSelected())
            chordCount++;
        if(eChk.isSelected())
            chordCount++;
        if(amChk.isSelected())
            chordCount++;
        if(dmChk.isSelected())
            chordCount++;
        if(emChk.isSelected())
            chordCount++;
        if(gChk.isSelected())
            chordCount++;
        if(cChk.isSelected())
            chordCount++;
        if(g7Chk.isSelected())
            chordCount++;
        if(c7Chk.isSelected())
            chordCount++;
        if(b7Chk.isSelected())
            chordCount++;
        if(fMaj7Chk.isSelected())
            chordCount++;
        randomNum = (int)(Math.random()*chordCount) + 1;
        for(int i = 0; i < chordNum; i++) {
            if(aChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "A";
            }
            if(dChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "D";
            }
            if(eChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "E";
            }
            if(amChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "Am";
            }
            if(dmChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "Dm";
            }
            if(emChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "Em";
            }
            if(gChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "G";
            }
            if(cChk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "C";
            }
            if(g7Chk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "G7";
            }
            if(c7Chk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "C7";
            }
            if(b7Chk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "B7";
            }
            if(fMaj7Chk.isSelected()) {
                randomNum--;
                if(randomNum == 0)
                    return "Fmaj7";
            }
        }
        return "Error in chooseChord()";
    }   
}
