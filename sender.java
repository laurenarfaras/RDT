import java.io.*;
import java.util.Scanner;
import java.net.*;
import java.nio.file.*;

public class sender {

  public static String seqNo = "0";
  public static int id = 1;

  public static void main(String[] args) throws IOException {

    String url = "";
    int port = 0;
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

    // create socket
    try {
      Socket s = new Socket(url, port);
      System.out.println(s);
      // Scanner to read in from server side
      Scanner networkIn = new Scanner(new InputStreamReader(s.getInputStream()));
      // PrintStream to send to server
      PrintStream networkOut = new PrintStream(s.getOutputStream());

      String messageIn = networkIn.nextLine();
      System.out.println("message in: " + messageIn);

      // create loop to handle network communication until termination
      while (true) {
        boolean verified = false;
        // send output to server
        // String messageOut = "0 1 317 You";
        // String checksum = "100";
        String checksum = getChecksum(fileSplit[id-1]);
        String messageOut = seqNo + " " + Integer.toString(id) + " " + checksum + " " + fileSplit[id-1];
        System.out.println("message out: " + messageOut);
        networkOut.println(messageOut);
        // get feedback ACK from network
        messageIn = networkIn.nextLine();
        System.out.println("message in: " + messageIn);
        String[] inputSplit = messageIn.split("\\s+");
        if (inputSplit[0].equals(seqNo)) {
          verified = true;
        }
        System.out.println("verified? " + verified);
        if (verified) {

          // terminate gracefully
          if (fileSplit.length == id) {
            networkOut.println("bye");
            s.close();
            networkIn.close();
            networkOut.close();
            System.exit(0);
          }

          id = id + 1;
          if (seqNo.equals("0")) {
            seqNo = "1";
          } else {
            seqNo = "0";
          }

        }
      }

    } catch(IOException err) {
      System.out.println("Socket could not be created.");
      System.exit(0);
    }

  }

  public static String getChecksum(String packageContent) {
    int ansiVal;
		int cs = 0;
		for (int i = 0; i < packageContent.length(); i++) {
			ansiVal = (int) packageContent.charAt(i);
			cs = cs + ansiVal;
		}
		return Integer.toString(cs);
  }

}
