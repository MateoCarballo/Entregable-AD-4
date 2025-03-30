package conexion;

import org.basex.examples.api.BaseXClient;

import java.io.IOException;

public class ConexionBaseX {
    private static BaseXClient session;
    public static final String BASE_X_HOST = "localhost";
    public static final int PORT = 1984;
    public static final String USER = "admin";
    public static final String PWD = "abc123";
    public static final String BASEX_DATABASE_NAME = "videojuegos";

    private ConexionBaseX() {}

    public static BaseXClient getSession() {
        if (session == null) {
            try {
                session = new BaseXClient(BASE_X_HOST, PORT, USER, PWD);
                System.out.println("Instaciada una nueva session con BaseX");
                session.execute("open " + BASEX_DATABASE_NAME);
                System.out.println("Abierta database en BaseX con nombre " + " ' " + BASEX_DATABASE_NAME + "' ");
            } catch (IOException e) {
                System.out.println("Error al abrir la base de datos \n" + e.getMessage());
            }
        }
        return session;
    }

    public static void closeSession() {
        if (session != null) {
            try {
                session.close();
                session = null;
                System.out.println("Session cerrada correctamente");
            } catch (IOException e) {
                System.out.println("ERROR al cerra la conexion con BaseX");
            }
        }
    }
}
