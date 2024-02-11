
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kerly Titus
 */
public class Driver {

    /** 
     * main class
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	 /*******************************************************************************************************************************************
    	  * TODO : implement all the operations of main class   																					*
    	  ******************************************************************************************************************************************/
        System.out.println("starting...");

        //--myComment Network is instantiated, where --1 is printed as a first step during instantiation 
    	Network objNetwork = new Network("network");            /* Activate the network */
        //--myComment objNetwork instance runs as a thread, where --2 is printed as a first step in run() method 
        objNetwork.start();


        //--myComment Server is instantiated, where --3 is printed as a first step during instantiation
        Server objServer = new Server();
        //--probably-> objServer.start()        
        /* Complete here the code for the main method ...*/

        
    }
}
