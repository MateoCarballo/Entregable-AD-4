package utilities;

public class StringResources {
    public static final String MENU_ELIGIR_TECNOLOGIA = """
            Seleccione una opción:
            1. Operaciones BaseX
            2. Operaciones MongoDB
            3. Salir""";

    public static final String MENU_OPCIONES_BASEX = """
                                   ########## OPERACIONES SOBRE BASEX ##########
            
            ########## OPERACIONES DE MODIFICACIÓN Y ELIMINACIÓN ##########
            
            1. Modificar un elemento por 'ID'.
            
            2. Eliminar un videojuego según su 'ID'.
            
            ########## CONSULTAS ##########
            
            3. Consulta 1: Obtener todos los videojuegos ordenados por plataforma y 
               en segundo lugar por título (campos: id, título, precio, disponibilidad, 
               edad mínima recomendada y plataforma).
            
            4. Consulta 2: Listar videojuegos con una edad mínima recomendada menor o 
               igual a X años (campos: id, título, precio, disponibilidad, edad mínima 
               recomendada y plataforma). Ordenados por la edad mínima recomendada.
            
            5. Consulta 3: Mostrar la plataforma, el título y el precio del videojuego 
               más barato por plataforma. Si hay varios, se devolverá el primero.
            
            6. Consulta 4: Mostrar el título y género de los videojuegos cuya descripción 
               contenga una subcadena, sin importar el uso de mayúsculas o minúsculas. 
               Ordenados alfabéticamente por género.
            
            7. Consulta 5: Mostrar la cantidad total de videojuegos por plataforma 
               (considerando la disponibilidad) y calcular el porcentaje que representa 
               respecto al total. Ordenado de forma descendente por la cantidad de videojuegos.
            
            8. Consulta 6: Mostrar el precio total de todos los videojuegos disponibles 
               (considerando el precio y la disponibilidad de cada uno).
            """;

    public static final String MENU_OPCIONES_MONGO = """
                                        ########## OPERACIONES SOBRE MONGO ##########
                                      
            9. Crear un nuevo usuario (sin emails repetidos).
            
            10. Identificar un usuario por su email. El ID del usuario será obtenido para 
                realizar consultas sobre él. Para cambiar de usuario, selecciona esta opción nuevamente.
            
            11. Borrar un usuario.
            
            12. Modificar un campo en la información del usuario.
            
            13. Añadir videojuegos al carrito del usuario. Se mostrará una lista de videojuegos 
                cuya edad mínima recomendada sea inferior o igual a la del usuario actual. 
                Se pedirá: ID del videojuego, cantidad y si se desea añadir más videojuegos.
            
            14. Mostrar el carrito del usuario, incluyendo los datos del carrito y el coste total.
            
            15. Comprar el carrito del usuario. Se mostrará el contenido del carrito junto con una 
                orden de confirmación. Si la orden es positiva, los videojuegos se trasladarán a la 
                compra y desaparecerán del carrito.
            
            16. Mostrar las compras del usuario, incluyendo la fecha de cada compra.
            
            17. Consulta 1: Calcular el coste de cada carrito para todos los usuarios y listar los resultados 
                ordenados de forma descendente por el total.
            
            18. Consulta 2: Calcular el total gastado por cada usuario en todas sus compras y listar los resultados 
                ordenados de forma ascendente por el total.
            """;


    public static final String MENU_ETIQUETAS_MODIFICABLES = """
            Que quieres modificar del videojuego:
            1. Titulo.
            2. Descripcion.
            3. Precio.
            4. Disponibilidad.
            5. Genero.
            6. Desarrollador.
            7. Edad minima recomendada.
            8. Plataforma.
            """;

    public static final String QUERY_MODIFICAR_NODO_STRING_POR_ID = """
            for $videojuego in /videojuegos/videojuego[id = %d]
            return replace value of node $videojuego/%s with "%s"
            """;
    public static final String QUERY_MODIFICAR_NODO_INT_POR_ID = """
            for $videojuego in /videojuegos/videojuego[id = %d]
            return replace value of node $videojuego/%s with "%d"
            """;
    public static final String QUERY_MODIFICAR_NODO_DOUBLE_POR_ID = """
            for $videojuego in /videojuegos/videojuego[id = %d]
            return replace value of node $videojuego/%s with '%s'
            """;

    public static final String QUERY_ELIMINAR_POR_ID = """
            for $videojuego in /videojuegos/videojuego[id = %d]
            return delete node $videojuego
            """;

    /* DEVUELVE EN FORMATO XML
    public static final String QUERY_1 = """
            for $videojuego in /videojuegos/videojuego
            order by $videojuego/plataforma, $videojuego/titulo
            return <videojuego>{$videojuego/id}{$videojuego/titulo}{$videojuego/precio}{$videojuego/disponibilidad}{$videojuego/edad_minima_recomendada}{$videojuego/plataforma}
            </videojuego>
            """;
     */

