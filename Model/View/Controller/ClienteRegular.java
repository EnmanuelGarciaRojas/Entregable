package Controller;

public class ClienteRegular extends Cliente{

    public ClienteRegular(int id, String nombre){
        super(id, nombre);
    }

    public double calcularDescuento(double subtotal){
        return 0;
    }
}
