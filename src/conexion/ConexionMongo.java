package conexion;

import com.mongodb.client.*;

public class ConexionMongo {
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static final String URL = "mongodb://localhost:27017";
    public static final String DATABASE_NAME = "comercio";
    public static final String COLLECTION_USERS_NAME = "Usuario";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_AGE = "age";
    public static final String FIELD_DIRECTION = "direction";
    public static final String COLLECTION_PURCHASES_NAME = "Compras";
    public static final String COLLECTION_SHOPPING_CARTS_NAME = "Carrito";

    private ConexionMongo() {
        try {
            mongoClient = MongoClients.create(URL);
            System.out.println("Creada conexion con MongoDB");
        } catch (Exception e) {
            System.out.println("Error al realizar la conexion con la base de datos");
        }
    }

    public static MongoDatabase getDataBase(String databaseName){
        try{
            if (mongoClient == null){
                new ConexionMongo();
                System.out.println("Conexion establecida con Mongo y la DB \"comercio\" ");
            }
        } catch (Exception e) {
            System.out.println("ERROR al conectar con el servidor");
        }

        if (database == null){
            database = mongoClient.getDatabase(databaseName);
            System.out.println("Accecido a base \"" + databaseName + "\"");
        }

        return database;
    }

    public static void closeConexion(){
        if (mongoClient != null){
            try{
                mongoClient.close();
            } catch (Exception e) {
                System.out.println("ERROR al intentar cerrar la conexion con Mongo");
            }
        }else{
            System.out.println("No existe conexion que cerrar");
        }
    }
}
