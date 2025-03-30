package model;

public class Videojuego {
    private int game_Id;
    private String title;
    private int quantity;
    private double price;

    public Videojuego(int game_Id, String title, int quantity, double price) {
        this.game_Id = game_Id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getGame_Id() {
        return game_Id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void addQuantity(){
        this.quantity++;
        System.out.println("Añadida una unidad mas del videojuego " + this.title);
    }

    public void lessQuantity(){
        this.quantity++;
    }


}
