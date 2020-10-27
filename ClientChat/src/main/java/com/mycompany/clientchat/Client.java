/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ClientChat;
import java.io.*;
import java.net.*;
/**
 *
 * @author loren
 */
public class Client {
    private final static int serverPort = 1234;
    private Socket s;
    private BufferedReader input;
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;
    
    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Client c = new Client();
        c.connect();
    }
    
    public void connect()
    {
        input = new BufferedReader(new InputStreamReader(System.in)); //Inizializzo input
        try{
            InetAddress ip = InetAddress.getByName("localhost"); //Recupero ip localhost
            s = new Socket(ip,serverPort); //Connetto al server
            inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outToServer = new DataOutputStream(s.getOutputStream());
            SendThread st = new SendThread();
            ReadThread rt = new ReadThread();
        }
        catch(Exception e)
        {
            e.toString();
        }
    }

     
    
    public class SendThread extends Thread{

        public SendThread() 
        {
            start();
        }
    

    
    @Override
    public void run()
    {
        try{
            for(;;)
            {
                String msg = input.readLine();
                if(msg.equals("STOP"))
                {
                    outToServer.writeBytes("Connessione chiusa!"+"/n");
                    inFromServer.close();
                    outToServer.close();
                    s.close();
                    System.exit(0);
                    break;
                }
                System.out.println("Io: "+msg);
                outToServer.writeBytes(msg+"/n");
            }
        }
            catch(Exception e)
           {
                e.toString();
                System.exit(1);
           }
      }
    }


    

public class ReadThread extends Thread
{
    public ReadThread()
    {
        start();
    }


@Override
public void run()
{
try
{
    for(;;)
    {
      String msg = inFromServer.readLine();
      System.out.println(msg);
    }
}
catch(Exception e)
{
    e.toString();
    System.exit(1);
}
}
}
}

