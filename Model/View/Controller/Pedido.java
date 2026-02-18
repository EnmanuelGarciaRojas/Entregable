package Controller;

import java.text.SimpleDateFormat;
import java.util.*;

public class Pedido {
    private int id;
    private Cliente cliente;
    private EstadoPedido estado;
    private List<DetallePedido> detalles;
    private Date fecha;

    public Pedido(int id, Cliente cliente){
        this.id = id;
        this.cliente = cliente;
        this.estado = EstadoPedido.BORRADOR;
        this.detalles = new ArrayList<>();
        this.fecha = new Date();
    }

    public int getId(){
        return id;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public String getFechaFormatada(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    public void agregarProducto(Producto p, int cantidad) throws StockInsuficienteException, PedidoInvalidoException{
        if(estado != EstadoPedido.BORRADOR){
            throw new PedidoInvalidoException("No se pueden agregar productos a un pedido que no está en estado BORRADOR.");
        }

        if(p.getStock() < cantidad){
            throw new StockInsuficienteException("El stock es insuficiente");
        }

        detalles.add(new DetallePedido(p, cantidad));
    }

    public double calcularSubtotal(){
        double suma = 0;
        for(DetallePedido d : detalles){
            suma += d.getSubtotal();
        }
        return suma;
    }

    public double calcularDescuento(){
        return cliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal(){
        return calcularSubtotal() - calcularDescuento();
    }

    public void confirmar() throws PedidoInvalidoException {
        if(detalles.isEmpty()){
            throw new PedidoInvalidoException("No se puede confirmar un pedido sin productos.");
        }

       
        for(DetallePedido d : detalles){
            d.getProducto().disminuirStock(d.getCantidad());
        }

        estado = EstadoPedido.CONFIRMADO;
    }

    public void cancelar(){
        if(estado == EstadoPedido.CONFIRMADO){
            for(DetallePedido d: detalles){
                d.getProducto().aumentarStock(d.getCantidad());
            }

            estado = EstadoPedido.CANCELADO;
        }
    }

    public void mostrarDetalle() {
        if (detalles.isEmpty()) {
            System.out.println("El pedido no tiene productos.");
            System.out.println("Estado: " + estado);
            return;
        }

        System.out.println("\n===== DETALLE PEDIDO =====");

        for (DetallePedido d : detalles) {
            System.out.println(d.getProducto().getNombre() + " x " + d.getCantidad());
        }

        System.out.println("Subtotal: " + calcularSubtotal());
        System.out.println("Descuento: " + calcularDescuento());
        System.out.println("Total: " + calcularTotal());
        System.out.println("Estado: " + estado);
    }
}
