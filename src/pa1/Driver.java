package pa1;
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
        //--myComment Network is instantiated, where --1 is printed as a first step during instantiation

    	Network objNetwork = new Network("network");            /* Activate the network */
        //--myComment objNetwork instance runs as a thread, where --2 is printed as a first step in run() method 
        objNetwork.start();

        //--myComment Server is instantiated, where --3 is printed as a first step during instantiation
        Server objServer = new Server();
        objServer.start();

        Client objClientSend = new Client("sending");
        Client objClientReceiving = new Client("receiving");
        
        objClientSend.start();     
        objClientReceiving.start();

        System.out.println("\n PING: last statement in Driver");
   
        //--myComment    order of threads launch/exit --based on printouts
        //--myComment    launch -- 1.Network 2.Server 3.Client.sending 4.Client.receiving
        //--myComment    terminate -- 1.Client.sending 2.Client.receiving 3.Server 4.Network (last)
    }
}
