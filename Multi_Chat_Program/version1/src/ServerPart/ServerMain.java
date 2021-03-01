package ServerPart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ServerMain {

	private ServerSocket serverSocket;
    private Socket socket;
    private ServerGUI gui;
    private String msg;
 
    private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();
 
    public final void setGui(ServerGUI gui) {
        this.gui = gui;
    }
 
    public void setting() throws IOException {
        Collections.synchronizedMap(clientsMap); 
        serverSocket = new ServerSocket(1234);
        while (true) {
            
            System.out.println("서버 대기중...\n");
            socket = serverSocket.accept(); 
            System.out.println(socket.getInetAddress() + "에서 접속했습니다.\n");
           
            Receiver receiver = new Receiver(socket);
            receiver.start();
        }
    }
 
    public static void main(String[] args) throws IOException {
        ServerMain serverBackground = new ServerMain();
        serverBackground.setting();
    }
 
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "님이 접속하셨습니다.\n");
        clientsMap.put(nick, out);
    }
 
    public void removeClient(String nick) {
        sendMessage(nick + "님이 나가셨습니다.\n");
        clientsMap.remove(nick);
    }
 
    public void sendMessage(String msg) {
        Iterator<String> it = clientsMap.keySet().iterator();
        String key = "";
        while (it.hasNext()) {
            key = it.next();
            try {
                clientsMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    class Receiver extends Thread {
        private DataInputStream in;
        private DataOutputStream out;
        private String nick;
 
        public Receiver(Socket socket) throws IOException {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            nick = in.readUTF();
            addClient(nick, out);
        }
 
        public void run() {
            try {
                while (in != null) {
                    msg = in.readUTF();
                    sendMessage(msg);
                    gui.appendMsg(msg);
                }
            } catch (IOException e) {
                removeClient(nick);
            }
        }
    }
	
	
}
