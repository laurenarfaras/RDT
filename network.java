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
      mainmenu(sender, receiver);

    } catch (Exception e) {
      System.out.println("Error: " + e);
    }

  }

  public static void mainmenu(Socket sender, Socket receiver) throws IOException {

    Scanner sc = new Scanner(System.in);
    String command = sc.nextLine();
    System.out.println("command: " + command);

    if (command.equals("bye")) {
      sender.close();
      receiver.close();
      System.exit(0);
    }

    // choose a random value to determine operation
    double r = Math.random();
    System.out.println("random number: " + r);

    boolean pass, corrupt, drop = false;

    if (r <= 0.50) {
      pass = true;
      System.out.println("pass");
    } else if (r > 0.50 && r  <= 0.75) {
      corrupt = true;
      System.out.println("corrupt");
    } else if (r > 0.75) {
      drop = true;
      System.out.println("drop");
    }

    mainmenu(sender, receiver);

  }

}
