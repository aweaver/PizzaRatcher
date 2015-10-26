package com.mygdx.game;

//import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
//import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
//import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Aaron Weaver on 4/17/2015.
 */
public class DebugSender
{
    private int socketport=6007;// 8091 6007-dbgwindow
    private ClientThread sendThread;
    private static String destIP=null;//10.0.0.3
    private boolean bDebugMode=false;
    private int msgId;
    private SimpleDateFormat timeformat=null;
    private InetAddress serverAddr;
    private String dbgIP;
    private GetDebugIPThread getDebugIPTthread=null;
    private static boolean bStarted=false;


    public DebugSender()
    {
        timeformat = new SimpleDateFormat("hh:mm.ss.SSS a", Locale.US);

    }

    public void findDbgIP()
    {
        if(destIP==null)
        {
            getDebugIPTthread= new GetDebugIPThread();
            getDebugIPTthread.start();
        }

    }

    public void sendMessage(String sMsg)
    {
        String timeString;
        String sSendMsg=null;

        if(bDebugMode==false)//get out if not in debug mode
            return;

        if(destIP==null)
            return;

        if(sMsg==null)
        {
            sSendMsg="Null";
        }
        else
        {
            sSendMsg=sMsg;
        }


        Calendar cal = Calendar.getInstance();
        timeString=timeformat.format(cal.getTime());

        sendThread= new ClientThread(sSendMsg,timeString);
        sendThread.start();
        //parentBroadReceiver.peerResponse(jsonResponse);
    }

    public void setDebugMode(boolean bsrcDgbmode)
    {
        bDebugMode=bsrcDgbmode;
    }

    class ClientThread extends Thread implements Runnable
    {

        private String msg;
        private String timemsg;


        public ClientThread(String sMsg,String stimeMsg )
        {
            msg=sMsg;
            timemsg=stimeMsg;
        }

        @Override
        public void run()
        {
            String outMsg=null;
            Socket outsocket=null;
            DataOutputStream dataOutputStream = null;

            try
            {

                // InetAddress serverAddr = InetAddress.getByName(destIP);

                //original
                //outsocket = new Socket(serverAddr,socketport);


                serverAddr = InetAddress.getByName(destIP);
                outsocket = new Socket(serverAddr,socketport);
                dataOutputStream = new DataOutputStream(outsocket.getOutputStream());


                dataOutputStream.writeUTF(timemsg+"||"+msg+System.getProperty("line.separator")+"@");

                //System.getProperty("line.separator")//;in case \n does not work

                //Thread.sleep(500);

                // dataOutputStream.flush();


                dataOutputStream.close();
                dataOutputStream=null;
                outsocket.close();
                //Log.d("ClientThread/run", "message sent"+"\n");
            }
            catch (UnknownHostException e1)
            {
                e1.printStackTrace();

            } catch (IOException e1)
            {
                e1.printStackTrace();

            }

        }

    }

    class GetDebugIPThread extends Thread implements Runnable
    {
        private DatagramPacket packet;
        private MulticastSocket dgmSocket;
        private String INET_ADDR = "224.0.0.3";
        int server_port = 8888;//8888
        byte[] message;
        boolean bDone=false;
        boolean bError=false;
        InetAddress address;


        public GetDebugIPThread( )
        {
            message=new byte[9];

            try
            {

                dgmSocket = new MulticastSocket(server_port);//if this fails check that the app has permissions first
                packet = new DatagramPacket(message, message.length);

                address = InetAddress.getByName(INET_ADDR);
                dgmSocket.joinGroup(address);
            }
            catch(java.net.SocketException e)
            {
                e.printStackTrace();
                bError=true;
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
                bError=true;
            }
            catch (IOException e) {
                bError=true;
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String text;

            do
            {
                try
                {
                    dgmSocket.receive(packet);
                    destIP = new String(message, 0, packet.getLength());
                    bDone=true;
                    dgmSocket.leaveGroup(address);
                    dgmSocket.close();
                }
               catch(java.io.IOException e)
               {
                   bError=true;
                   e.printStackTrace();
               }
            }
            while(!bDone);
        }


    }
}
