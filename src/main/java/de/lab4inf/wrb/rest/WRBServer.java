package de.lab4inf.wrb.rest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Objects;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Bootstrap the HttpServer at default localhost:8080. You can specifiy at the command line host and port via
 * <pre>
 *      WRBServer -h hostname -p portnumber.
 * </pre>
 *
 */
public class WRBServer {
    /**
     * Internal response filter to handle CORS request from everywhere.
     */
    static public class WRBResponseFilter implements ContainerResponseFilter {
        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
                throws IOException {
            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
        }
    }

    public static final Package pack = WRBServer.class.getPackage();
    // public static final String optional PATH.";
    public static final String PATH = "";
    // Base URI the HTTP server will listen on.
    public static String BASE_URI;
    // our one and only (singleton) server at a time.
    public static HttpServer server;

    /**
     * Initialisation of the HttpServer parging the host and port number.
     * @param args from the comand line
     */
    static void initialize(String[] args) {
        String host = "localhost";
        int port = 8080;
        try {
            for (int k = 0; k < args.length; k++) {
                if ("-h".equals(args[k])) {
                    host = args[++k];
                } else if ("-p".equals(args[k])) {
                    port = Integer.parseInt(args[++k]);
                }
            }
            InetAddress addr = Objects.requireNonNull(InetAddress.getByName(host));
            host = addr.getHostName();
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.printf("couldn't resolve addr..." + e);
        }
        BASE_URI = String.format("http://%s:%d/%s", host, port, PATH);
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String[] args) {
        if (null==server) {
            // evaluate the comand line arguments
            initialize(args);
            // create a resource config that scans for JAX-RS resources and providers
            // in the server package.
            final ResourceConfig rc = new ResourceConfig().packages(pack.getName());
            // register our internal response filter
            rc.register(WRBResponseFilter.class);
            // create and start a new instance of grizzly http server
            // exposing the Jersey application at BASE_URI
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        }
        return server;
    }

    /**
     * Stops the underlying HttpServer instance. 
     */
    public static void stopServer() {
        if (null!=server) {
            server.shutdownNow();
            server = null;
        }
    }
    
    /**
     * Main method.
     * @param args with optional host and port parameters.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        server = startServer(args);
        System.out.printf("Server started with WADL at %sapplication.wadl\nHit enter to stop it...", BASE_URI);
        System.in.read();
        stopServer();
    }
}
