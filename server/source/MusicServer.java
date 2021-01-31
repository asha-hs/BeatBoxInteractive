import java.net.*;
import java.util.*;
import java.io.*;

public class MusicServer {
  ArrayList<ObjectOutputStream> clientOutputStreams;
  public static void main(String[] args) {
    new MusicServer().go();
  }

  public void go() {
    clientOutputStreams = new ArrayList<ObjectOutputStream>();
    try {
      ServerSocket serverSock = new ServerSocket(4242);

      while(true) {
        Socket clientSocket = serverSock.accept();
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        clientOutputStreams.add(out);
        Thread t = new Thread(new ClientHandler(clientSocket));
        t.start();

        System.out.println(" got a connection");
      }
    }catch(Exception ex) { ex.printStackTrace();}
  } //close go

  public class ClientHandler implements Runnable {
    ObjectInputStream in;
    Socket clientSocket;

    public ClientHandler(Socket socket) {
      try {
        clientSocket = socket;
        in = new ObjectInputStream(clientSocket.getInputStream());
      } catch(Exception ex) { ex.printStackTrace();}

    } // close constructor

    public void run() {
      Object o1 = null;
      Object o2 = null;
      try {
        while((o1 = in.readLine()) != null) {
          o2 = in.readLine();

          System.out.println("read two objects");
          tellEveryOne(o1,o2);
        }// close while
      } catch(Exception ex) { ex.printStackTrace();}

    } // close run
  }// close inner getClass

  public void tellEveryOne(Object one, Object two) {
    Iterator it = clientOutputStreams.iterator();
    while(it.hasNext()) {
      try {
        ObjectOutputStream out = (ObjectOutputStream) it.next();
        out.writeObject(one);
        out.writeObject(two);
      } catch(Exception ex) { ex.printStackTrace();}
    }
  }// close tellEveryOne
} // close class
