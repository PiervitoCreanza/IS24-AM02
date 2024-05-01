package it.polimi.ingsw.network;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//TCPDecoder
//command to send JSON file via netcat
//cat chosenCard.json | nc 192.168.1.75 1234
//TODO: handling of multiple clients at the same time

public class TCPClientConnectionHandler extends Thread {
    private final Socket socket;
    private final NetworkCommandMapper networkCommandMapper;

    public TCPClientConnectionHandler(Socket socket, NetworkCommandMapper networkCommandMapper) {
        super("EchoServerThread");
        this.socket = socket;
        this.networkCommandMapper = networkCommandMapper;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {

        }
        String inputLine;

        // Read from socket and echo back the input line until null (client disconnects).
        while (true) { //while for next JSON
            inputLine = keepReadingJSON(in);
            if (inputLine == null || (inputLine.isEmpty())) {
                break;
            }
            System.out.println(inputLine); // Print to console also.
            //out.println(inputLine); //Print to remote
            //out.println(); //Print to remote

            //Gson stops parsing when encounters a newline character
            //ChosenCardMessage parsedMessage = chosenCardMessageFromJson(inputLine);
            String statusMessage = this.networkCommandMapper.parse(inputLine);
            System.out.println(statusMessage);
            out.println(statusMessage); //Print to remote
        }


        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String keepReadingJSON(BufferedReader in) {
        //Method to avoid TCP fragmentation
        String inputLine = null;
        String allJSON = "";

        while (true) {
            try {
                inputLine = in.readLine();
                if (inputLine == null) {
                    break;
                }
                allJSON += inputLine;

                if (isJson(allJSON)) {
                    break;
                }
            } catch (IOException e) {
                return null;
            }
        }

        return allJSON;
    }

    private static boolean isJson(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }


}
