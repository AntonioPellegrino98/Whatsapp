package whatsapp;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {
    public static void main(String[] args) throws IOException{
        System.out.println("In Connessione!");
        ServerSocket server = null;
        int numeroPorta = 6666;
        try {
            server = new ServerSocket(numeroPorta);
            System.out.println("Server Avviato!");
        } catch (IOException ex) {
            System.out.println("Errore nella creazione della Socket!");
        }
        Socket socket = null;
        try {
            socket = server.accept();
            System.out.println("Client Accettato! " + socket);
        } catch (IOException ex) {
            System.out.println("Errore nell'accettazione della socket!");
        }
        DataInputStream streamIn = null;
        try {
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Errore nella streamIn!");
        }
        String numeroTelefono = "";
        try
        {
            numeroTelefono = streamIn.readUTF();
            System.out.println("Numero ricevuto: " + numeroTelefono);
        }
        catch(IOException ioe)
        {
            System.out.println("Errore in ricezione del messaggio!");
        }
        
        //lettura file di testo in base al numero
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader("Nomi.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("File non trovato!");
        }
        boolean trovato = false;
        String [] vett = new String[2];
        String line = "RND";
        String nome = "";
        while(line != null)
        {
            line = reader.readLine();
             
            if(line != null)
            {
               vett = line.split("-");
               if(vett[0].equals(numeroTelefono))
               {
                   //ho trovato il numero
                   trovato = true;
                   nome = vett[1];
               }
            }
        }
        File f = new File(nome+".txt");
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        if(!trovato)
        {        
            try 
            {
                System.out.println("Non Registrato!");
                oos.writeObject(f);
                System.out.println("File "+ nome+".txt Inviato");
                oos.flush();
                oos.close();
                socket.close();
                streamIn.close();
                reader.close();
            } catch (IOException ex) 
            {
                System.out.println("Errore in Chiusura!");
            }
        }
        else
        {
            try
            {
                oos.writeObject(f);
                System.out.println("File "+ nome+".txt Inviato");
                oos.flush();
            }
            catch(IOException ioe)
            {
                System.out.println("Errore in Invio: " + ioe.getMessage());
            }  
            try {
                socket.close();
                streamIn.close();
                reader.close();
                oos.close();
            } catch (IOException ex) {
                System.out.println("Errore in Chiusura!");
            }
        }
    } 
}