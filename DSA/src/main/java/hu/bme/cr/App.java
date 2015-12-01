package hu.bme.cr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
	
	private static Scanner scanner;
	
	private static Properties props;
	
    public static void main( String[] args ){
        init();
    	
        System.out.print("Automatic or manual simulation? (1 - automatic, 2 - manual) ");
        
        
        
        if (Integer.valueOf(scanner.nextLine()) == 1) {
        	autoSimulation();
        }
        else {
        	CRSystem system = createSimulation();
        	
        	play(system);      	
        }
        
        scanner.close();
    }
    
    private static void init() {
    	scanner = new Scanner(System.in);
    	ClassLoader loader = Thread.currentThread().getContextClassLoader();
    	props = new Properties();
    	
    	try(InputStream stream = loader.getResourceAsStream("simulation.properties")) {
    		props.load(stream);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void autoSimulation() {
    	System.out.print("Name of the file to read simulation init params from: ");
    	String fileName = scanner.nextLine();
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(new File(props.getProperty("SIMULATION_PARAM_DIR") + fileName)))) {
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
				CRSystem system = createSimulationWithParams(params);
				
				play(system);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    private static void play(CRSystem system) {
    	system.init();
    	system.playInitPhase();
    	system.playSetPhase();
    	
    	for (int i = 0; i < Integer.valueOf(props.getProperty("ROUNDS")); i++) {
    		system.playDecidePhase(i);
    	}
    	
    	system.endGame();  
    }
    
    private static CRSystem createSimulation() {
    	if (props.getProperty("SIMULATION_MODE").equals("NORMAL")) {
    		return new CRSystem();
    	}
    	else if (props.getProperty("SIMULATION_MODE").equals("CONTENTION")) {
    		return new CRContentionSimulation();
    	}
    	
    	return null;
    }
    
    private static CRSystem createSimulationWithParams(String params) {
    	if (props.getProperty("SIMULATION_MODE").equals("NORMAL")) {
    		return new CRSystem(params);
    	}
    	else if (props.getProperty("SIMULATION_MODE").equals("CONTENTION")) {
    		return new CRContentionSimulation(params);
    	}
    	
    	return null;
    }
}
