import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client {
	private static String msgToServer = "";
	private static String msgToClient = "";
	private static DataInputStream inputStream = null;
	private static DataOutputStream outputStream = null;	
	private static JFrame frame;
	private static JLabel label = new JLabel();;
	private static JPanel panel;
	private static JTextField textField;
	private static JButton button = new JButton("Send");
	
	
	
	public static void main(String[] args){
		Socket socket = null;
		
		try{
			//***********************************************
			//GUI
			frame = new JFrame();
			textField = new JTextField(30);		
			
			panel = new JPanel();
			panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
			panel.setLayout(new GridLayout(0, 1));
			panel.add(label);
			panel.add(textField);
			panel.add(button);
			
			frame.add(panel, BorderLayout.CENTER);
			frame.setSize(400, 200);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Client");
			frame.pack();
			frame.setVisible(true);
			label.setText("Server is connected");
			Thread.sleep(1000);
			//***********************************************
			
			socket = new Socket("localhost", 9999);
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!textField.getText().isEmpty()) {
						msgToServer = textField.getText();
						try {
							if(outputStream != null) {
								outputStream.writeUTF(msgToServer);
								outputStream.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						textField.setText("");
					}	
				}
			});			
			
			while (!msgToClient.equals("end") && !msgToServer.equals("end")){
				msgToClient = inputStream.readUTF();
				label.setText("Server says: " + msgToClient);
			}
		}
		catch (Exception exe){
			exe.printStackTrace();
		}finally{
			try{				
				if (inputStream != null)
					inputStream.close();				

				if (outputStream != null)
					outputStream.close();
				
				if (socket != null)
					socket.close();				
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}