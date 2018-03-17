import java.io.*;
import java.util.*;
import java.net.*;

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

    // create server socket instance
    ServerSocket serverSocket = new ServerSocket(port);

    listen:
    System.out.println("listening...");
    serverSocket.accept();



  }

}
