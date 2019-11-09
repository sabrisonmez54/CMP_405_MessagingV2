import java.awt.LayoutManager;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

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
    public static ArrayList<MainWindow> windowArray = new ArrayList<MainWindow>();
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

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println(ipTextField.getText());
				// System.out.println(portTextField.getText());

				MainWindow window = new MainWindow(ipTextField.getText(), portTextField.getText(), mySocket);
				windowArray.add(window);
				window.display();
	
		 }});
	}
	
	public static void main(String[] args) 
	{
		setWindow();
	
		while(true){
 		//open receive thread
        packet = mySocket.receive();
        
         // if packet is received get message and display it    
		 if (packet != null) 
		 {
            byte[] inBuffer = packet.getData();
            message = new String(inBuffer);
			
			ListIterator<MainWindow> iter 
			= windowArray.listIterator();
			
			if (iter.hasNext()){
				MainWindow window = iter.next();

				String packetAdress = packet.getAddress().toString().substring(1);
				String windowIp = window.ipName;
				String packetPort =Integer.toString(packet.getPort());
				String windowPort = window.portName;

				//System.out.println(packetAdress  + windowIp) ;
				
				if(packetAdress.equals(windowIp)){
					if(packetPort.equals(windowPort)){
						
						if(window.equals(iter))
						window.chatBox.append("Them: " + message.trim() + "\n");

					
					}
				}
				
				else
				{
					MainWindow newWindow = new MainWindow(packetAdress,packetPort, mySocket);
					//newWindow = iter.next();
					iter.add(newWindow);
					newWindow.display();
					newWindow.chatBox.append("Them: " + message.trim() + "\n");
					}  
				
			}
			else
				{
					String packetAdress = packet.getAddress().toString().substring(1);
					
					String portAddress = Integer.toString(packet.getPort());

					MainWindow newWindow = new MainWindow(packetAdress,portAddress, mySocket);
					//newWindow = iter.next();
					iter.add(newWindow);
					newWindow.display();
					newWindow.chatBox.append("Them: " + message.trim() + "\n");
					}
			}
		}
	}

	public boolean checkWindow(String packetAdress,String windowIp, String packetPort, String windowPort){
		if(packetAdress.equals(windowIp)){
			if(packetPort.equals(windowPort)){
			
				//window.chatBox.append("\nThem: " + message.trim() + "\n");

				return true;
	
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
