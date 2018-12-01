package whatsapp;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Client {
    public static void main(String[] args){
        System.out.println("In Connessione!");
        String ipServer = "192.168.1.3";
        int numeroPorta = 6666;
        Socket socket = null;
        try {
            socket = new Socket(ipServer, numeroPorta);
            System.out.println("Connesso! "+ socket);
        } catch (IOException ex) {
            System.out.println("Errore in Connessione!");
        }
        
        DataInputStream console = new DataInputStream(System.in);
        DataOutputStream streamOut = null;
        try {
            streamOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Errore in streamOut Client!");
        }
        
        String numeroTelefono="";
        
        try
        {
            System.out.print("Inserisci numero: ");
            numeroTelefono = console.readLine();
            streamOut.writeUTF(numeroTelefono);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            System.out.println("Errore in Invio: " + ioe.getMessage());
        }
        
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
        }
        File inFile = null;
        try {
            inFile = (File) oin.readObject();
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        if(inFile.getName().equals(".txt")==false)
        {
            BufferedReader reader = null;
            try
            {
                reader = new BufferedReader(new FileReader(inFile));
            } catch (FileNotFoundException ex) {
                System.out.println("File non trovato!");
            }
            String [] vett = new String [3];
            String line = "RND";
            String scrittore="";
            String lettore="";
            int num = 0;
            num = inFile.getName().indexOf(".");
            scrittore =inFile.getName().substring(0, num);
            System.out.println("Ciao "+ scrittore + ", di seguito le tue chat!");
            num=0;
            scrittore="";
            while(line != null)
            {
                try {
                    line = reader.readLine();
                } catch (IOException ex) {
                }
                if(line != null)
                {
                    vett = line.split("-");
                    num = vett[0].indexOf(" ");
                    scrittore = vett[0].substring(0, num);
                    lettore = vett[0].substring(num+1);
                    if(inFile.getName().equals(scrittore+".txt"))
                        System.out.println("Tu: "+vett[1]+ "         a "+lettore + " (" + vett[2] + ")");
                    else
                        System.out.println(scrittore+": "+vett[1]+ "         a Te (" + vett[2] + ")");
                }
            }

            try
            {
                console.close();
                streamOut.close();
                oin.close();
                socket.close();
                reader.close();
            }
            catch(IOException ioe)
            {
                System.out.println("Errore in Chiusura!");
            }
        }
        else
        {
            System.out.println("Non Registrato!");
        }
        try
        {
            console.close();
            streamOut.close();
            oin.close();
            socket.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Errore in Chiusura!");
        }
    } 
}