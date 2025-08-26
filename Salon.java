public class Salon {
    private int numero;
    private String tipo;
    private int capacidadMaxima;
    private double costoPorHora;
    private boolean disponible;
    
    public Salon(int numero, String tipo, int capacidadMaxima, double costoPorHora) {
        this.numero = numero;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.costoPorHora = costoPorHora;
        this.disponible = true;
    }
    
    public boolean verificarDisponibilidad() {
        return disponible;
    }
    
    public void reservar() {
        this.disponible = false;
    }
    
    public void liberar() {
        this.disponible = true;
    }
    
    public boolean verificarCapacidad(int personas) {
        return personas <= (capacidadMaxima * 0.9);
    }
    
    public boolean validarCompatibilidadEvento(String tipoEvento) {
        if ("Grande".equals(tipo)) {
            return "VIP".equals(tipoEvento);
        }
        return true;
    }
    
    public int getNumero() { 
        return numero; 
    }
    public String getTipo() { 
        return tipo; 
    }
    public int getCapacidadMaxima() { 
        return capacidadMaxima; 
    }
    public double getCostoPorHora() { 
        return costoPorHora; 
    }
    public boolean isDisponible() { 
        return disponible; 
    }
    public void setDisponible(boolean disponible) { 
        this.disponible = disponible; 
    }
}