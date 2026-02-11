package Controller;

public class SistemaDeGestionDePedidos {

    private Producto[] productos;
    private Cliente[] clientes;
    private Pedido[] pedidos;

    private int cp,cc,cd;

    public SistemaDeGestionDePedidos(int maxProducto,int maxClientes,int maxPedidos){
        productos = new Producto[maxProducto];
        clientes = new Cliente[maxClientes];
        pedidos = new Pedido[maxPedidos];
    }

    public boolean existeProducto(int id){
        for(int i=0;i<cp;i++){
            if(productos[i].getId()==id)
                return true;
        }
        return false;
    }

    public boolean existeCliente(int id){
        for(int i=0;i<cc;i++){
            if(clientes[i].getId()==id)
                return true;
        }
        return false;
    }

    public boolean existePedido(int id){
        for(int i=0;i<cd;i++){
            if(pedidos[i].getId()==id)
                return true;
        }
        return false;
    }

    public void registrarProducto(Producto p){

        if(existeProducto(p.getId()))
            throw new IllegalArgumentException("ID de producto duplicado");

        productos[cp++]=p;
        System.out.println("Producto registrado correctamente.");
    }

    public void registrarCliente(Cliente c){

        if(existeCliente(c.getId())){
            throw new IllegalArgumentException("ID de cliente duplicado");
        }
        clientes[cc++]=c;
        System.out.println("Cliente registrado correctamente.");
    }

    public Producto buscarProducto(int id){

        if(cp == 0){
            System.out.println("No hay productos registrados.");
            return null;
        }

        for(int i=0;i<cp;i++)
            if(productos[i].getId()==id)
                return productos[i];

        System.out.println("Producto no encontrado.");
        return null;
    }

    public Cliente buscarCliente(int id){

        if(cc==0){
            System.out.println("No hay clientes registrados.");
            return null;
        }

        for(int i=0;i<cc;i++)
            if(clientes[i].getId()==id)
                return clientes[i];

        System.out.println("Cliente no encontrado.");
        return null;
    }

    public Pedido buscarPedido(int id){

        if(cd==0){
            System.out.println("No hay pedidos registrados.");
            return null;
        }

        for(int i=0;i<cd;i++)
            if(pedidos[i].getId()==id)
                return pedidos[i];

        System.out.println("Pedido no encontrado.");
        return null;
    }

    public void crearPedido(int id,Cliente c){

        if(existePedido(id))
            throw new IllegalArgumentException("ID de pedido duplicado");

        if(c==null){
            System.out.println("Cliente inválido.");
            return;
        }

        pedidos[cd++] = new Pedido(id, c ,20);
        System.out.println("Pedido creado en estado BORRADOR.");
    }

    public void listarProductos(){

        if(cp == 0){
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("\nLista de productos:");
        for(int i=0;i<cp;i++){
            System.out.println("ID del producto: " +
                productos[i].getId() +  " - " +
                "Nombre:" +
                productos[i].getNombre()+
                " | Precio: "+productos[i].getPrecio()+
                " | Stock: "+productos[i].getStock()
            );
        }
    }

    public void listarPedidos(){

        if(cd == 0){
            System.out.println("No hay pedidos registrados.");
            return;
        }

        System.out.println("\nLista de pedidos:");
        for(int i = 0; i < cd; i++){
            System.out.println(
                "Pedido " + pedidos[i].getId()+
                " | Estado: " + pedidos[i].getEstado()
            );
        }
    }
}
