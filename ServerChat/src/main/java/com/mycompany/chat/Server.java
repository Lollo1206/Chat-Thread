/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chat;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author loren
 */
public class Server {
    private ArrayList<ServerThread> sockets = new ArrayList<>();
    private ServerSocket s;
    private Socket socket = new Socket();
    
    public void start()
    {
        try
        {
          System.out.println("In attesa...");
          s = new ServerSocket(1234);
          for(int i=0; i<2; i++)
          {
              sockets.add(new ServerThread(s.accept()));
              sockets.get(i).start();
          }
          s.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore!");
            System.exit(1);
        }
    }
    
    public static void main(String[] args)
    {
        Server s1 = new Server();
        s1.start();
    }
    
    public class ServerThread extends Thread
    {
        private Socket clients = null;
        private BufferedReader inDalClient;
        private DataOutputStream outVersoClient;
        private String usernameClient;    
        
        public ServerThread(Socket socket)
        {
            this.clients = socket;
        }
        
        @Override
        public void run()
        {
            try
            {
                comunica();
            }
            catch(Exception e)
            {
               e.toString();         
            }
        }
        
        public void comunica() throws Exception
        {
            inDalClient = new BufferedReader(new InputStreamReader(clients.getInputStream()));
            outVersoClient = new DataOutputStream(clients.getOutputStream());
            outVersoClient.writeBytes("Inserisci nome utente"+"/n");
            usernameClient = inDalClient.readLine();
            outVersoClient.writeBytes(usernameClient + " connesso!"+"/n");
            
            for(;;)
            {
                String msg = inDalClient.readLine();
                if(msg.equals("STOP"))
                {
                    System.out.println(usernameClient + " disconnesso!" +"/n");
                    sockets.remove(this);
                    break;
                }
                else if(sockets.size()>1)
                {
                    for(ServerThread s : sockets)
                    {
                        if(s != this)
                        {
                            s.outVersoClient.writeBytes(usernameClient + ": "+msg+"/n");
                        }
                    }
                }
                else
                {
                    outVersoClient.writeBytes("Utenti connessi: 0"+"/n");
                }
                outVersoClient.close();
                inDalClient.close();
                clients.close();
            }
        }
        
    }
    
}
