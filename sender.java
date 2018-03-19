import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.nio.file.*;

public class sender {

  public static void main(String[] args) throws IOException {

    // check for url, port, and message file name
    String url = "";
    int port = -1;
    String msgFileName = "";
    if (args.length == 3) {
      url = args[0].replaceAll("\\s+", "");
      port = Integer.parseInt(args[1]);
      msgFileName = args[2].replaceAll("\\s+", "");
    } else {
      System.out.println("java receiver <url> <port> <messsageFileName>");
      System.exit(0);
    }

    // read from file
    String fileContents = new String(Files.readAllBytes(Paths.get(msgFileName)));
    System.out.println("File Contents: " + fileContents);
    String[] fileSplit = fileContents.split("\\s+");
    for (int i = 0; i < fileSplit.length; i++) {
      System.out.println(fileSplit[i]);
    }

    // start new socket connection with the url and port
    Socket s = new Socket(url, port);
    // create reading/writing streams to communicate with the network socket server
    PrintStream networkOut = new PrintStream(s.getOutputStream());
    BufferedReader networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
    // call mainmenu function to begin communicating with network
    mainmenu(s, networkOut, networkIn, fileSplit);

  }

  public static void mainmenu(Socket s, PrintStream networkOut, BufferedReader networkIn, String[] fileSplit) {

    

    // mainmenu(s, networkOut, networkIn, fileSplit);

  }

}
