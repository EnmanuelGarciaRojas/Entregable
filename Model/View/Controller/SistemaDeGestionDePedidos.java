package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SistemaDeGestionDePedidos {

    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    private Thread procesadorThread;
    private Thread reporteThread;
    private final Object fileLock = new Object();

    public SistemaDeGestionDePedidos(){
        productos = new ArrayList<>();
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();
    }

    public void iniciarHilos(){
        procesadorThread = new Thread(new ProcesadorPedidos());
        procesadorThread.setDaemon(true);
        procesadorThread.start();

        reporteThread = new Thread(new GeneradorReportes());
        reporteThread.setDaemon(true);
        reporteThread.start();
    }

    private void validarNombre(String nombre){
        if(nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El ID del producto ya existe");
        }
    }

    public synchronized boolean existeProducto(int id){
        for(Producto p : productos){
            if(p.getId() == id){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean existeCliente(int id){
        for(Cliente c : clientes){
            if(c.getId() == id){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean existePedido(int id){
        for(Pedido p : pedidos){
            if(p.getId() == id){
                return true;
            }
        }
        return false;
    }

    public synchronized void registrarProducto(Producto p){
        validarNombre(p.getNombre());

        if(existeProducto(p.getId())){
            throw new IllegalArgumentException("El ID del producto ya existe");
        }
        
        productos.add(p);
        guardarSistema();
        System.out.println("Producto registrado correctamente.");
    }

    public synchronized void registrarCliente(Cliente c){
        validarNombre(c.getNombre());

        if(existeCliente(c.getId())){
            throw new IllegalArgumentException("ID de cliente duplicado");
        }

        clientes.add(c);
        guardarSistema();
        System.out.println("Cliente registrado correctamente.");
    }

    public synchronized void buscarProducto(int id) throws ProductoNoEncontradoException{
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

    public synchronized Cliente buscarCliente(int id)throws Exception{
        for(Cliente c : clientes){
            if(c.getId() == id){
                return c;
            }
        }

        throw new Exception("Cliente no encontrado.");
    }

    public synchronized Pedido buscarPedido(int id)throws PedidoInvalidoException{

        for(Pedido p : pedidos){
            if(p.getId() == id){
                return p;
            }
        }
        throw new PedidoInvalidoException("Pedido no encontrado");
    }

    public synchronized void buscarProductoPorNombre(String nombre) throws ProductoNoEncontradoException{
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

    public synchronized void agregarProductoAPedido(int idPedido, int idProducto, int cantidad)throws Exception{
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
        guardarSistema();
        System.out.println("Producto agregado al pedido correctamente");
    }

    public synchronized void cambiarEstadoPedido(int idPedido, int opcion)throws Exception{
        Pedido pedido = buscarPedido(idPedido);

        if(opcion == 1){
            pedido.confirmar();
            System.out.println("Pedido confirmado");
        }else{
            pedido.cancelar();
            System.out.println("Pedido cancelado");
        }
        guardarSistema();
    }

    public synchronized void crearPedido(int id,Cliente c){

        if(existePedido(id)){
            throw new IllegalArgumentException("ID de pedido duplicado");
        }

        if(c == null){
            throw new IllegalArgumentException("Cliente inválido.");
        
        }

        pedidos.add(new Pedido(id, c));
        guardarSistema();
        System.out.println("Pedido creado en estado BORRADOR.");
    }

    public synchronized void listarProductos(){

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

    public synchronized void listarClientes(){
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

    private void guardarProductos()throws IOException{
        synchronized(fileLock){
            try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("Productos.dat"))){
                dos.write(productos.size());
                for(Producto p : productos){
                    dos.writeInt(p.getId());
                    dos.writeUTF(p.getNombre());
                    dos.writeDouble(p.getPrecio());
                    dos.writeInt(p.getStock());
                }
            }
        }
    }

    private void cargarProductos()throws IOException{
        synchronized(fileLock){
            try(DataInputStream dis = new DataInputStream(new FileInputStream("Productos.dat"))){
                int size = dis.readInt();
                for(int i = 0; i < size; i++){
                   int id = dis.readInt();
                   String nombre = dis.readUTF();
                   double precio = dis.readDouble();
                   int stock = dis.readInt();
                   productos.add(new Producto(id, nombre, precio, stock));
                }
            } catch(FileNotFoundException e){
            }
        }
    }

    private void guardarClientes() throws IOException{
        synchronized(fileLock){
            try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("Clientes.dat"))){
                dos.write(clientes.size());
                for(Cliente c : clientes){
                    if(c instanceof ClienteRegular){
                        dos.writeByte(0);
                        dos.writeInt(c.getId());
                        dos.writeUTF(c.getNombre());
                    }else if(c instanceof ClienteVip){
                        dos.writeByte(1);
                        dos.writeInt(c.getId());
                        dos.writeUTF(c.getNombre());
                        dos.writeDouble(((ClienteVip)c).getPorcentajeDescuento());
                    }
                }
            }
        }
    }

    private void cargarClientes() throws IOException{
        synchronized(fileLock){
            try(DataInputStream dis = new DataInputStream(new FileInputStream("Clientes.dat"))){
                int size = dis.readInt();
                for(int i = 0; i < size; i++){
                    byte tipo = dis.readByte();
                    int id = dis.readInt();
                    String nombre = dis.readUTF();
                    if(tipo == 0){
                        clientes.add(new ClienteRegular(id, nombre));
                    }else {
                        double descuento = dis.readDouble();
                        clientes.add(new ClienteVip(id, nombre, descuento));
                    }
                }
            } catch(FileNotFoundException e){
            }
        }
    }

     private void guardarPedidos() throws IOException{
        synchronized(fileLock){
            try(PrintWriter pw = new PrintWriter(new FileWriter("Pedidos.dat"))){
                for(Pedido p : pedidos){
                    pw.printf("%d|%d|%s|%s|%.2f%n",
                        p.getId(),
                        p.getCliente().getId(),
                        p.getEstado(),
                        p.getFechaFormatada().toString(),
                        p.calcularTotal()
                    );
                }
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter("detalles_pedido.txt"))){
                for(Pedido p : pedidos){
                    for(DetallePedido d : p.getDetalles()){
                        pw.printf("%d|%d|%d%n",
                            p.getId(),
                            d.getProducto().getId(),
                            d.getCantidad()
                        );
                    }
                }
            }
        }
    }

    private void cargarPedidos() throws IOException {
        synchronized(fileLock){
            Map<Integer, Cliente> mapClientes = new HashMap<>();
            for (Cliente c : clientes) {
                mapClientes.put(c.getId(), c);
            }

            Map<Integer, Producto> productosMap = new HashMap<>();
            for (Producto p : productos) {
                productosMap.put(p.getId(), p);
            }

            try (BufferedReader br = new BufferedReader(new FileReader("pedidos.txt"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split("\\|");
                    if (partes.length == 5) {
                        int idPedido = Integer.parseInt(partes[0]);
                        int idCliente = Integer.parseInt(partes[1]);
                        String fechaStr = partes[2];
                        String estadoStr = partes[3];
                        Cliente cliente = mapClientes.get(idCliente);
                        if (cliente == null) continue;
                        Pedido pedido = new Pedido(idPedido, cliente);
                        try {
                            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
                            pedido.setFecha(fecha);
                        } catch (ParseException e){
                        }
                        pedido.setEstado(EstadoPedido.valueOf(estadoStr));
                        pedidos.add(pedido);
                    }
                }
            }catch(FileNotFoundException e){
            }

            try (BufferedReader br = new BufferedReader(new FileReader("detalles_pedido.txt"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split("\\|");
                    if (partes.length == 3) {
                        int idPedido = Integer.parseInt(partes[0]);
                        int idProducto = Integer.parseInt(partes[1]);
                        int cantidad = Integer.parseInt(partes[2]);
                        Pedido pedido;
                        try {
                            pedido = buscarPedido(idPedido);
                        } catch (PedidoInvalidoException e) {
                            continue; 
                        }
                        if (pedido == null)
                            continue;
                        Producto producto = productosMap.get(idProducto);
                        if (producto == null)
                            continue;
                        pedido.agregarDetalle(new DetallePedido(producto, cantidad));
                    }
                }
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void guardarSistema(){
        try {
            guardarProductos();
            guardarClientes();
            guardarPedidos();
            System.out.println("El sistema se a guardado correctamente");
        } catch (Exception e) {
            System.out.println("Error al guardar el sistema: " + e.getMessage());
        }
    }

    public void cargarSistema() {
        productos.clear();
        clientes.clear();
        pedidos.clear();
        try {
            cargarProductos();
            cargarClientes();
            cargarPedidos();
            System.out.println("El sistema se a cargado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar el sistema: " + e.getMessage());
        }
    }

    public void generarReporte(){
        generarReportePeriodico();
    }

    private void generarReportePeriodico() {
        synchronized (fileLock) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("reporte_sistema.txt"))) {
                pw.println(">>> Reporte del Sistema <<<");
                pw.println("Fecha y hora: " + new Date());
                synchronized (this) {
                    pw.println("Total productos: " + productos.size());
                    pw.println("Total clientes: " + clientes.size());
                    int borrador = 0, confirmado = 0, cancelado = 0, procesado = 0;
                    double ingresos = 0;
                    for (Pedido p : pedidos) {
                        switch (p.getEstado()) {
                            case BORRADOR: borrador++; 
                            break;

                            case CONFIRMADO: confirmado++; 
                            break;

                            case CANCELADO: cancelado++; 
                            break;

                            case PROCESADO: procesado++; 
                            break;
                        }

                        if (p.getEstado() == EstadoPedido.CONFIRMADO || p.getEstado() == EstadoPedido.PROCESADO) {
                            ingresos += p.calcularTotal();
                        }
                    }

                    pw.println("Pedidos por estado:");
                    pw.println("  BORRADOR: " + borrador);
                    pw.println("  CONFIRMADO: " + confirmado);
                    pw.println("  PROCESADO: " + procesado);
                    pw.println("  CANCELADO: " + cancelado);
                    pw.println("Ingresos totales (confirmados+procesados): " + ingresos);
                    pw.println("Productos con stock bajo (<5):");

                    for (Producto prod : productos) {
                        if (prod.getStock() < 5) {
                            pw.println("  " + prod.getNombre() + " (ID: " + prod.getId() + ") stock: " + prod.getStock());
                        }
                    }
                }
                pw.println("===========================");
            } catch (IOException e) {
                System.out.println("Error al generar reporte: " + e.getMessage());
            }
        }
    }

    public synchronized int getPedidosEnProceso() {
        int count = 0;
        for (Pedido p : pedidos) {
            if (p.getEstado() == EstadoPedido.CONFIRMADO) {
                count++;
            }
        }
        return count;
    }

    private class ProcesadorPedidos implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<Pedido> confirmados = new ArrayList<>();
                    synchronized (SistemaDeGestionDePedidos.this) {
                        for (Pedido p : pedidos) {
                            if (p.getEstado() == EstadoPedido.CONFIRMADO) {
                                confirmados.add(p);
                            }
                        }
                    }
                    for (Pedido p : confirmados) {
                        Thread.sleep(2000 + (int) (Math.random() * 1000)); 
                        synchronized (SistemaDeGestionDePedidos.this) {
                            if (p.getEstado() == EstadoPedido.CONFIRMADO) {
                                p.procesar();
                                guardarSistema();
                                System.out.println("Pedido " + p.getId() + " procesado.");
                            }
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Error en procesador: " + e.getMessage());
                }
            }
        }
    }

    private class GeneradorReportes implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                    generarReportePeriodico();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}