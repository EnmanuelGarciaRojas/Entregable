package Controller;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SistemaDeGestionDePedidos sistema = new SistemaDeGestionDePedidos(50, 50, 50);

        int option;

        do {
            System.out.println("1. Registrar producto");
            System.out.println("2. Registrar cliente");
            System.out.println("3. Crear pedido");
            System.out.println("4. Agregar producto a pedido");
            System.out.println("5. Ver detalle de pedido");
            System.out.println("6. Listar productos");
            System.out.println("7. Listar pedidos");
            System.out.println("8. Cambiar estado de pedido");
            System.out.println("0. Salir");

            System.out.print("Seleccione una opcion: ");

            option = sc.nextInt();

            try {

                switch (option) {
                    case 1:
                        System.out.print("ID: ");
                        int id = sc.nextInt();

                        System.out.print("Nombre: ");
                        String nombre = sc.next();

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

                        System.out.print("Nombre: ");
                        String nomc = sc.next();

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

                        Cliente c = sistema.buscarCliente(idcl);

                        sistema.crearPedido(idp, c);
                        break;

                    case 4:
                        System.out.print("ID Pedido: ");
                        Pedido ped = sistema.buscarPedido(sc.nextInt());

                        System.out.print("ID Producto: ");
                        Producto pr = sistema.buscarProducto(sc.nextInt());

                        System.out.print("Cantidad: ");
                        int cant = sc.nextInt();

                        ped.agregarProducto(pr, cant);
                        break;

                    case 5:
                        System.out.print("ID Pedido: ");
                        int idd = sc.nextInt();

                        Pedido pdetalle = sistema.buscarPedido(idd);

                        if (pdetalle == null) {
                            System.out.println("Pedido no encontrado.");
                            break;
                        }

                        pdetalle.mostrarDetalle();
                        break;

                    case 6:
                        sistema.listarProductos();
                        break;

                    case 7:
                        sistema.listarPedidos();
                        break;

                    case 8:
                        System.out.print("ID Pedido: ");
                        Pedido p = sistema.buscarPedido(sc.nextInt());

                        System.out.print("1 Confirmar / 2 Cancelar: ");
                        int est = sc.nextInt();

                        if (est == 1)
                            p.confirmar();
                        else
                            p.cancelar();
                        break;

                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (option != 0);
    }
}
