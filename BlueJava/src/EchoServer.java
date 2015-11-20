import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/*
 * Ubuntu
 * sudo apt-get install libbluetooth-dev
 */


public class EchoServer {
	/*
	public final UUID uuid = new UUID(                              //the uid of the service, it has to be unique,
			"27012f0c68af4fbf8dbe6bbaf7aa432a", false); //it can be generated randomly
			*/
	
	public final UUID uuid = new UUID(                              //the uid of the service, it has to be unique,
			"0000110100001000800000805F9B34FB", false); //it can be generated randomly

    public final String name = "Echo Server";                       //the name of the service
    public final String url  =  "btspp://localhost:" + uuid         //the service url
                                + ";name=" + name 
                                + ";authenticate=false;encrypt=false;";
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;

    public EchoServer() {
        try {
            System.out.println("Setting device to be discoverable...");
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            System.out.println("Start advertising service...");
            server = (StreamConnectionNotifier)Connector.open(url);
            System.out.println("Waiting for incoming connection...");
            conn = server.acceptAndOpen();
            RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
            System.out.println("Client Connected..."+rd.getBluetoothAddress()+" "+rd.getFriendlyName(true));
            //DataInputStream din   = new DataInputStream(conn.openInputStream());
            InputStream din = conn.openInputStream();
            //String responseLine;
            OutputStream os = conn.openOutputStream();
            //DataOutputStream dos= new DataOutputStream(conn.openOutputStream());

            BufferedReader lines = new BufferedReader(new InputStreamReader(din, "UTF-8"));
            while (true) {
              String line = lines.readLine();

              PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
              pw.write("Respuesta del servidor!\n");
              pw.flush();
              
              //dos.writeUTF("respuesta serv");
              //dos.flush();
/*
              if (line == null)
                break;
  */            
              if(line.equals("Ok")){
            	  din.close();
            	 // dos.close();
            	  os.close();
            	  conn.close();
            	 break;
              }
              System.out.println("Recibido " + line);
            }
            /*
            while ((responseLine = din.readLine()) != null) {
                System.out.println("Server: " + responseLine);
                if (responseLine.indexOf("Ok") != -1) {
                  break;
                }
            }*/
            
            //din.close();
           /*
            while(true){
            	
            	//BufferedReader bReader=new BufferedReader(new InputStreamReader(is));
                //String lineRead=bReader.readLine();
                //System.out.println(lineRead);
                
            	
                String cmd = "";
                char c;
                while (((c = din.readChar()) > 0) && (c!='\n') ){
                    cmd = cmd + c;
                }
                System.out.println("Received " + cmd);
                
            }
*/
            
        } catch (Exception  e) {System.out.println("Exception Occured: " + e.toString());}
    }
    
    public static void main (String args[]){
        EchoServer echoserver = new EchoServer(); 
    }
}
