package model;

import java.util.ArrayList;

public class User {
    private int userId;
    private String name;
    private String email;
    private int age;
    private String direction;
    public ArrayList<Videojuego> videojuegos = new ArrayList<>();

    public User() {

    }

    public User(String name, String email, int age, String direction) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.direction = direction;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getDirection() {
        return direction;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        // Si no tenemos videojuegos en el carrito printeamos el mensaje "No tiene videojuegos en el carrito" si no la lista que tenga
        String videojuegosInfo = (videojuegos.isEmpty()) ? "###### ###### VACIO ###### ######" : printearListadoVideojuegos();

        return "Contenido del usuario \n" +
                "userId: " + userId + "\n" +
                "name: " + name + "\n" +
                "email: " + email + "\n" +
                "age: " + age + "\n" +
                "direction: " + direction + "\n" +
                "Listado de videojuegos en carrito: \n" +
                videojuegosInfo;
    }

    public String printearListadoVideojuegos() {
        StringBuilder sb = new StringBuilder();
        for (Videojuego v : this.videojuegos) {
            sb.append("ID: " + v.getGame_Id() + "\n")
                    .append("Title: " + v.getTitle() + "\n")
                    .append("Quantity: " + v.getQuantity() + "\n")
                    .append("Price: " + v.getPrice() + "\n");
        }
        return sb.toString();
    }
}