    public static final String QUERY_1 = """
            for $videojuego in /videojuegos/videojuego
            order by $videojuego/plataforma, $videojuego/titulo
            return concat("ID: ",$videojuego/id," Titulo: ", $videojuego/titulo," Precio: ", $videojuego/precio, " Disponibilidad: ", $videojuego/disponibilidad, " Edad minima: ", $videojuego/edad_minima_recomendada, " Plataforma: ",$videojuego/plataforma)
            """;
    /* DEVUELVE EN FORMATO XML
     public static String QUERY_2 = """
                for $videojuego in /videojuegos/videojuego
                where $videojuego/edad_minima_recomendada < %d
                order by $videojuego/edad_minima_recomendada
                return <videojuego>{$videojuego/id}{$videojuego/titulo}{$videojuego/precio}{$videojuego/disponibilidad}{$videojuego/edad_minima_recomendada}{$videojuego/plataforma}</videojuego>
                """;
     */
    public static String QUERY_2 = """
            for $videojuego in /videojuegos/videojuego
            where xs:integer($videojuego/edad_minima_recomendada) < %d
            order by xs:integer($videojuego/edad_minima_recomendada)
            return concat("ID: ",$videojuego/id," Titulo: ",$videojuego/titulo," Precio: ",$videojuego/precio," Disponibilidad: ",$videojuego/disponibilidad," Edad mínima recomendada: ",$videojuego/edad_minima_recomendada," Plataforma: ",$videojuego/plataforma)
            """;
    public static final String QUERY_3 = """
            for $plataforma in distinct-values(/videojuegos/videojuego/plataforma)
            let $precio :=  min(/videojuegos/videojuego[plataforma = $plataforma]/precio)
            let $titulo := /videojuegos/videojuego[precio = $precio]/titulo
            return concat("Plataforma: ",$plataforma," Titulo: ",$titulo[1]," Precio: ",$precio[1])
            """;
    /*
    OTRA forma de realizar la consulta 3
   for $plataforma in distinct-values(/videojuegos/videojuego/plataforma)
            let $precio :=  min(/videojuegos/videojuego[plataforma = $plataforma]/precio)
            let $titulo := head(/videojuegos/videojuego[precio = $precio]/titulo)
            return concat("Plataforma: ",$plataforma," Titulo: ",$titulo," Precio: ",$precio)
     */
    public static final String QUERY_4 = """
            let $subcadena := "%s"
            for $videojuego in /videojuegos/videojuego
            where matches($videojuego/descripcion, $subcadena, "i")
            order by $videojuego/genero
            return concat("Titulo: ",$videojuego/titulo, " Genero: ", $videojuego/genero, " Descripcion: ", $videojuego/descripcion)
            """;

    public static final String QUERY_5 = """
            let $total_videojuegos := sum(//disponibilidad)
            for $plataforma in distinct-values(/videojuegos/videojuego/plataforma)
            for $suma_videojuego in sum(/videojuegos/videojuego[plataforma = $plataforma]/disponibilidad)
            let $porcent := round(($suma_videojuego div $total_videojuegos) * 100,2)
            order by count(/videojuegos/videojuego[plataforma = $plataforma]/disponibilidad) descending
            return  concat("La plataforma ",$plataforma," tiene un total de  ",$suma_videojuego," lo que representa un porcentaje del ",$porcent," % del total")
            """;

    public static final String QUERY_6 = """
            let $precioTodosVideojuegos := sum(
              for $videojuego in /videojuegos/videojuego
              let $precioUnidad := $videojuego/precio
              let $unidades := $videojuego/disponibilidad
              return $precioUnidad * $unidades)
            return concat("El precio total de comprar todos los videojuegos es ",format-number($precioTodosVideojuegos, '#0.00'))
            """;

    public static String QUERY_13 = """
            for $videojuego in /videojuegos/videojuego
            where $videojuego/edad_minima_recomendada < %d
            order by $videojuego/edad_minima_recomendada
            return string-join(($videojuego/id/text(),$videojuego/titulo/text(),$videojuego/precio/text()),",")
            """;

    public static final String MENU_CLAVE_MODIFICABLES_USUARIOS = """
            Que quieres modificar de %s:
            1. Nombre.
            2. Email.
            3. Age.
            4. Direccion.
            0. Salir
            """;

    public static final String CORREO_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$";
    public static final String NOMBRE_PATTERN = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+){1,2}$";

    public static final String CUADRO_COLOR_AZUL_MOSTRAR = """
    \033[34m*******************************
     %s 
    *******************************
    
    %s
    
    \033[0m
    """;

    public static final String CUADRO_COLOR_AZUL_MOSTRAR_VIDEOJUEGO = """
    \033[34m*******************************
        VIDEOJUEGO  
    *******************************
        ID %d
        NOMBRE %s
        CANTIDAD: %d
        PRECIO: %s
    \033[0m
    """;

    public static final String CUADRO_COLOR_AZUL_MOSTRAR_COMPRA = """
    \033[34m*******************************
     Compras realizadas por: %s 
    *******************************
    
    ID COMPRA: %d
    FECHA COMPRA: %s
    TOTAL: %s\033[0m
    """;
    public static final String CUADRO_COLOR_AZUL_MOSTRAR_TOTAL_COMPRAS =
    """
     \033[34m***************************************
     Total gastado por el usuario con id : %s
     ***********************************************
     Ha gastado un total de %s en todas sus compras 
     ***********************************************
            \033[0m
     """;


    private StringResources() {
    }
}
