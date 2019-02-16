/*
 * A UDP Client
 * Implementing the Stop-n-Wait-ARQ algorithm
 * Uses: 
 * 		class Packet  payload, sequence numbers, etc
 * 		class NoisyDatagramSocket - extends DatagramSocket - add drops & delays
 * 
 * Last modified:  Malcolm 20171005
 *
 */

import java.io.*;
import java.net.*;
import java.nio.*;

class StopAndWaitClient{
    private static final int BUFFER_SIZE = 1024;
	private static final int size = 4;
    private static final int PORT = 6789;
    private static final int SEQUENCE_NUMBER = 0;
    private static final int TimeOutValue = 3000;
    private static final String SERVER = "localhost";
    private static int counter=-1;
	private static int N = 10;                        // number of times to loop 
	public static boolean timedOut;
	public static DatagramPacket sDatagram;
	public static DatagramPacket sDatagram1;
	public static DatagramPacket sDatagram2;
	public static DatagramPacket sDatagram3;
	public static byte[] rData;
	public static InetAddress serverIP;
	public static Packet sPacket;
	public static int counter2 =0;
	
	
    public static void main(String args[]) throws Exception{
		
		// Create a socket  //DatagramSocket socket = new DatagramSocket();
		NoisyDatagramSocket socket = new NoisyDatagramSocket();
		socket.setSoTimeout( TimeOutValue );
		

		// The message we're going to send converted to bytes
		Integer sequenceNumber = SEQUENCE_NUMBER;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        //String sentence = " 0123456789";      
		String sentence;
		
		while( (sentence = inFromUser.readLine()) != null) {
		
		//  N = sentence.length();//System.out.println("FROM USER:"+sentence);
	
		  for (int i=0; i<sentence.length(); i+=size) {			// here we keep creating new sentences
			  
			
			 for(int c=1;c<=4;c++) { 	// makes sure we are sending 4 packets instead of one
			  
			  String sData[] = null;
			
			  sData[c] = sentence.substring(i, i+size);
			
		
			boolean timedOut = true;
            
			// Create a byte array for sending and receiving data
		byte[] rData = new byte[ BUFFER_SIZE ];

Packet sPacket = new Packet(sequenceNumber, sData[c]); // payload: data & i (sequence number)

			
				// serialize the payload
				ByteArrayOutputStream baos = new ByteArrayOutputStream();   // setup stream - will be bytes
				ObjectOutputStream oos = new ObjectOutputStream(baos);      // setup to serialize Packet
				oos.writeObject(sPacket);                                 // write Packet to Object stream
				
												
				byte[] sBuf = baos.toByteArray();      // put packet (stream) into byte buffer
										
				
			// Get the IP address of the server
			InetAddress serverIP = InetAddress.getByName( SERVER );

	System.out.println( "Sending Packet (seq_n: " + sequenceNumber + ") Payload: '" + sData + "'"); 
	
	setCounter();// making sure the count is at 1
			if(getCounter()==1) {DatagramPacket sDatagram = new DatagramPacket(sBuf, sBuf.length, serverIP, PORT);}
			if(getCounter()==2) {DatagramPacket sDatagram1 = new DatagramPacket(sBuf, sBuf.length, serverIP, PORT);}
			if(getCounter()==3) {DatagramPacket sDatagram2 = new DatagramPacket(sBuf, sBuf.length, serverIP, PORT);}
			if(getCounter()==4) {DatagramPacket sDatagram3 = new DatagramPacket(sBuf, sBuf.length, serverIP, PORT);}
			
							// making my window size 4
			
											}  // end of if statement
			 }				//closing loop
	

while( timedOut ){					//I'm going to change were the while look is at
		try{ // Send the UDP Packet to the server
			socket.send( sDatagram );
			socket.send( sDatagram1 );
			socket.send( sDatagram2 );
			socket.send( sDatagram3 );

				// Receive the server's packet
				for(int k=0; k < rData.length; k++) rData[k]=0;           //fill with zeros
				DatagramPacket rDatagram = new DatagramPacket(rData, rData.length, serverIP, PORT);
				socket.receive( rDatagram );
				byte[] rPayload = rDatagram.getData();                     // fill buffer for payload

				// serialize the payload
				ByteArrayInputStream bais = new ByteArrayInputStream(rPayload);// stream will be array of bytes
				ObjectInputStream ois = new ObjectInputStream(bais);        // setup stream 
				setCounter();
				Packet rPacket = new Packet(0, new String(new byte[BUFFER_SIZE]));
						try {
							rPacket = (Packet) ois.readObject();                   // read and cast to Packet	
						} 
						
						catch (ClassNotFoundException e) {e.printStackTrace(); }  // if not packet (recover?)
						
						String rBuf = new String(rPacket.getPayload());         // pull out data
						int seq_no = rPacket.getSeq();                             // pull out sequence number
						System.out.println(" Received Packet (seq_n: "  + seq_no + ") Payload: '" + rBuf + "'");
						
						setCounter();		// counter that keeps track of the packets that were sent
						
					if(getCounter()<4) {break;}		// makes sure all packets were sent across
						// If we receive an ack, stop the while loop	
				
						sequenceNumber = (sequenceNumber==1)? 0 : 1;  // if seq==0 set to 1 else set to 0
						timedOut = false;
					
					
		} 
		catch( SocketTimeoutException exception ){
			// If we don't get an ack, prepare to resend sequence number
			sPacket.incRetransmits();
			System.out.println( "Timeout (Sequence Number " + sequenceNumber + ")" );
		}
			}	//end of  timeout while loop
		  }
	    }	
	//socket.close();
   					
    public static void setCounter() {
    	counter++;
    }
    public static int getCounter() {
    	return counter;
    }
    public static void setCounter2() {
    	counter2++;
    }
    public static int getCounter2() {
    	return counter2;
    }
}
