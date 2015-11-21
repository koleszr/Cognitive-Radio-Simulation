package hu.bme.cr;

import hu.bme.cr.utilities.UtilityConstants;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        CRSystem system = new CRSystem();
        
        system.init();
        system.playInitPhase();
        system.playSetPhase();
        
        for (int i = 0; i < UtilityConstants.ROUNDS; i++) {
        	system.playDecidePhase(i);
        }
        
        system.endGame();
    }
}
