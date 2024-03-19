package server.gateway;

import java.rmi.registry.Registry;

import server.auth.AuthInterface;

public class GatewayServer {
    
    static String host = "localhost";
    static String storage = "storage";
    static AuthInterface authServer;
    public static Registry registry;

    public static void main(String[] args) {
        Gateway gateway = new Gateway();
    }
}
