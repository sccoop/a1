
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** Client class
 *
 * @author Kerly Titus
 */

public class Client extends Thread{ 
    
    private static int numberOfTransactions;   		/* Number of transactions to process */
    private static int maxNbTransactions;      		/* Maximum number of transactions */
    private static Transactions [] transaction; 	/* Transactions to be processed */
    private static Network objNetworkClient;          	/* Client object to handle network operations */
    private String clientOperation;    				/* sending or receiving */
    
	/** Constructor method of Client class
 	 * 
     * @return 
     * @param
     */
     Client(String operation)
     { 
       if (operation.equals("sending"))
       {   //--9
           System.out.println("\n Initializing client sending application ...");
           numberOfTransactions = 0;
           maxNbTransactions = 100;
           transaction = new Transactions[maxNbTransactions];  
           objNetworkClient = new Network("client");
           clientOperation = operation;
           //--11 
           System.out.println("\n Initializing the transactions ... ");
           readTransactions();
           //--13
           System.out.println("\n Connecting client to network ...");
           String cip = objNetworkClient.getClientIP();
           if (!(objNetworkClient.connect(cip)))
           {   System.out.println("\n Terminating client application, network unavailable");
               System.exit(0);
           }
       	}
       else
    	   if (operation.equals("receiving"))
           {    //--14
    		   System.out.println("\n Initializing client receiving application ...");
    		   clientOperation = operation; 
           }
     }
           
    /** 
     * Accessor method of Client class
     * 
     * @return numberOfTransactions
     * @param
     */
     public int getNumberOfTransactions()
     {
         return numberOfTransactions;
     }
         
    /** 
     * Mutator method of Client class
     * 
     * @return 
     * @param nbOfTrans
     */
     public void setNumberOfTransactions(int nbOfTrans)
     { 
         numberOfTransactions = nbOfTrans;
     }
         
    /** 
     * Accessor method of Client class
     * 
     * @return clientOperation
     * @param
     */
     public String getClientOperation()
     {
         return clientOperation;
     }
         
    /** 
     * Mutator method of Client class
	 * 
	 * @return 
	 * @param operation
	 */
	 public void setClientOperation(String operation)
	 { 
	     clientOperation = operation;
	 }
         
    /** 
     * Reading of the transactions from an input file
     * 
     * @return 
     * @param
     */
     public void readTransactions()
     {
        Scanner inputStream = null;     /* Transactions input file stream */
        int i = 0;                      /* Index of transactions array */
        
        try
        {
        	inputStream = new Scanner(new FileInputStream("C:\\n" + //
            "ika_chychkova\\repos\\a1_concurrency\\a1\\PA1-code\\transaction.txt"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File transaction.txt was not found");
            System.out.println("or could not be opened.");
            System.exit(0);
        }
        while (inputStream.hasNextLine( ))
        {
            try
            {   transaction[i] = new Transactions();
                transaction[i].setAccountNumber(inputStream.next());            /* Read account number */
                transaction[i].setOperationType(inputStream.next());            /* Read transaction type */
                transaction[i].setTransactionAmount(inputStream.nextDouble());  /* Read transaction amount */
                transaction[i].setTransactionStatus("pending");                 /* Set current transaction status */
                i++;
            }
             catch(InputMismatchException e)
            {
                System.out.println("Line " + i + "file transactions.txt invalid input");
                System.exit(0);
            }
            
        }
        setNumberOfTransactions(i);		/* Record the number of transactions processed */
        //--12
        System.out.println("\n DEBUG : Client.readTransactions() - " + getNumberOfTransactions() + " transactions processed");
        
        inputStream.close( );

     }
     
    /** 
     * Sending the transactions to the server 
     * 
     * @return 
     * @param
     */
    //--myComment: probable entry point, sends all transactions from 'Client.transaction' array to 'Network.inComingPacket' array using objNetworkClient.send()
     public void sendTransactions()
     {
         int i = 0;     /* index of transaction array */
         
         while (i < getNumberOfTransactions())
         {  
            // while( objNetworkClient.getInBufferStatus().equals("full") );     /* Alternatively, busy-wait until the network input buffer is available */
            Thread.yield();                                 	
            transaction[i].setTransactionStatus("sent");   /* Set current transaction status */
           //--16
            System.out.println("\n DEBUG : Client.sendTransactions() - sending transaction on account " + transaction[i].getAccountNumber());
            
            objNetworkClient.send(transaction[i]);                            /* Transmit current transaction */
            i++;
         }
         
    }
         
 	/** 
  	 * Receiving the completed transactions from the server
     * 
     * @return 
     * @param transact
     */
     public void receiveTransactions(Transactions transact)
     {
         int i = 0;     /* Index of transaction array */
         
         while (i < getNumberOfTransactions())
         {     
        	 // while( objNetworkClient.getOutBufferStatus().equals("empty"));  	/* Alternatively, busy-wait until the network output buffer is available */
            Thread.yield();                                                            	
            objNetworkClient.receive(transact);                               	/* Receive updated transaction from the network buffer */
            
            System.out.println("\n DEBUG : Client.receiveTransactions() - receiving updated transaction on account " + transact.getAccountNumber());
            
            System.out.println(transact);                               	/* Display updated transaction */    
            i++;
         } 
    }
     
    /** 
     * Create a String representation based on the Client Object
     * 
     * @return String representation
     * @param 
     */
     public String toString() {
    	 return ("\n client IP " + objNetworkClient.getClientIP() + " Connection status" + objNetworkClient.getClientConnectionStatus() + "Number of transactions " + getNumberOfTransactions());
     }
    
    /** Code for the run method
     * 
     * @return 
     * @param
     */
    public void run()
    {   
        //--15 (line below has been added by me)
    	System.out.println("\n DEBUG : Client.run() - starting client thread");
        
    	Transactions transact = new Transactions();
    	long sendClientStartTime, sendClientEndTime, receiveClientStartTime, receiveClientEndTime;
        sendClientStartTime = System.currentTimeMillis();
        receiveClientStartTime = System.currentTimeMillis();

        // --19 line doesn't exist yet (should start the "client receiving thread")
        //--myComment run server while it's network status is connected
        if(getClientOperation().equals("sending"))
        {
            try {
                 sendTransactions();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            objNetworkClient.setClientConnectionStatus("inactive");

            sendClientEndTime = System.currentTimeMillis();
            System.out.println("\n Terminating Client "+ getClientOperation() + " thread - " + " Running time " + (sendClientEndTime - sendClientStartTime) + " milliseconds");
        }
        if(getClientOperation().equals("receiving"))
        {
            try {
                receiveTransactions(transact);
            } catch (Exception e) {
                e.printStackTrace();
            }
            objNetworkClient.disconnect("192.168.2.0");

            receiveClientEndTime = System.currentTimeMillis();
            System.out.println("\n Terminating Client "+ getClientOperation() + " thread - " + " Running time " + (receiveClientEndTime - receiveClientStartTime) + " milliseconds");
        }

    }
}
