import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class Socket {
	
	private int myPortNumber;
	private InetAddress myAddress;
	private DatagramSocket mySocket;
	
	private Thread receiveThread;
	private boolean receiveThreadShouldKeepRunning = true;
	
	private ConcurrentLinkedQueue<DatagramPacket> messageQueue = 
			new ConcurrentLinkedQueue<DatagramPacket>();
	
	public Socket(int myPortNumber) {
		this.myPortNumber = myPortNumber;		
		try {
			this.myAddress = InetAddress.getLocalHost();

			System.out.println(myAddress.getHostAddress());
			this.mySocket = new DatagramSocket(myPortNumber);
		} catch (Exception uhe) {
			uhe.printStackTrace();
			System.exit(-1);
		}
		
		this.receiveThread = new Thread(
				new Runnable() {
					public void run() {
						receiveThreadMethod();
					}
				});
		this.receiveThread.setName("Receive Thread for Port Number - " + this.myPortNumber);
		this.receiveThread.start();
	}
	
	public void send(String message, 
					 InetAddress destinationAddress,
					 int destinationPort) {
		
		byte[] outBuffer;
		outBuffer = message.getBytes();
		
		DatagramPacket outPacket = new DatagramPacket(outBuffer, 
													  outBuffer.length,
													  destinationAddress,
													  destinationPort);
		
		try {
			this.mySocket.send(outPacket);
			System.out.println("Message sent");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void receiveThreadMethod() {

		System.out.println("\nReceive Thread is Starting!!!! \n" );
		
		try {
			this.mySocket.setSoTimeout(50);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		do {
			byte[] inBuffer = new byte[1024];

			for ( int i = 0 ; i < inBuffer.length ; i++ ) {
				inBuffer[i] = ' ';
			}

			DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			
			try 
			{
				mySocket.receive(inPacket);
				System.out.println("I received something");
				messageQueue.add(inPacket);

			} 
			catch (SocketTimeoutException ste) 
			{
				// nothing to do
			} 
			catch (IOException ioe) 
			{
				ioe.printStackTrace();
				System.exit(-1);
			}
			
		} 
		while (receiveThreadShouldKeepRunning);
		System.out.println("Receive Thread is Exiting!!!!");
	}

	public DatagramPacket receive() 
	{
		return this.messageQueue.poll();
	}
	
	public void close() 
	{
		System.out.println("\nClosing Socket and Stopping Receive Thread");
		this.receiveThreadShouldKeepRunning = false;
	
		try 
		{
			TimeUnit.MILLISECONDS.sleep(100);
		} 
		catch (InterruptedException ie) 
		{
			ie.printStackTrace();
			System.exit(-1);
		}
		mySocket.close();
		System.out.println("Socket Closed");
	}

	public int getMyPortNumber() 
	{
		return this.myPortNumber;
	}

	public InetAddress getMyAddress() 
	{
		return this.myAddress;
	}
}
