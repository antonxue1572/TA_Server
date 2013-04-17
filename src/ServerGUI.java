import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerGUI {
    
    // Important Constants
    private final int SERVER_GUI_WIDTH = 480;
    private final int SERVER_GUI_HEIGHT = 600;
    private final int WIDTH_ACCOM = 8;
    private final int PORT = 31415;
    
    // Framework
    private JFrame serverFrame;
    private Container serverPane;
    private Insets serverInsets;
    
    // Components
    private JLabel port;
    private JTextArea chatArea;
    private JScrollPane chatAreaWrapper;
    private JTextArea chatSubmit;
    private JScrollPane chatSubmitWrapper;
    private JButton submitButton;
    
    // Component constants
    private final int PORT_X = 10;
    private final int PORT_Y = 10;
    private final int CHAT_AREA_WIDTH = 400;
    private final int CHAT_AREA_HEIGHT = 400;
    private final int CHAT_SUBMIT_WIDTH = 400;
    private final int CHAT_SUBMIT_HEIGHT = 50;
    
    // Misc
    private String chatMessage;
    private boolean updateReady = false;
    
    // Constructor
    public ServerGUI() {
        
    }
    
    // Init GUI framework
    public void initGUI() {
        // Fancy
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        // Init frame
        serverFrame = new JFrame("Server");
        serverFrame.setSize(SERVER_GUI_WIDTH, SERVER_GUI_HEIGHT);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Containers
        serverPane = serverFrame.getContentPane();
        serverPane.setLayout(null);
        
        // Insets
        serverInsets = serverFrame.getInsets();
        
        // Other components
        initComponents();
        
        // Finalize
        serverFrame.setVisible(true);
    }
    
    // Initialize the individual components
    private void initComponents() {
        // Port label
        port = new JLabel("Port: " + Integer.toString(PORT));
        port.setBounds((serverInsets.left + SERVER_GUI_WIDTH) / 2 - port.getPreferredSize().width / 2 - WIDTH_ACCOM, serverInsets.top + 10, port.getPreferredSize().width, port.getPreferredSize().height);
        serverPane.add(port);
        
        // Message chat area
        chatArea = new JTextArea();
        chatAreaWrapper = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatAreaWrapper.setWheelScrollingEnabled(true);
        chatAreaWrapper.setBounds((serverInsets.left + SERVER_GUI_WIDTH) / 2 - CHAT_AREA_WIDTH / 2 - WIDTH_ACCOM, serverInsets.top + 50, CHAT_AREA_WIDTH, CHAT_AREA_HEIGHT);
        serverPane.add(chatAreaWrapper);
        
        // Text Submit Area
        chatSubmit = new JTextArea();
        chatSubmitWrapper = new JScrollPane(chatSubmit);
        chatSubmit.setEditable(true);
        chatSubmit.setLineWrap(true);
        chatSubmitWrapper.setWheelScrollingEnabled(true);
        chatSubmitWrapper.setBounds((serverInsets.left + SERVER_GUI_WIDTH) / 2 - CHAT_SUBMIT_WIDTH / 2 - WIDTH_ACCOM, serverInsets.top + 460, CHAT_SUBMIT_WIDTH, CHAT_SUBMIT_HEIGHT);
        //serverPane.add(chatSubmit);
        serverPane.add(chatSubmitWrapper);
        
        // Buttons
        submitButton = new JButton("Send");
        submitButton.setBounds(serverInsets.left + 375, serverInsets.top + 520, submitButton.getPreferredSize().width, submitButton.getPreferredSize().height);
        submitButton.addActionListener(new SubmitListener());
        serverPane.add(submitButton);
    }
    
    private class SubmitListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            chatMessage = "[Server] " + chatSubmit.getText().trim();
            System.out.println(chatMessage);
            updateChat(chatMessage);
            chatSubmit.setText("");
            updateReady = true;
        }
    }
    
    public void updateChat(String newText) {
        chatArea.append(newText + "\n");
    }
    
    public boolean getUpdateReady() {
        return updateReady;
    }
    
    public void setUpdateReady(boolean state) {
        updateReady = state;
    }
    
    public String getChatMessage() {
        return chatMessage;
    }
    
    public String getFullMessage() {
        return chatArea.getText();
    }
    
}
