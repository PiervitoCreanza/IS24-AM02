package it.polimi.ingsw.network;

import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NetworkUtils {
    public static String getCurrentHostIp(CommandLine cmd) {
        if (cmd.hasOption("localhost")) {
            return "localhost";
        }

        // Get local ip address
        if (cmd.hasOption("lan")) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                String clientIp = socket.getLocalAddress().getHostAddress();
                socket.close();
                return clientIp;
            } catch (IOException e) {
                throw new RuntimeException("Unable to retrieve your ip address. Please check your connection.");
            }
        }

        if (cmd.hasOption("c")) {
            return cmd.getOptionValue("c");
        }

        // Get public ip address
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://checkip.amazonaws.com"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().trim();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Unable to retrieve your ip address. Please check your connection.");
        }
    }
}
