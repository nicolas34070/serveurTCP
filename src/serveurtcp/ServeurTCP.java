package serveurtcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {

    public static void main(String[] args) throws Exception {
        ServerSocket m_ServerSocket = new ServerSocket(1234);
        int id = 0;
        System.out.println("Lancement du serveur...");
        while (true) {
            Socket clientSocket = m_ServerSocket.accept();
            ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
            cliThread.start();
        }
    }
}

class ClientServiceThread extends Thread {

    Socket clientSocket;
    int clientID = -1;
    boolean running = true;
    String demande = "demandeCoordonnees";
    String msgEnvoi;
    String coordonnees = "4321.3226,N,00520.31884,E";

    ClientServiceThread(Socket s, int i) {
        clientSocket = s;
        clientID = i;
    }

    @Override
    public void run() {
        System.out.println("Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String clientCommand = in.readLine();

            while (clientCommand != null) {

                if (clientCommand.equals(demande)) {

                    out.println(coordonnees);
                    out.flush();
                }

                System.out.println("Client Says :" + clientCommand);
                clientCommand = in.readLine();

            }

            System.out.print("Stopping client thread for client : " + clientID + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}