package server;

import oop.server.ServerLogoutAction;
import oop.server.ServerActionManager;
import oop.server.ServerLoginAction;
import org.json.simple.JSONObject;
import util.Actions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain{

    public static final int PORT = 8888;
    public ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private  ServerActionManager serverActionManager = new ServerActionManager();
    
    
    public ServerMain() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is up");
        while(true){
            Socket request = serverSocket.accept();
            handleRequestAndBuildThreads(request);
        }
    }


    public void handleRequestAndBuildThreads(Socket request){
        new Thread(()->{
            try {
                ObjectOutputStream out = new ObjectOutputStream(request.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(request.getInputStream());
                while(true){
                    Object incomingRequest = in.readObject();
                    resolveRequest(incomingRequest,out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public synchronized void resolveRequest(Object o, ObjectOutputStream out){
        JSONObject json = (JSONObject)o;
        String action = (String)json.get("action");
        switch (action){
            case (Actions.ACTION_LOGIN_REQUEST):
                serverActionManager.setServerActionResolver(new ServerLoginAction(out));break;
            case (Actions.ACTION_CLOSE_APPLICATION):
                serverActionManager.setServerActionResolver(new ServerLogoutAction());break;

        }
        serverActionManager.resolve(json,this);
    }

    public synchronized void sendToClients(JSONObject jsonObject,ArrayList<ConnectedClient> receivers){
        receivers.forEach(e->{
            try {
                e.getObjectOutputStream().writeObject(jsonObject);
                e.getObjectOutputStream().flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public synchronized void sendToClients(JSONObject jsonObject, ObjectOutputStream out){
        try {
            out.writeObject(jsonObject);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<ConnectedClient> getConnectedClients() {
        return connectedClients;
    }

    public synchronized void addClient(ConnectedClient c)
    {
        connectedClients.add(c);
    }

    public synchronized void deleteClient(ConnectedClient c){
        try {
            c.getObjectOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectedClients.remove(c);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ServerMain();
    }
}
