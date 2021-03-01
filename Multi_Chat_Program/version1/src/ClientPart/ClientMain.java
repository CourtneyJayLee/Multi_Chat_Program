package ClientPart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientMain {

	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientGUI gui;
    private String msg;
    private String nickName;
 
    public final void setGui(ClientGUI gui) {
        this.gui = gui;
    }
 
    public void connet() {
        try {
            socket = new Socket("192.168.219.106", 1234);
            System.out.println("서버 연결됨.\n");
 
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
 
            out.writeUTF(nickName);
            System.out.println("클라이언트 : 메시지 전송완료\n");
            while (in != null) {
                msg = in.readUTF();
                gui.appendMsg(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        ClientMain clientBackground = new ClientMain();
        clientBackground.connet();
    }
 
    public void sendMessage(String msg2) {
        try {
            out.writeUTF(msg2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void setNickname(String nickName) {
        this.nickName = nickName;
    }
	
}
