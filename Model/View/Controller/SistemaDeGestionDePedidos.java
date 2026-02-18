package Controller;

import java.util.*;

public class SistemaDeGestionDePedidos {

    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    public SistemaDeGestionDePedidos(){
        productos = new ArrayList<>();
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();
    }

    private void validarNombre(String nombre){
        if(nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El ID del producto ya existe");
        }
    }

    public boolean existeProducto(int id){
        for(Producto p : productos){
            if(p.getId() == id){
                return true;
            }
        }
        return false;
    }

    public boolean existeCliente(int id){
        for(Cliente c : clientes){
            if(c.getId() == id){
                return true;
            }
        }
        return false;
    }

    public boolean existePedido(int id){
        for(Pedido p : pedidos){
            if(p.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void registrarProducto(Producto p){
        validarNombre(p.getNombre());

        if(existeProducto(p.getId())){
            throw new IllegalArgumentException("El ID del producto ya existe");
        }
        
        productos.add(p);
        System.out.println("Producto registrado correctamente.");
    }

    public void registrarCliente(Cliente c){
        validarNombre(c.getNombre());

        if(existeCliente(c.getId())){
            throw new IllegalArgumentException("ID de cliente duplicado");
        }

        clientes.add(c);
        System.out.println("Cliente registrado correctamente.");
    }

    public void buscarProducto(int id) throws ProductoNoEncontradoException{
        for(Producto p : productos){
            if(p.getId() == id){
                System.out.println("Producto encontrado");
                System.out.println("ID: " + p.getId() +
                                    "\nNombre: " + p.getNombre() +
                                    "\nPrecio: " + p.getPrecio() +
                                    "\nStock: " + p.getStock()
                );
                return;
            }
        }

        throw new ProductoNoEncontradoException("Producto no encontrado");
    }

    public Cliente buscarCliente(int id)throws Exception{
        for(Cliente c : clientes){
            if(c.getId() == id){
                return c;
            }
        }

        throw new Exception("Cliente no encontrado.");
    }

    public Pedido buscarPedido(int id)throws PedidoInvalidoException{

        for(Pedido p : pedidos){
            if(p.getId() == id){
                return p;
            }
        }
        throw new PedidoInvalidoException("Pedido no encontrado");
    }

    public void buscarProductoPorNombre(String nombre) throws ProductoNoEncontradoException{
        if(nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no debe estar vacio");
        }

        String buscado = nombre.trim().toLowerCase();

        for(Producto p : productos){
            if(p.getNombre().toLowerCase().contains(buscado)){
                System.out.println("Producto encontrado: ");
                System.out.println("ID " + p.getId() +
                                    "\nNombre: " + p.getNombre() + 
                                    "\nPrecio: " + p.getPrecio() +
                                    "\nStock: " + p.getStock()
                );
                return;
            }
        }

        throw new ProductoNoEncontradoException("El producto con el nombre ingresado no existe");
    }

    public void agregarProductoAPedido(int idPedido, int idProducto, int cantidad)throws Exception{
        Pedido pedido = buscarPedido(idPedido);

        Producto producto = null;

        for(Producto p : productos){
            if(p.getId() == idProducto){
                producto = p;
                break;
            }
        }

        if(producto == null){
            throw new ProductoNoEncontradoException("Producto no encontrado");
        }

        pedido.agregarProducto(producto, cantidad);
        System.out.println("Producto agregado al pedido correctamente");
    }

    public void cambiarEstadoPedido(int idPedido, int opcion)throws Exception{
        Pedido pedido = buscarPedido(idPedido);

        if(opcion == 1){
            pedido.confirmar();
            System.out.println("Pedido confirmado");
        }else{
            pedido.cancelar();
            System.out.println("Pedido cancelado");
        }
    }

    public void crearPedido(int id,Cliente c){

        if(existePedido(id)){
            throw new IllegalArgumentException("ID de pedido duplicado");
        }

        if(c == null){
            throw new IllegalArgumentException("Cliente inválido.");
        
        }

        pedidos.add(new Pedido(id, c));
        System.out.println("Pedido creado en estado BORRADOR.");
    }

    public void listarProductos(){

        if(productos.isEmpty()){
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("\n====Lista de productos====");
        for(Producto p : productos){
            System.out.println("ID del producto: " + p.getId() + 
                                "\nNombre: " + p.getNombre()+
                                "\nPrecio: " + p.getPrecio()+
                                "\nStock: " + p.getStock()
            );
        }
    }

    public void listarPedidos(){

        if(pedidos.isEmpty()){
            System.out.println("No hay pedidos registrados.");
            return;
        }

        System.out.println("\n====Lista de pedidos====");
        for(Pedido p : pedidos){
            System.out.println(
                "Pedido " + p.getId()+
                "\nEstado: " + p.getEstado()
            );
        }
    }

    public void listarClientes(){
        if(clientes.isEmpty()){
            System.out.println("No hay clientes registrados");
            return;
        }

        System.out.println("\n====Lista de clientes====");
        for(Cliente c : clientes){
            System.out.println("ID: " + c.getId() +
                                "\nNombre: " + c.getNombre()
            );
        }
    }
}
