package Controller;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock){
        if(precio <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }

        if(stock < 0){
            throw new IllegalArgumentException("El stock no puede tener un valor negativo");
        }

        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public double getPrecio(){
        return precio;
    }

    public int getStock(){
        return stock;
    }

    public void disminuirStock(int cantidad){
        stock -= cantidad;
    }

    public void aumentarStock(int cantidad){
        stock += cantidad;
    }
}
