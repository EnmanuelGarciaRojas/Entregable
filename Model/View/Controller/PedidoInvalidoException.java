package Controller;

public class PedidoInvalidoException extends Exception{
    public PedidoInvalidoException(String mensaje){
        super(mensaje);
    }
}
