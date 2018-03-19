import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class receiver {

  public static int totalReceived = 0;
  public static int previousId = 0;
  public static String fullMsg = "";

  public static void main(String[] args) throws IOException {

    // check for url and port number
    String url = "";
    int port = -1;
    if (args.length == 2) {
      url = args[0].replaceAll("\\s+", "");
      port = Integer.parseInt(args[1]);
    } else {
      System.out.println("java receiver <url> <port>");
      System.exit(0);
    }

    // start new socket connection with the url and port
    Socket s = new Socket(url, port);
    // create reading/writing streams to communicate with the network socket server
    PrintStream networkOut = new PrintStream(s.getOutputStream());
    BufferedReader networkIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
    // call mainmenu function to begin communicating with network
    mainmenu(s, networkOut, networkIn);

  }

  public static void mainmenu(Socket s, PrintStream networkOut, BufferedReader networkIn) throws IOException {

    // read in message from network
    String messageIn = "";
    try {
      messageIn = networkIn.readLine();
    } catch (Exception e) {
      System.err.println("Error: " + e);
    }

    System.out.println("message in: " + messageIn);
    String[] messageSplit = messageIn.split("\\s+");
    String messageOut = "";
    boolean overwrite = false;

    if (messageSplit.length == 2) {
      // if get ACK2 then emulate DROP function
      // send ACK2 to resend
      messageOut = "2 0";
    } else if (messageSplit.length == 4) {
      // received packet
      // verify checksum, sequence number, and id
      boolean cs = checksum(messageSplit[3], Integer.parseInt(messageSplit[2]));
      String sn = messageSplit[0];
      boolean correctId;
      int id = Integer.parseInt(messageSplit[1]);
      if (id == previousId + 1) {
        previousId = id;
        correctId = true;
      } else if (id == previousId) {
        overwrite = true;
        correctId = true;
      } else {
        correctId = false;
      }

      // verify incoming message
      if (correctId && !overwrite) {
        // message is correct
        fullMsg = fullMsg + " " + messageSplit[3];
        messageOut = sn + " 0";
      } else if (overwrite) {
        // overwrite previous message
        String fullSplit[] = fullMsg.split("\\s+");
        fullSplit[fullSplit.length - 1] = messageSplit[3];
        fullMsg = String.join(" ", fullSplit);
        messageOut = sn + " 0";
      } else {
        // incorrect message
        // ask for duplicate
        messageOut = "2 0";
      }

      totalReceived = totalReceived + 1;

    }

    System.out.println("message out: " + messageOut);
    networkOut.println(messageOut);

    if (messageSplit.length == 4) {
      // check if last package in message
      String lastCharacter = messageSplit[3].substring(messageSplit[3].length() - 1);
      if (lastCharacter.equals(".")) {
        // this is the last packet in message
        // print full message
        System.out.println("Message: " + fullMsg);
        s.close();
        networkOut.close();
        networkIn.close();
        System.exit(0);
      }
    }

    // call mainmenu function to loop the program
    mainmenu(s, networkOut, networkIn);

  }

  public static boolean checksum(String message, int checksum) {
    return true;
  }

}
