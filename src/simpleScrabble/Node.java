/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleScrabble;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mfyildirim97
 */
public class Node {
    public static void main(String[] args) {
        //System.out.println("Mainde");
        
        
        JFrame myFrame = new JFrame("Oyun Kurma Ekranı");
        myFrame.setSize(600, 600);
        myFrame.setLocation(300, 100);
        myFrame.setContentPane(new Jpanel1(myFrame));
        myFrame.setVisible(true);    
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // if you want add your jcomponent
        //myFrame.getContentPane().add(yourJComponent);
        
    }
}
