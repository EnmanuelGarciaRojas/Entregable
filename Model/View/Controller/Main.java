package Controller;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SistemaDeGestionDePedidos sistema = new SistemaDeGestionDePedidos();

        sistema.iniciarHilos();
        int option;

        do {
            System.out.println("1. Registrar producto");
            System.out.println("2. Registrar cliente");
            System.out.println("3. Crear pedido");
            System.out.println("4. Agregar producto a pedido");
            System.out.println("5. Ver detalle de pedido");
            System.out.println("6. Listar productos");
            System.out.println("7. Listar pedidos");
            System.out.println("8. Guardar sistema");
            System.out.println("9. Cargar sistema desde archivos");
            System.out.println("10. Generar reporte del sistema");
            System.out.println("11. ver estado de procesamiento de pedido");
            System.out.println("12. Listar clientes");
            System.out.println("13. Cambiar estado de pedido");
            System.out.println("14. Buscar producto por nombre");
            System.out.println("0. Salir");

            System.out.print("Seleccione una opcion: ");

            option = sc.nextInt();

            try {

                switch (option) {
                    case 1:
                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Precio: ");
                        double precio = sc.nextDouble();

                        System.out.print("Stock: ");
                        int stock = sc.nextInt();

                        sistema.registrarProducto(new Producto(id, nombre, precio, stock));
                        break;

                    case 2:
                        System.out.print("Tipo cliente (1 Regular / 2 VIP): ");
                        int tipo = sc.nextInt();

                        System.out.print("ID: ");
                        int idc = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nombre: ");
                        String nomc = sc.nextLine();

                        if (tipo == 1)
                            sistema.registrarCliente(new ClienteRegular(idc, nomc));
                        else
                            sistema.registrarCliente(new ClienteVip(idc, nomc, 0.1));
                        break;

                    case 3:
                        System.out.print("ID Pedido: ");
                        int idp = sc.nextInt();

                        System.out.print("ID Cliente: ");
                        int idcl = sc.nextInt();

                        Cliente cliente = sistema.buscarCliente(idcl);
                        sistema.crearPedido(idp, cliente);
                        break;

                    case 4:
                        System.out.print("ID Pedido: ");
                        int ped = sc.nextInt();

                        System.out.print("ID Producto: ");
                        int pr = sc.nextInt();

                        System.out.print("Cantidad: ");
                        int cant = sc.nextInt();

                        sistema.agregarProductoAPedido(ped, pr, cant);
                        break;

                    case 5:
                        System.out.print("ID Pedido: ");
                        int idd = sc.nextInt();

                        Pedido pdetalle = sistema.buscarPedido(idd);

                        pdetalle.mostrarDetalle();
                        break;

                    case 6:
                        sistema.listarProductos();
                        break;

                    case 7:
                        sistema.listarPedidos();
                        break;

                    case 8:
                        sistema.guardarSistema();
                        break;

                    case 9:
                        sistema.cargarSistema();
                        break;

                    case 10:
                        sistema.generarReporte();
                        System.out.println("Reporte generado");
                        break;

                    case 11:
                        System.out.println("Pedidos en proceso (confirmados): " + sistema.getPedidosEnProceso());
                        break;

                    case 12:
                        sistema.listarClientes();
                        break;

                    case 13:
                        System.out.print("ID Pedido: ");
                        int p = sc.nextInt();
                        System.out.print("1 Confirmar / 2 Cancelar: ");
                        int est = sc.nextInt();

                        sistema.cambiarEstadoPedido(p, est );
                        break;

                    case 14:
                        sc.nextLine();

                        System.out.print("Nombre del producto: ");
                        String nombreBusqueda = sc.nextLine();

                        sistema.buscarProductoPorNombre(nombreBusqueda);
                        break;

                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Ingrese un valor entre 0 y 10");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (option != 0);
    }
}
