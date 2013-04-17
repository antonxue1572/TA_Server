import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    // Reference vars
    public static Server server = new Server();
    public static ServerGUI sgui = new ServerGUI(); 
    
    // Constants
    private final int PORT = 31415;
    private final int BACK_LOG = 100;
    private final String HOST_NAME = "localhost";
    
    // Server
    private ServerSocket serverSocket;
    
    // Constructor
    public Server() {
        
    }
    
    // Main method :)
    public static void main(String ... args) {
        // GUI
        new Thread(new Runnable() {
            public void run() {
                sgui.initGUI();
            }
        }).start();
        
        // Server
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.initServer();
            }
        }).start();
    }
    
    // Set up server
    public void initServer() {
        try {
            serverSocket = new ServerSocket(PORT, BACK_LOG, InetAddress.getByName("localhost"));
            System.out.println("ServerSocket established at " + HOST_NAME + ":" + PORT + " | Back log:" + BACK_LOG);
            
            while(true) {
                Socket client = serverSocket.accept();
                System.out.println("Accepted " + client.getPort());
                sgui.updateChat("- Client@" + client.getPort() + " joined -");
                new Thread(new ServerOutput(client)).start();
                new Thread(new ServerInput(client)).start();
            }
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private class ServerOutput implements Runnable {

        private Socket socket;
        
        public ServerOutput(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            // PrintWriter with auto-flush
            PrintWriter pw;
            try {
                pw = new PrintWriter(socket.getOutputStream(), true);
                while(true) {
                    /*
                     * It appears that something must be printed here, even a
                     * blank String for it to work properly. Most likely due to
                     * internal Java errors regarding stream processing.
                     */
                    System.out.print("");
                    if(sgui.getUpdateReady()) {
                        pw.println(sgui.getChatMessage());
                        //pw.println(sgui.getFullMessage());
                        sgui.setUpdateReady(false);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    private class ServerInput implements Runnable {

        private Socket socket;
        
        public ServerInput(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientText;
                while((clientText = br.readLine()) != null) {
                    System.out.println(clientText);
                    sgui.updateChat("[Client@" + socket.getPort() + "] " + clientText);
                }
            } catch (IOException ex) {
                sgui.updateChat("- Client@" + socket.getPort() + " disconnected -");
            }
        }
        
    }
    
}
