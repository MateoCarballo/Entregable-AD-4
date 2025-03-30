package main;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import conexion.ConexionBaseX;
import conexion.ConexionMongo;
import model.User;
import model.Videojuego;
import org.basex.examples.api.BaseXClient;
import org.basex.query.func.string.StringFormat;
import org.bson.Document;
import utilities.StringResources;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Sorts.descending;
import static utilities.StringResources.CUADRO_COLOR_AZUL_MOSTRAR_VIDEOJUEGO;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static org.basex.examples.api.BaseXClient session;
    private static MongoDatabase mongoDatabase;
    private static User userSelected = new User();
    /*
    Esto hace que los resutlados se muestren de una mejor forma

    private static JsonWriterSettings prettyPrintSettings = JsonWriterSettings.builder()
            .outputMode(JsonMode.STRICT)
            .indent(true)
            .build();

     Ejemplo de uso para printear de una forma mas bonita los resutlados
     System.out.println(document.toJson(prettyPrintSettings));

     */

    public static void main(String[] args) {
        //Creamos la conexiones con BaseX y MongoDB
        session = ConexionBaseX.getSession();
        mongoDatabase = ConexionMongo.getDataBase(ConexionMongo.DATABASE_NAME);
        if (session == null || mongoDatabase == null) System.exit(100);
        ejecutarMenuPrincipal();
        ConexionBaseX.closeSession();
        ConexionMongo.closeConexion();
    }

    // Menu principal para elegir la tecnologia
    private static void ejecutarMenuPrincipal() {
        boolean continuar = true;
        while (continuar) {
            int opcion = elegirOpcion(StringResources.MENU_ELIGIR_TECNOLOGIA, 1, 3);
            switch (opcion) {
                case 1 -> menuOperacionesBaseX();
                case 2 -> menuOperacionesMongoDB();
                case 3 -> continuar = false;
            }
        }
        System.out.println("""
                Cerrando APP ... 
                """);
    }

    // ############################################ OPERACIONES SOBRE BASE X ############################################
    private static void menuOperacionesBaseX() {

        int opcion = elegirOpcion(StringResources.MENU_OPCIONES_BASEX, 1, 8);

        switch (opcion) {
            case 1 -> ejecutarConsultaBaseX(preguntarFiltroParaModificarPorId());
            case 2 -> ejecutarConsultaBaseX(preguntarFiltroParaEliminarPorId());
            case 3 -> ejecutarConsultaBaseX(StringResources.QUERY_1);
            case 4 -> ejecutarConsultaBaseX(pregunarFiltroParaConsultaQuery2());
            case 5 -> ejecutarConsultaBaseX(StringResources.QUERY_3);
            case 6 -> ejecutarConsultaBaseX(preguntarFiltroParaConsultaQuery4());
            case 7 -> ejecutarConsultaBaseX(StringResources.QUERY_5);
            case 8 -> ejecutarConsultaBaseX(StringResources.QUERY_6);
        }
    }

    private static String preguntarFiltroParaModificarPorId() {

        System.out.println("Introduce el id del juego a modificar");
        int numeroEtiqueta = -1;
        String nodeToChange = "";

        int idVideojuego = preguntarId();

        numeroEtiqueta = elegirOpcion(StringResources.MENU_ETIQUETAS_MODIFICABLES, 1, 8);

        switch (numeroEtiqueta) {
            case 1 -> nodeToChange = "titulo";
            case 2 -> nodeToChange = "descripcion";
            case 3 -> nodeToChange = "precio";
            case 4 -> nodeToChange = "disponibilidad";
            case 5 -> nodeToChange = "genero";
            case 6 -> nodeToChange = "desarrollador";
            case 7 -> nodeToChange = "edad_minima_recomendada";
            case 8 -> nodeToChange = "plataforma";
        }

        System.out.println("Indica el nuevo valor para el nodo seleccionado (" + nodeToChange + ")");
        String newValue = sc.nextLine();
        if (numeroEtiqueta == 3) {
            newValue = newValue.replace(",", ".");
            double newValueDouble = Double.parseDouble(newValue);
            String newValueDotStyle = String.format(Locale.US, "%.2f", newValueDouble);
            return StringResources.QUERY_MODIFICAR_NODO_DOUBLE_POR_ID.formatted(idVideojuego, nodeToChange, newValueDotStyle);
        }
        if (numeroEtiqueta == 4 || numeroEtiqueta == 7) {
            return StringResources.QUERY_MODIFICAR_NODO_INT_POR_ID.formatted(idVideojuego, nodeToChange, Integer.parseInt(newValue));
        }
        return StringResources.QUERY_MODIFICAR_NODO_STRING_POR_ID.formatted(idVideojuego, nodeToChange, newValue);
    }

    private static int preguntarId() {
        ArrayList<Integer> listadoIds = new ArrayList<>();
        int idVideojuego = -1;
        //Preguntar qué ids existen en la database
        try {
            BaseXClient.Query ids = session.query("distinct-values(/videojuegos/videojuego/id)");
            while (ids.more()) {
                listadoIds.add(Integer.parseInt(ids.next()));
            }
        } catch (IOException e) {
            System.out.println("Error al realizar la consulta");
        }

        //Imprimirlos por pantalla

        System.out.println("Ids disponibles en la database");
        System.out.print("[ ");
        for (int i : listadoIds) {
            System.out.print(i + ", ");
        }
        System.out.println("]");

        //Recoger la eleccion y comprobar que este contenido en los existentes si no volver a preguntar
        do {
            try {
                System.out.println("Introduce el id del producto");
                idVideojuego = Integer.parseInt(sc.nextLine());
                if (!listadoIds.contains(idVideojuego)) {
                    System.out.println("Escoge un id entre los disponibles !!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ha ocurrido un error revisa los datos");
            }
        } while (!listadoIds.contains(idVideojuego));
        return idVideojuego;
    }

    private static String preguntarFiltroParaEliminarPorId() {
        System.out.println("Introduce el id del videojuego que quieres eliminar");
        int idVideojuego = preguntarId();
        return StringResources.QUERY_ELIMINAR_POR_ID.formatted(idVideojuego);
    }

    private static String pregunarFiltroParaConsultaQuery2() {
        int filtroConsulta = -1;
        do {
            try {
                System.out.println("Edad minima sobre la que filtraremos la DB");
                filtroConsulta = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un numero " + e.getMessage());
                sc.nextLine();
            }
        } while (filtroConsulta == -1);
        return StringResources.QUERY_2.formatted(filtroConsulta);
    }

    private static String preguntarFiltroParaConsultaQuery4() {
        System.out.println("Buscar en la descripción que contenga la string que introduces");
        String filtroConsulta = sc.nextLine();
        return StringResources.QUERY_4.formatted(filtroConsulta);
    }


    private static void ejecutarConsultaBaseX(String queryAsString) {   try {
            BaseXClient.Query query = session.query(queryAsString);
            while (query.more()) {
                System.out.println(query.next());
                System.out.println(session.info());
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error de tipo " + e.getMessage());
            ;
        } catch (RuntimeException e) {
            System.out.println("Revisa que BaseXGUI no este abierto y conectado a la misma base de datos " + e.getMessage());
        }
    }

    // ############################################ OPERACIONES SOBRE MONGODB ############################################
    private static void menuOperacionesMongoDB() {
        try {
            System.out.println("Obtenida la conexion con la database en Mongo: " + ConexionMongo.DATABASE_NAME);
        } catch (Exception e) {
            System.out.println("Error al abrir la base de datos" + ConexionMongo.DATABASE_NAME + "\n" + e.getMessage());
            return;
        }

        int opcion = elegirOpcion(StringResources.MENU_OPCIONES_MONGO, 9, 18);

        switch (opcion) {
            case 9 -> insertarNuevoUsuario();
            case 10 -> seleccionarUsuarioPorEmail();
            case 11 -> eliminarUsuarioPorId();
            case 12 -> modificarCampoUsuarioSeleccionado();
            case 13 -> anhadirVideojuegos();
            case 14 -> mostrarCarritoDelUsuario();
            case 15 -> comprarCarrito();
            case 16 -> mostrarComprasUsuarioSeleccionado();
            case 17 -> listarTotalCarritosMayorAMenor();
            case 18 -> listarTotalGastadoCompras();
        }
    }

    private static void insertarNuevoUsuario() {
        User userToInsert = preguntarFiltroParaCrearUsuario();
        if (userToInsert == null) {
            System.out.println("Los datos recopilados no han podido crear un usuario, revisa los datos y repite la operacion");
            return;
        }

        MongoCollection<Document> usersColection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        int newId = getHigherId(ConexionMongo.COLLECTION_USERS_NAME, "user_Id");
        userToInsert.setUserId(newId);
        Document newUser = new Document("user_Id", userToInsert.getUserId())
                .append("name", userToInsert.getName())
                .append("email", userToInsert.getEmail())
                .append("age", userToInsert.getAge())
                .append("direction", userToInsert.getDirection());
        usersColection.insertOne(newUser);
        System.out.println(StringResources.CUADRO_COLOR_AZUL_MOSTRAR.formatted("Usuario añadido con exito!", userToInsert.toString()));
    }

    private static User preguntarFiltroParaCrearUsuario() {
        String name = "";
        String email = "";
        int age = -1;
        String direction = "";

        do {
            System.out.println("Nombre del nuevo usuario(Debe contener dos partes  con solo letras)");
            name = sc.nextLine();
        } while (!name.matches(StringResources.NOMBRE_PATTERN));

        do {
            do {
                System.out.println("E-mail del nuevo usuario (correoEjemplo@dominio.es)");
                email = sc.nextLine();
            } while (!email.matches(StringResources.CORREO_PATTERN));
        } while (comprobarCampoRepetido(ConexionMongo.COLLECTION_USERS_NAME, "email", email));
        boolean datoOK = false;
        while (!datoOK) {
            System.out.println("Edad del nuevo usuario (La edad debe estar enter 1 y 99 incluidos)");
            String edadIntroducida = sc.nextLine();
            if (edadIntroducida.matches("\\d+")) {
                age = Integer.parseInt(edadIntroducida);
                if (age < 99 && age > 0) {
                    datoOK = true;
                }
            }
        }

        System.out.println("Direccion del nuevo usuario");
        direction = sc.nextLine();

        return new User(name, email, age, direction);
    }

    private static int getHigherId(String collectionName, String idField) {
        int maxUserId = 0;
        MongoCollection<Document> usersColection = mongoDatabase.getCollection(collectionName);
        Document highestUser = usersColection.find()
                .sort(descending(idField))
                .first();
        if (highestUser != null) {
            maxUserId = highestUser.getInteger(idField);
        } else {
            System.out.println("No se encontraron ningun campo en la coleccion.");
        }
        return maxUserId + 1;
    }

    private static boolean comprobarCampoRepetido(String collectionName, String campo, String valor) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document foundUser = collection.find(Filters.eq(campo, valor)).first();
        if (foundUser != null) {
            System.out.println("Este correo ya está registrado");
            return true;
        }
        return false;
    }

    public static void seleccionarUsuarioPorEmail() {
        MongoCollection<Document> colectionUsuarios = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        String email = obtenerEmail();
        if (email != null) {
            Document documentUsuario = colectionUsuarios.find(Filters.eq("email", email)).first();
            if (documentUsuario != null) {
                userSelected.setUserId(documentUsuario.getInteger("user_Id"));
                userSelected.setName(documentUsuario.getString("name"));
                userSelected.setEmail(documentUsuario.getString("email"));
                userSelected.setAge(documentUsuario.getInteger("age"));
                userSelected.setDirection(documentUsuario.getString("direction"));
                MongoCollection<Document> collectionCarts = mongoDatabase.getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);
                Document documentoCarrito = collectionCarts.find(Filters.eq("user_Id", userSelected.getUserId())).first();
                if (documentoCarrito != null) {
                    userSelected.videojuegos = new ArrayList<>();

                    List<Document> items = (List<Document>) documentoCarrito.get("items");

                    for (Document item : items) {
                        Videojuego videojuego = new Videojuego(item.getInteger("game_Id"),
                                item.getString("title"),
                                item.getInteger("quantity"),
                                item.getDouble("price"));
                        userSelected.videojuegos.add(videojuego);
                    }
                }
                System.out.println(StringResources.CUADRO_COLOR_AZUL_MOSTRAR.formatted("Has seleccionado al usuario", userSelected));
            }
        } else {
            System.out.println("La colecion esta vacia!");
        }
    }

    public static void actualizarUsuarioJava() {
        MongoCollection<Document> colectionUsuarios = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        Document documentUsuario = colectionUsuarios.find(Filters.eq("user_Id", userSelected.getUserId())).first();
        if (documentUsuario != null) {
            String name = documentUsuario.getString("name");
            String email = documentUsuario.getString("email");
            int age = documentUsuario.getInteger("age");
            String direction = documentUsuario.getString("direction");
            userSelected.setName(name);
            userSelected.setEmail(email);
            userSelected.setAge(age);
            userSelected.setDirection(direction);
            System.out.println("Has selecccionado al usuario \n" + userSelected);
        }
    }

    public static HashMap<Integer, String> obtenerUsuarios() {
        HashMap<Integer, String> paresEmail = new HashMap<>();
        FindIterable<Document> usersColection = mongoDatabase
                .getCollection(ConexionMongo.COLLECTION_USERS_NAME)
                .find()
                .projection(new Document("user_Id", 1).append("email", 1))
                .sort(Sorts.ascending("user_Id"));

        if (usersColection != null) {
            System.out.println("Opciones disponibles");
            for (Document document : usersColection) {
                int userId = document.getInteger("user_Id");
                String email = document.getString("email");
                paresEmail.put(userId, email);
            }
            return paresEmail;
        } else {
            return null;
        }
    }

    public static void printearListadosUsuariosRegistrados(HashMap<Integer, String> pares) {
        for (Map.Entry<Integer, String> hm : pares.entrySet()) {
            System.out.println(hm.getKey() + "-" + hm.getValue());
        }
    }

    public static String obtenerEmail() {
        HashMap<Integer, String> paresEmail = obtenerUsuarios();
        if (paresEmail != null) {
            printearListadosUsuariosRegistrados(paresEmail);
            while (true) {
                System.out.println("Elige una opcion, puedes indicar el id o el email");
                String entradaTeclado = sc.nextLine();
                try {
                    int id = Integer.parseInt(entradaTeclado);
                    if (paresEmail.containsKey(id)) {
                        return paresEmail.get(id);
                    } else {
                        System.out.println("\033[31m" +  // Cambiar el color del texto a rojo
                                "*******************************\n" +
                                "* El email introducido no es *\n" +
                                "* válido. Intenta nuevamente. *\n" +
                                "*******************************\n" +
                                "\033[0m");
                    }
                } catch (NumberFormatException e) {
                    if (paresEmail.containsValue(entradaTeclado)) {
                        return entradaTeclado;
                    } else {
                        System.out.println("\033[31m" +
                                "*******************************\n" +
                                "* El email introducido no es *\n" +
                                "* válido. Intenta nuevamente. *\n" +
                                "*******************************\n" +
                                "\033[0m");
                    }
                }
            }
        }
        return null;
    }

    public static void eliminarUsuarioPorId() {
        HashMap<Integer, String> paresEmail = obtenerUsuarios();
        printearListadosUsuariosRegistrados(paresEmail);
        MongoCollection<Document> usersCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        MongoCollection<Document> cartCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);
        MongoCollection<Document> purchasesCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_PURCHASES_NAME);

        while (true) {
            System.out.println("Escribe el correo que deseas eliminar");
            String entradaTeclado = sc.nextLine();
            try {
                int id = Integer.parseInt(entradaTeclado);
                if (paresEmail.containsKey(id)) {
                    System.out.println("Estas seguro que deseas eliminar el usuario ? \n (pulsa 'y'para eliminar o cualquier otra para canecelar)");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        usersCollection.deleteOne(Filters.eq("user_Id", id));
                        cartCollection.deleteOne(Filters.eq("user_Id", id));
                        purchasesCollection.deleteMany(Filters.eq("user_Id", id));
                        if (userSelected.getUserId() == id) {
                            System.out.println("Has eliminado de la base de datos Mongo el usuario que habias seleccionado en el punto 10");
                            userSelected = new User();
                        }
                    }
                    return;
                }
            } catch (NumberFormatException e) {
                if (paresEmail.containsValue(entradaTeclado)) {
                    for (Map.Entry<Integer, String> parClaveValor : paresEmail.entrySet()) {
                        if (parClaveValor.getValue().equals(entradaTeclado)) {
                            System.out.println("Estas seguro que deseas eliminar el usuario ? \n (pulsa 'y'para eliminar o cualquier otra para canecelar)");
                            if (sc.nextLine().equalsIgnoreCase("y")) {
                                usersCollection.deleteOne(Filters.eq("user_Id", parClaveValor.getKey()));
                                cartCollection.deleteOne(Filters.eq("user_Id", parClaveValor.getKey()));
                                purchasesCollection.deleteMany(Filters.eq("user_Id", parClaveValor.getKey()));
                                System.out.println("Usuario eliminado con exito !");
                                if (userSelected.getUserId() == parClaveValor.getKey()) {
                                    System.out.println("Has eliminado de la base de datos Mongo el usuario que habias seleccionado en el punto 10");
                                    userSelected = new User();
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void modificarCampoUsuarioSeleccionado() {
        if (userSelected.getName() == null) {
            System.out.println("Debes seleccionar un usuario en el punto 10 para poder modificar sus campos");
            return;
        }
        int entradaTeclado;
        String name = "";
        int age = 0;
        String email = "";
        String direction = "";

        do {
            entradaTeclado = elegirOpcion(StringResources.MENU_CLAVE_MODIFICABLES_USUARIOS.formatted(userSelected.getName()), 0, 4);
            switch (entradaTeclado) {
                case 1 -> {
                    do {
                        System.out.println("Introduce el nuevo nombre del usuario\n(Al menos dos partes 'Ejemplo: Mateo Carballo')");
                        name = sc.nextLine();
                        if (name.matches(StringResources.NOMBRE_PATTERN)) {
                            actualizarCampoString(ConexionMongo.FIELD_NAME, name);
                            userSelected.setName(name);
                        }
                    } while (!name.matches(StringResources.NOMBRE_PATTERN));
                }
                case 2 -> {
                    do {
                        System.out.println("Introduce el nuevo email del usuario\n(Al menos dos partes 'Ejemplo1: ejemplo@dominio.ext)'");
                        email = sc.nextLine();
                        if (email.matches(StringResources.CORREO_PATTERN)) {
                            actualizarCampoString(ConexionMongo.FIELD_EMAIL, email);
                            userSelected.setEmail(email);
                        }
                    } while (!email.matches(StringResources.CORREO_PATTERN));
                }
                case 3 -> {
                    boolean datoOK = false;
                    while (!datoOK) {
                        System.out.println("Escribe la nueva edad del usuario\n(La edad debe estar enter 1 y 99 incluidos)");
                        String edadIntroducida = sc.nextLine();
                        if (edadIntroducida.matches("\\d+")) {
                            age = Integer.parseInt(edadIntroducida);
                            if (age < 99 && age > 0) {
                                datoOK = true;
                            }
                        }
                    }
                    actualizarCampoInt(ConexionMongo.FIELD_AGE, age);
                    userSelected.setAge(age);
                }
                case 4 -> {
                    System.out.println("Escribe la nueva direccion del usuario\n(Esto es ciudad sin ley como si le pones que vieve en Marte)");
                    direction = sc.nextLine();
                    actualizarCampoString(ConexionMongo.FIELD_DIRECTION, direction);
                    userSelected.setDirection(direction);
                }
            }
        } while (entradaTeclado != 0);

    }

    public static void actualizarCampoString(String fieldName, String value) {
        MongoCollection<Document> userCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        userCollection.updateOne(
                Filters.eq(ConexionMongo.FIELD_NAME, userSelected.getName()),
                Updates.set(fieldName, value)
        );
    }

    public static void actualizarCampoInt(String fieldName, int value) {
        MongoCollection<Document> userCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_USERS_NAME);
        userCollection.updateOne(
                Filters.eq(ConexionMongo.FIELD_NAME, userSelected.getName()),
                Updates.set(fieldName, value)
        );

    }

    public static void anhadirVideojuegos() {

        if (userSelected.getName() == null) {
            System.out.println("Selecciona un usuario para agregar items al carrito");
            return;
        }
        añadirVideojuegoJava();
        insertarDatosEnMongo();
    }

    public static void añadirVideojuegoJava() {

        ArrayList<Videojuego> videojuegosDisponibles;
        boolean continuar = true;
        do {
            printearListadoVideojuegosDisponibles();
            videojuegosDisponibles = obtenerListadoJuegosComoArrayList();
            if (videojuegosDisponibles.isEmpty()) {
                System.out.println("Imposible cargar los datos en la arrayList, comprueba los datos en BaseX o al edad del usuario");
                return;
            }

            try {
                boolean existeJuegoEnCarrito = false;
                int idSeleccionado = elegirIdVideojuego(videojuegosDisponibles);
                for (Videojuego juegoEnCarrito : userSelected.videojuegos) {
                    if ((idSeleccionado == juegoEnCarrito.getGame_Id())) {
                        existeJuegoEnCarrito = true;
                    }
                }

                if (!existeJuegoEnCarrito) {
                    for (Videojuego v : videojuegosDisponibles) {
                        if (v.getGame_Id() == idSeleccionado) {
                            userSelected.videojuegos.add(v);
                            System.out.println("Has añadido el videojuego " + v.getTitle() + " al carrito de " + userSelected.getName());
                        }
                    }
                } else {
                    for (Videojuego v : userSelected.videojuegos) {
                        if (idSeleccionado == v.getGame_Id()) {
                            v.addQuantity();
                        }
                    }
                }
                System.out.println(userSelected);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Entrada inválida, ingrese un número.");
            }
            System.out.println("Deseas añadir mas juegos al carrito?(y/n)");
            if (!sc.nextLine().equalsIgnoreCase("y")) continuar = false;
        } while (continuar);
    }

    public static void printearListadoVideojuegosDisponibles() {
        System.out.println("\033[34m" + "Videojuegos disponibles en BaseX:" + "\033[0m");
        BaseXClient.Query query = null;
        try {
            query = session.query(StringResources.QUERY_2.formatted(userSelected.getAge()));
            while (query.more()) {
                System.out.println("\033[34m" + query.next() + "\033[0m");
            }
        } catch (IOException e) {
            System.out.println("Error al printear los videojuegos seleccionables");
        }
    }

    public static ArrayList<Videojuego> obtenerListadoJuegosComoArrayList() {
        ArrayList<Videojuego> games = new ArrayList();
        BaseXClient.Query query;
        try {
            query = session.query(StringResources.QUERY_13.formatted(userSelected.getAge()));
            while (query.more()) {
                String cadenaResultado = query.next();
                String[] spliteada = cadenaResultado.split(",");
                games.add(new Videojuego(Integer.parseInt(spliteada[0]), spliteada[1], 1, Double.parseDouble(spliteada[2])));
            }
        } catch (IOException e) {
            System.out.println("Error al obtener los videojuegos como arrayList");
        }
        return games;
    }

    public static int elegirIdVideojuego(ArrayList<Videojuego> videojuegosDisponibles) {
        int opcion;
        while (true) {
            System.out.println("Elige un videojuego de la lista por Id");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                for (Videojuego v : videojuegosDisponibles) {
                    if (v.getGame_Id() == opcion) {
                        return opcion;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes elegir un numero");
            }
        }
    }

    public static void insertarDatosEnMongo() {
        MongoCollection<Document> cartCollection = mongoDatabase.getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);
        List<Document> nuevosItems = new ArrayList<>();
        for (Videojuego v : userSelected.videojuegos) {
            nuevosItems.add(new Document("game_Id", v.getGame_Id())
                    .append("title", v.getTitle())
                    .append("quantity", v.getQuantity())
                    .append("price", v.getPrice()));
        }
        Document filtroCarrito = new Document("user_Id", userSelected.getUserId());
        Document carritoActualizado = new Document("user_Id", userSelected.getUserId())
                .append("items", nuevosItems);
        FindIterable<Document> existeCarrito = cartCollection.find(Filters.eq("user_Id", userSelected.getUserId()));
        if (existeCarrito.first() == null) {
            cartCollection.insertOne(carritoActualizado);
        } else {
            cartCollection.updateOne(Filters.eq("user_Id", userSelected.getUserId()), new Document("$set", carritoActualizado));
        }

        System.out.println("Se ha creado un nuevo carrito y se han añadido los videojuegos.");
    }

    public static void mostrarCarritoDelUsuario() {
        MongoCollection<Document> carritos = mongoDatabase.getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);


        AggregateIterable<Document> resultado = carritos.aggregate(Arrays.asList(
                new Document("$match", new Document("user_Id", userSelected.getUserId())), // Filtramos por user_Id
                new Document("$addFields", new Document("PrecioTotal", // Agregamos el campo PrecioTotal
                        new Document("$sum", new Document("$map", new Document("input", "$items") // Iteramos sobre los items
                                .append("as", "item")
                                .append("in", // Para cada item, multiplicamos la cantidad por el precio
                                        new Document("$multiply", Arrays.asList("$$item.quantity", "$$item.price"))
                                )
                        ))
                )),
                new Document("$project", // Proyectamos los campos que queremos mostrar
                        new Document("_id", 0L) // No mostramos el campo _id
                                .append("items", 1L) // Incluimos los items
                                .append("PrecioTotal", 1L) // Incluimos el campo PrecioTotal
                )
        ));

        // Obtener el primer documento, ya que debería ser único para el usuario
        Document carrito = resultado.first();

        if (carrito != null) {
            // Mostramos los items y el total
            ArrayList<Document> itemsCarrito = (ArrayList) carrito.get("items");
            double total = carrito.getDouble("PrecioTotal");
            System.out.println("El usuario %s con id: %d tiene estos elementos en su carrito:".formatted(userSelected.getName(), userSelected.getUserId()));

            for (Document item : itemsCarrito) {
                int gameId = item.getInteger("game_Id");
                String title = item.getString("title");
                int quantity = item.getInteger("quantity");
                String price = String.format("%.2f", item.getDouble("price"));
                System.out.println(CUADRO_COLOR_AZUL_MOSTRAR_VIDEOJUEGO.formatted(gameId, title, quantity, price));
            }
            String price = String.format("%.2f", total);
            System.out.println(""" 
                        \033[34mTOTAL: %s  \033[0m
                    """.formatted(price));
        } else {
            System.out.println("El carrito del usuario está vacío");
        }
    }

    public static void comprarCarrito() {
        if (userSelected == null) {
            return;
        }
        int user_id = -1;
        double valorCarrito = 0.0;
        MongoCollection<Document> carritos = mongoDatabase.getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);
        AggregateIterable<Document> resultados = carritos.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("user_Id", userSelected.getUserId())),
                        Aggregates.unwind("$items"),
                        Aggregates.group("$user_Id",
                                Accumulators.sum(
                                        "Total_carrito",
                                        new Document("$multiply", Arrays.asList("$items.quantity", "$items.price")
                                        )
                                )
                        )
                )
        );
        for (Document parIdTotal : resultados) {
            user_id = parIdTotal.getInteger("_id");
            valorCarrito = parIdTotal.getDouble("Total_carrito");
        }

        Document results = carritos.find(Filters.eq("user_Id", userSelected.getUserId()))
                .projection(new Document("items", 1)
                        .append("_id", 0))
                .first();
        Document nuevaCompra = new Document("purchase_id", getHigherId(ConexionMongo.COLLECTION_PURCHASES_NAME, "purchase_id"))
                .append("user_Id", user_id)
                .append("items", results)
                .append("total", valorCarrito)
                .append("purchase_date", new Date());

        MongoCollection compras = mongoDatabase.getCollection(ConexionMongo.COLLECTION_PURCHASES_NAME);
        if (confirmarOperacion()) {
            compras.insertOne(nuevaCompra);
            carritos.deleteOne(Filters.eq("user_Id", user_id));
            userSelected.videojuegos = new ArrayList<>();
        }
    }

    private static void mostrarComprasUsuarioSeleccionado() {
        if (userSelected.getName() == null) {
            System.out.println("Debes seleccionar un usuario");
            return;
        }

        MongoCollection<Document> compras = mongoDatabase.getCollection(ConexionMongo.COLLECTION_PURCHASES_NAME);

        FindIterable<Document> resultados = compras.find(Filters.eq("user_Id", userSelected.getUserId()));

        for (Document res : resultados) {
            int purchaseId = res.getInteger("purchase_id");
            Date fechaCompra = res.getDate("purchase_date");
            String total = String.format("%.2f", res.getDouble("total"));

            System.out.println(StringResources.CUADRO_COLOR_AZUL_MOSTRAR_COMPRA.formatted(userSelected.getName(), purchaseId, fechaCompra.toString(), total));
        }
    }


    private static void listarTotalCarritosMayorAMenor() {
        AggregateIterable<Document> iterDoc = mongoDatabase
                .getCollection(ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME)
                .aggregate(
                        Arrays.asList(
                                Aggregates.unwind("$items"),
                                Aggregates.addFields(
                                        new Field<>("totalItemCost",
                                                new Document("$multiply", Arrays.asList(
                                                        "$items.quantity", "$items.price"
                                                ))
                                        )
                                ),
                                Aggregates.lookup("Usuarios", "user_Id", "user_Id", "userInfo"),
                                Aggregates.group("$user_Id",
                                        Accumulators.sum("totalCost", "$totalItemCost")
                                ),
                                Aggregates.sort(descending("totalCost")))
                );
        System.out.println("CARRITOS ORDENADOR DE MAYOR A MENOR PRECIO TOTAL:");
        for (Document document : iterDoc) {
            System.out.println("=====================================");
            int userId = document.getInteger("_id");
            String totalCost = String.format( "%.2f", document.getDouble("totalCost"));
            System.out.println("ID: " + userId);
            System.out.println("COSTE TOTAL: " + totalCost);
            System.out.println("=====================================");
        }
    }

    private static void listarTotalGastadoCompras() {
        MongoCollection<Document> collectionCompras = mongoDatabase.getCollection(ConexionMongo.COLLECTION_PURCHASES_NAME);

        AggregateIterable<Document> resultado = collectionCompras.aggregate(
                Arrays.asList(
                // Desempaquetamos el array de items para procesarlos por separado
                new Document("$unwind", "$items"),

                // Calculamos el total por item (quantity * unit_price)
                new Document("$addFields", new Document("itemTotal",
                        new Document("$multiply", Arrays.asList("$items.quantity", "$items.unit_price"))
                )),

                // Agrupamos por user_Id y sumamos los totales de los items
                new Document("$group", new Document("_id", "$user_Id")
                        .append("totalGastado", new Document("$sum", "$itemTotal"))
                ),

                // Ordenamos por totalGastado de menor a mayor
                new Document("$sort", new Document("totalGastado", 1))
        ));

        for (Document doc : resultado) {
            System.out.println(StringResources.CUADRO_COLOR_AZUL_MOSTRAR_TOTAL_COMPRAS.formatted(
                    doc.getInteger("_id"),
                    String.format("%.2f", doc.getDouble("totalGastado"))
            ));
        }
    }


    // ############################################ OPERACIONES GLOBALES ############################################
    private static boolean confirmarOperacion() {
        while (true) {
            System.out.println("Pulsa 'y' para confirmar la compra o cualquier otra tecla para cancelar la operacion");
            try {
                if (Main.sc.nextLine().equalsIgnoreCase("y")) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("A ocurrido alguna excepcion al recoger datos del teclado");
                System.out.println(e.getMessage());
            }
        }
    }

    private static int elegirOpcion(String menu, int min, int max) {
        int opcion = -1;
        do {
            try {
                System.out.println(menu);
                opcion = Integer.parseInt(sc.nextLine());
                if (opcion < min || opcion > max) {
                    System.out.println("ERROR: Opción fuera de rango");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Entrada inválida, ingrese un número.");
            }
        } while (opcion < min || opcion > max);
        return opcion;

    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }
}
    /*CONSULTA REALIZADA CON LA FUNCION DE TRANSFORMAR A JAVA DESDE MONGO PARA EL PUNTO 14
        CONSULTA NIVEL JESUCRISTO
        Arrays.asList(new Document("$addFields",
        new Document("total",
        new Document("$sum",
        new Document("$map",
        new Document("input", "$items")
                        .append("as", "item")
                        .append("in",
        new Document("$multiply", Arrays.asList("$$item.quantity", "$$item.price"))))))),
        new Document("$project",
        new Document("_id", 0L)
            .append("items", 1L)
            .append("total", 1L)))
             Arrays.asList(
                       Aggregates.match(Filters.eq("user_Id",userSelected.getUserId())),
                       Aggregates.unwind("$items"),
                       Aggregates.group(
                               "$user_Id",
                               Accumulators.sum("Total_carrito",
                                       new Document("$multiply",Arrays.asList(
                                               "$items.quantity","$items.price")))
                       )
               )
         */
    /* METODO 14 MONTANDO LOS DATOS EN JAVA

        MongoCollection<Document> carritos = mongoDatabase.getCollection(conexion.ConexionMongo.COLLECTION_SHOPPING_CARTS_NAME);
        //Aqui no se como usar las agregaciones para poder filtrar solo el carro del usuario si existe y despues
        // sobre el resultado iterar la suma de todos los campos 'quantity' dentro del array de item

        Document carrito = carritos.find(Filters.eq("user_Id", userSelected.getUserId())).first();
        if (carrito != null) {
            double total = Double.MIN_VALUE;
            int id = carrito.getInteger("user_Id");
            ArrayList<Document> itemsCarrito = (ArrayList) carrito.get("items");
            System.out.println("El usuario %s con id: %d tiene estos elementos en su carrito:".formatted(userSelected.getName(), userSelected.getUserId()));
            for (Document item : itemsCarrito) {
                total+= item.getDouble("price");
                int gameId = item.getInteger("game_Id");
                String title = item.getString("title");
                int quantity = item.getInteger("quantity");
                String price = String.format("%.2f", item.getDouble("price"));
                System.out.println("""
                        VIDEOJUEGO
                            ID: %d
                            NOMBRE: %s
                            CANTIDAD: %d
                            PRECIO: %s
                        """.formatted(gameId, title, quantity, price));
            }
            String price = String.format("%.2f",total);
            System.out.println("""
                            TOTAL: %s
                        """.formatted(price));
        } else {
            System.out.println("El carrito del usuario está vacio");
        }
     */

