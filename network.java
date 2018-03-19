import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class network {

  public static void main(String[] args) throws IOException {

    // check for port number
    int port = -1;
    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
    } else {
      System.out.println("Please enter a port number.");
      System.exit(0);
    }

    try {
      // create server socket instance
      ServerSocket serverSocket = new ServerSocket(port);

      // listen for the two incoming connections
      listen:
      System.out.println("listening...");
      Socket sender = serverSocket.accept();
      Socket receiver = serverSocket.accept();
      System.out.println("Two sockets connected");
      PrintStream senderOut = new PrintStream(sender.getOutputStream());
      BufferedReader senderIn = new BufferedReader(new InputStreamReader(sender.getInputStream()));
      PrintStream receiverOut = new PrintStream(receiver.getOutputStream());
      BufferedReader receiverIn = new BufferedReader(new InputStreamReader(receiver.getInputStream()));
      mainmenu(sender, receiver, senderOut, senderIn, receiverOut, receiverIn);

    } catch (Exception e) {
      System.out.println("Error: " + e);
    }

  }

  public static void mainmenu(Socket sender, Socket receiver, PrintStream senderOut, BufferedReader senderIn, PrintStream receiverOut, BufferedReader receiverIn) throws IOException {

    senderOut.println("hi you're the sender");

    String senderMessage = "";
    try {
      senderMessage = senderIn.readLine();
    } catch (SocketException e) {
      System.out.println("SocketException: " + e);
    }

    if (senderMessage.equals("bye")) {
      senderIn.close();
      senderOut.close();
      sender.close();
      receiverIn.close();
      receiverOut.close();
      receiver.close();
      System.exit(0);
    }

    boolean packet = false;
    boolean ack = false;

    String[] messageSplit = senderMessage.split("\\s+");
    if (messageSplit.length == 4) {
      packet = true;
    } else if (messageSplit.length == 2) {
      ack = true;
    } else {
      System.out.println("error: cannot identify if message is packet/ACK");
    }

    // choose a random value to determine operation
    double r = Math.random();
    System.out.println("random number: " + r);

    boolean pass = false;
    boolean corrupt = false;
    boolean drop = false;
    String messageToSend = "";

    if (r <= 0.50) {
      pass = true;
      System.out.println("pass");
      messageToSend = String.join(" ", messageSplit);
    } else if (r > 0.50 && r  <= 0.75) {
      corrupt = true;
      System.out.println("corrupt");
      if (packet) {
        int checksum = Integer.parseInt(messageSplit[2]);
        checksum = checksum + 1;
        messageSplit[2] = Integer.toString(checksum);
      } else if (ack) {
        int checksum = Integer.parseInt(messageSplit[1]);
        checksum = checksum + 1;
        messageSplit[1] = Integer.toString(checksum);
      } else {
        System.out.println("error: cannot identify if message is packet/ACK");
      }
      messageToSend = String.join(" ", messageSplit);
    } else if (r > 0.75) {
      drop = true;
      System.out.println("drop");
      messageToSend = "2 0";
    }

    System.out.println("message to send: " + messageToSend);

    if (packet) {

    } else if (ack) {

    } else {

    }

    mainmenu(sender, receiver, senderOut, senderIn, receiverOut, receiverIn);

  }

}
