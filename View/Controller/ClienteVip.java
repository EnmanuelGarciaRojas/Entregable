package Controller;

public class ClienteVip extends Cliente{

    private double porcentajeDescuento;

    public ClienteVip(int id, String nombre, double porcentajeDescuento){
        super(id, nombre);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double calcularDescuento(double subtotal){
        return subtotal * porcentajeDescuento;
    }

}
