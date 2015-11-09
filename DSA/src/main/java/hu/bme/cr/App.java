package hu.bme.cr;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        CRSystem system = new CRSystem();
        
        system.init();
        system.printAll();
    }
}
