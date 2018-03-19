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
      // create reading streams to communicate with sender and receiver sockets
      PrintStream senderOut = new PrintStream(sender.getOutputStream());
      BufferedReader senderIn = new BufferedReader(new InputStreamReader(sender.getInputStream()));
      PrintStream receiverOut = new PrintStream(receiver.getOutputStream());
      BufferedReader receiverIn = new BufferedReader(new InputStreamReader(receiver.getInputStream()));

      // call mainmenu function to begin communicating with sockets
      mainmenu(sender, receiver, senderOut, senderIn, receiverOut, receiverIn);

    } catch (Exception e) {
      System.out.println("Error: " + e);
    }

  }

  public static void mainmenu(Socket sender, Socket receiver, PrintStream senderOut, BufferedReader senderIn, PrintStream receiverOut, BufferedReader receiverIn) throws IOException {

    // first socket to connect is sender
    senderOut.println("hi you're the sender");

    // read in message from sender
    String messageIn = "";
    try {
      messageIn = senderIn.readLine();
    } catch (SocketException e) {
      System.out.println("SocketException: " + e);
    }

    if (messageIn.equals("bye")) {
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

    String[] messageSplit = messageIn.split("\\s+");
    if (messageSplit.length == 4) {
      packet = true;
    } else if (messageSplit.length == 2) {
      ack = true;
    } else {
      System.err.println("error: cannot identify if message is packet/ACK");
    }

    // choose a random value to determine operation
    double r = Math.random();
    System.out.println("random number: " + r);

    boolean pass = false;
    boolean corrupt = false;
    boolean drop = false;
    String messageOut = "";

    // logic to implement operation based on the chosen random value
    if (r <= 0.50) {
      // if pass leave the message the same
      pass = true;
      System.out.println("pass");
      messageOut = String.join(" ", messageSplit);
    } else if (r > 0.50 && r  <= 0.75) {
      // if corrupt, add one to the checksum
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
        System.err.println("error: cannot identify if message is packet/ACK");
      }
      messageOut = String.join(" ", messageSplit);
    } else if (r > 0.75) {
      // if drop then send ACK2 message
      drop = true;
      System.out.println("drop");
      messageOut = "2 0";
    }

    // send messageOut to corresponding socket
    System.out.println("message to send: " + messageOut);
    if (packet) {
      receiverOut.println(messageOut);
    } else if (ack) {
      senderOut.println(messageOut);
    } else {
      System.err.println("error: cannot identify if message is packet/ACK");
    }

    // call mainmenu function again to loop the program
    mainmenu(sender, receiver, senderOut, senderIn, receiverOut, receiverIn);

  }

}
