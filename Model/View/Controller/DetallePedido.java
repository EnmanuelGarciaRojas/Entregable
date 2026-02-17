package Controller;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    public double getSubtotal(){
        return cantidad * precioUnitario;
    }

    public Producto getProducto(){
        return producto;
    }

    public int getCantidad(){
        return cantidad;
    }
}
