package Controller;

public class Pedido {
    private int id;
    private Cliente cliente;
    private EstadoPedido estado;
    private DetallePedido[] detalles;
    private int contador;

    public Pedido(int id, Cliente cliente, int maxDetalles){
        this.id = id;
        this.cliente = cliente;
        this.estado = EstadoPedido.BORRADOR;
        this.detalles = new DetallePedido[maxDetalles];
    }

    public int getId(){
        return id;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public void agregarProducto(Producto p, int cantidad){
        if(estado != EstadoPedido.BORRADOR){
            throw new IllegalStateException("No se pueden agregar productos a un pedido que no está en estado BORRADOR.");
        }

        detalles[contador++] = new DetallePedido(p, cantidad);
    }

    public double calcularSubtotal(){
        double suma = 0;
        for(int i = 0; i < contador; i++){
            suma += detalles[i].getSubtotal();
        }
        return suma;
    }

    public double calcularDescuento(){
        return cliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal(){
        return calcularSubtotal() - calcularDescuento();
    }

    public void confirmar(){
        if(contador == 0){
            throw new IllegalStateException("No se puede confirmar un pedido sin productos.");
        }

        if(estado == EstadoPedido.BORRADOR){
            for(int i = 0; i < contador; i++){
                detalles[i].getProducto().disminuirStock(detalles[i].getCantidad());
            }

            estado = EstadoPedido.CONFIRMADO;
        }
    }

    public void cancelar(){
        if(estado == EstadoPedido.CONFIRMADO){
            for(int i = 0; i < contador; i++){
                detalles[i].getProducto().aumentarStock(detalles[i].getCantidad());
            }

            estado = EstadoPedido.CANCELADO;
        }
    }

    public void mostrarDetalle() {
        if (contador == 0) {
            System.out.println("El pedido no tiene productos.");
            System.out.println("Estado: " + estado);
            return;
        }

        System.out.println("\n===== DETALLE PEDIDO =====");

        for (int i = 0; i < contador; i++) {
            System.out.println(detalles[i].getProducto().getNombre() + " x " + detalles[i].getCantidad());
        }

        System.out.println("Subtotal: " + calcularSubtotal());
        System.out.println("Descuento: " + calcularDescuento());
        System.out.println("Total: " + calcularTotal());
        System.out.println("Estado: " + estado);
    }
}
