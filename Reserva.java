public class Reserva {
    private Evento evento;
    private Salon salon;
    private String fechaReserva;
    private double costoTotal;
    private boolean confirmada;
    
    public Reserva(Evento evento, Salon salon, String fechaReserva) {
        this.evento = evento;
        this.salon = salon;
        this.fechaReserva = fechaReserva;
        this.confirmada = false;
        this.costoTotal = calcularCosto();
    }
    
    public double calcularCosto() {
        return evento.calcularDuracion() * salon.getCostoPorHora();
    }
    
    public void confirmar() {
        this.confirmada = true;
        evento.confirmarReserva();
    }
    
    public void cancelar() {
        this.confirmada = false;
        salon.liberar();
        evento.cancelarReserva();
    }
    
    public Evento getEvento() {
        return evento;
    }
    public Salon getSalon() {
        return salon;
    }
    public String getFechaReserva() {
        return fechaReserva;
    }
    public double getCostoTotal() {
        return costoTotal;
    }
    public boolean isConfirmada() {
        return confirmada;
    }
}