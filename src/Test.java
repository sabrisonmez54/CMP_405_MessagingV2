import java.awt.LayoutManager;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test implements ActionListener 
{
	private static JLabel 		ipLabel;
	private static JTextField 	ipTextField;
	private static JLabel 		portLabel;
	private static JTextField 	portTextField;
	private static JButton 		startButton;

	public static InetAddress myAddress;
    public static InetAddress sendAddress;
    public static DatagramPacket packet;
    public static String message;
	private static Map<InetAddress, MainWindow> windowMap = new HashMap<InetAddress, MainWindow>();
	public static Socket mySocket = new Socket(64000);

	public static void setWindow() 
	{
		ipLabel = new JLabel("IP address: ");
		ipTextField = new JTextField(50);
		portLabel = new JLabel("Port: ");
		portTextField = new JTextField(50);
		startButton = new JButton("Start");

		JPanel startPanel = new JPanel();

		startPanel.add(ipLabel);
		startPanel.add(ipTextField);
		startPanel.add(portLabel);
		startPanel.add(portTextField);
		startPanel.add(startButton);

		JFrame startFrame = new JFrame("Start Frame");
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
	
	public static void main(String[] args) 
	{
		setWindow();
	
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
			String packetPort =Integer.toString(packet.getPort());
			
			//check if a window with same ip already exists
			if(windowMap.containsKey(packet.getAddress()))
				{ 
				//find the chat window that exists and append the received message
					MainWindow currentChat = windowMap.get(packet.getAddress());
					currentChat.getChatArea().append("Them: " +message.trim() + "\n");
				}
			
			else
				{//if not create a new window and add to hashmap
					MainWindow newWindow = new MainWindow(packetAdress,packetPort, mySocket);
					windowMap.put(packet.getAddress(), newWindow);
					newWindow.display();
					newWindow.chatBox.append("Them: " + message.trim() + "\n");
				}
			
			}
		
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
