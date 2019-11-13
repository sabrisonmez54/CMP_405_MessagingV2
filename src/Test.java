import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test implements ActionListener 
{
	private static JLabel 			ipLabel;
	private static JTextField 		ipTextField;
	private static JLabel 			portLabel;
	private static JTextField 		portTextField;
	private static JButton 			startButton;

	public static InetAddress 		myAddress;
    public static InetAddress 		sendAddress;
	public static DatagramPacket 	packet;
	
	public static String 			message;
	public static String 			senderName;
	public static String 			senderIP;
	public static String 			receiverName;
	
	public static Map<InetAddress, MainWindow> windowMap = new HashMap<InetAddress, MainWindow>();
	public static Socket mySocket = new Socket(64000);

	public static void setWindow() 
	{
		ipLabel = new JLabel("IP address you want to communicate with: ");
		ipTextField = new JTextField(50);
		portLabel = new JLabel("Port Number: ");
		portTextField = new JTextField(50);
		portTextField.setText("64000");
		startButton = new JButton("Start Chatting");

		JPanel startPanel = new JPanel();

		startPanel.add(ipLabel);
		startPanel.add(ipTextField);
		startPanel.add(portLabel);
		startPanel.add(portTextField);
		startPanel.add(startButton);

		JFrame startFrame = new JFrame("My IP Address: " + mySocket.getMyAddress());
		startFrame.setContentPane(startPanel);
		startFrame.setSize(650, 200);
		startFrame.setLocationRelativeTo(null);
		startFrame.setVisible(true);
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startButton.addActionListener(new ActionListener() 
		{ //start button pressed
			public void actionPerformed(ActionEvent e) 
			{ //create new window with the details provided by user and then add to hashmap
				MainWindow window = new MainWindow(ipTextField.getText(), portTextField.getText(), mySocket);
				try 
				{ // get address to send to
					sendAddress  = InetAddress.getByName(ipTextField.getText());
				}
				catch (UnknownHostException e1) 
				{
					e1.printStackTrace();
				}
				windowMap.put(sendAddress, window);
				window.display();
		 	}
		});
	}
	public static void setWindow1() 
	{
		JPanel startChatPanel = new JPanel();
		JLabel nameLabel = new JLabel("Name of person you want to communicate with: ");
		JTextField nameTextField = new JTextField(50);
		JButton startButton = new JButton("Start Chatting");

		startChatPanel.add(nameLabel);
		startChatPanel.add(nameTextField);
		startChatPanel.add(startButton);
		
		JFrame startChatFrame = new JFrame("My IP Address: " + mySocket.getMyAddress());
		startChatFrame.setContentPane(startChatPanel);
		startChatFrame.setSize(650, 200);
		startChatFrame.setLocationRelativeTo(null);
		startChatFrame.setVisible(true);
		startChatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startButton.addActionListener(new ActionListener() 
		{ 
			InetAddress broadcastAdress = null;
			public void actionPerformed(ActionEvent e) 
			{ 
				InetAddress broadcastAdress = null;
			try 
			{
				broadcastAdress = InetAddress.getByName("255.255.255.255");
			} 
			catch (UnknownHostException e1) 
			{
				e1.printStackTrace();
			}
			String sendName = nameTextField.getText();
			String myName = "Sabri";
			String message = "????? " + sendName + " ##### " + myName;
			System.out.println(message);
			mySocket.send(message, broadcastAdress, 64000);
		 	}
		});
	}
	
	public static void main(String[] args) 
	{
		
		setWindow1();
		//setWindow();
		while(true)
		{
 			//open receive thread
			packet = mySocket.receive();
        
         	// if packet is received get message and display it    
			if (packet != null) 
			{
				//get packet details
            	byte[] inBuffer = packet.getData();
            	message = new String(inBuffer);
				String packetAdress = packet.getAddress().toString().substring(1);
				//String packetPort = Integer.toString(packet.getPort());
			
				System.out.println("Someone BroadCasted:  " + message.trim());
				
				if(message.startsWith("?????")) 
				{
					if(CheckBroadcast(message)) 
					{
						System.out.println("I received \n" + message.trim());
						System.out.println("sender name" + senderName);
						System.out.println("sender IP" + packetAdress);
	
						InetAddress broadcastAdress = null;
						try 
						{
							broadcastAdress = InetAddress.getByName(packetAdress);
							myAddress = InetAddress.getLocalHost();
						} 
						catch (UnknownHostException e1) 
						{
							e1.printStackTrace();
						}

						mySocket.send("##### " +senderName+ " ##### " + myAddress.getHostAddress(), broadcastAdress , 64000);
	
						checkPacket(packet);
					}
				}
				
				if(message.startsWith("#####")) 
				{
					String[] splittedMessage = message.split(" ");
			
			  		if(splittedMessage[2].equalsIgnoreCase("#####"))
			  		{
			       		senderName = splittedMessage[1];
					}
					
					checkPacket(packet);
				}
				
				if(!message.startsWith("#####") && !message.startsWith("?????"))
				{
					checkPacket(packet);
				}
			}
		}
	}

	private static boolean CheckBroadcast(String message) {
		
		if(message.startsWith("?????")) 
		{
			String[] splittedMessage = message.split(" ");
			
			  if(splittedMessage[2].equalsIgnoreCase("#####"))
			  {
			       receiverName = splittedMessage[1];
				   senderName = splittedMessage[3];
				   	if(receiverName.equalsIgnoreCase("Sabri")) 
				   	{
						return true; 
					}
				}
		}
		return false;
	}


	private static void checkPacket(DatagramPacket packet) 
	{
		//get packet details
        byte[] inBuffer = packet.getData();
        message = new String(inBuffer);
		String packetAdress = packet.getAddress().toString().substring(1);
		String packetPort = Integer.toString(packet.getPort());
			
		//check if a window with same ip already exists
		if(windowMap.containsKey(packet.getAddress()))
		{ 
			//find the chat window that exists and append the received message
			MainWindow currentChat = windowMap.get(packet.getAddress());
			currentChat.getChatArea().append(senderName + ": " +message.trim() + "\n");
		}

		else
		{
			//if not create a new window and add to hashmap
			MainWindow newWindow = new MainWindow(packetAdress,packetPort, mySocket);
			windowMap.put(packet.getAddress(), newWindow);
			newWindow.display();
			newWindow.chatBox.append(senderName + ": " + message.trim() + "\n");
		}
	}

	public static Map<InetAddress, MainWindow> getMap()
	{
		return windowMap;
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
