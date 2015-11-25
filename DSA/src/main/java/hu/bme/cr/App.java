package hu.bme.cr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import hu.bme.cr.utilities.UtilityConstants;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        
        System.out.print("Automatic or manual simulation? (1 - automatic, 2 - manual)");
        Scanner scanner = new Scanner(System.in);
        
        
        if (Integer.valueOf(scanner.nextLine()) == 1) {
        	System.out.print("Name of the file to read simulation init params from: ");
        	String fileName = scanner.nextLine();
        	
        	try (BufferedReader br = new BufferedReader(new FileReader(new File(UtilityConstants.SIMULATION_PARAM_FILE + fileName)))) {
        		int n = Integer.valueOf(br.readLine());
        		String line = null;
        		
				while ((line = br.readLine()) != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(line + "\n");
					
					for (int i = 0; i < n; i++) {
						sb.append(br.readLine() + "\n");
					}
					
					String params = sb.toString();
					System.out.println(params);
					CRSystem system = new CRSystem(params);	
					
					play(system);
				}
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
        }
        else {
        	CRSystem system = new CRSystem();
        	
        	play(system);      	
        }
        
        scanner.close();
        
    }
    
    private static void play(CRSystem system) {
    	system.init();
    	system.playInitPhase();
    	system.playSetPhase();
    	
    	for (int i = 0; i < UtilityConstants.ROUNDS; i++) {
    		system.playDecidePhase(i);
    	}
    	
    	system.endGame();  
    }
}
