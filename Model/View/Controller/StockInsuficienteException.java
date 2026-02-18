package Controller;

public class StockInsuficienteException extends Exception{
    public StockInsuficienteException(String mensaje){
        super(mensaje);
    }
}