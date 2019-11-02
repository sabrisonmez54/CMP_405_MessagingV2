import java.awt.LayoutManager;
import java.net.DatagramPacket;
import java.net.InetAddress;

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

		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//System.out.println(ipTextField.getText());
				//System.out.println(portTextField.getText());
 
 
				MainWindow window = new MainWindow(ipTextField.getText(), portTextField.getText());
				
				window.display();
				startFrame.setVisible(false);
		 }});
	}
	
	public static void main(String[] args) 
	{
		setWindow();

		// mySocket = new Socket(64000);
		
		// mySocket.send("Hello Communication World!!!", 
		// 			  mySocket.getMyAddress(), 
		// 			  mySocket.getMyPortNumber());

		// DatagramPacket inPacket = null;
		// do {
		// 	inPacket = mySocket.receive();
		// } while (inPacket == null);
		
		// byte[] inBuffer = inPacket.getData();
		// String inMessage = new String(inBuffer);
		// InetAddress senderAddress = inPacket.getAddress();
		// int senderPort = inPacket.getPort();
		
		// System.out.println();
		// System.out.println("Received Message = " + inMessage);
		// System.out.println("Sender Address   = " + senderAddress.getHostAddress());
		// System.out.println("Sender Port      = " + senderPort);
		
		// mySocket.close();	
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
