/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EarTraining;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author jilli
 */
public class EarTraining {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        EarTrainingGUI earTraining = new EarTrainingGUI((int)screenSize.getWidth(), (int)screenSize.getHeight());
        EarTrainingGUI earTraining = new EarTrainingGUI(500, 500);
//        EarTrainingGUI earTraining = new EarTrainingGUI(1600, 900);
    }
    
}
