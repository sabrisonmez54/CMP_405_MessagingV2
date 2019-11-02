import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.Color;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainWindow implements ActionListener {
    public static String ipName;
    public static String portName;
    public static JButton sendMessage;
    public static JButton closeButton;
    public static JTextField messageBox;
    public static JTextArea chatBox;
    public static JTextField usernameChooser;

    public static Socket socket;
    public static InetAddress myAddress;
    public static InetAddress sendAddress;
    public static DatagramPacket packet;
    public static String message;
    private static Socket mySocket;

    public MainWindow(String ipText, String portText) {
        ipName = ipText;
        portName = portText;
    }

    public void display() {
        String appName = "IP: [ " + ipName + " ]  " + " Port: [ " + portName + " ] ";
        JFrame newFrame = new JFrame(appName);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.RED);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(this);

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);
        southPanel.add(closeButton, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(500, 300);
        newFrame.setVisible(true);
        newFrame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // mySocket = new Socket(Integer.parseInt(portName));

        // mySocket.send(messageBox.getText(),
        // mySocket.getMyAddress(),
        // mySocket.getMyPortNumber());

        // DatagramPacket inPacket = null;
        // do {
        // inPacket = mySocket.receive();
        // } while (inPacket == null);

        // byte[] inBuffer = inPacket.getData();
        // String inMessage = new String(inBuffer);
        // InetAddress senderAddress = inPacket.getAddress();
        // int senderPort = inPacket.getPort();
        // messageBox.setText("");

        // chatBox.append("\nReceived Message = " + inMessage);
        // chatBox.append("\nSender Address = " + senderAddress.getHostAddress());
        // chatBox.append("\nSender Port = " + senderPort);

        // mySocket.close();

        mySocket = new Socket(Integer.parseInt(portName));

        message = messageBox.getText();
        try {
            sendAddress = InetAddress.getByName(ipName);
        } catch (UnknownHostException e1) {
            
            e1.printStackTrace();
        }
			mySocket.send(message, sendAddress, 64000);
            chatBox.append("message: "+message+"\n sent to: "+sendAddress+" with port: "+portName+" ");
            messageBox.setText("");
            
        packet = mySocket.receive();
        
        if (packet != null) {
            byte[] inBuffer = packet.getData();
            message = new String(inBuffer);
            chatBox.append(packet.getAddress() + "received this message: " + message + "\n");
        }
    }
    //192.168.1.104
}