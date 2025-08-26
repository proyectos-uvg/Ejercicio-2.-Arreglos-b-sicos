public class Evento {
    private String nombre;
    private String encargado;
    private String tipoEvento;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private int numeroAsistentes;
    private String estado;
    private boolean depositoPagado;
    
    public Evento(String nombre, String encargado, String tipoEvento, 
                  String fecha, String horaInicio, String horaFin, int numeroAsistentes) {
        this.nombre = nombre;
        this.encargado = encargado;
        this.tipoEvento = tipoEvento;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.numeroAsistentes = numeroAsistentes;
        this.estado = "Pendiente";
        this.depositoPagado = false;
    }
    
    public int calcularDuracion() {
        try {
            String[] inicio = horaInicio.split(":");
            String[] fin = horaFin.split(":");
            int horasInicio = Integer.parseInt(inicio[0]);
            int horasFin = Integer.parseInt(fin[0]);
            return horasFin - horasInicio;
        } catch (Exception e) {
            return 1;
        }
    }
    
    public boolean verificarTipo(String tipo) {
        return tipoEvento.equals(tipo);
    }
    
    public void confirmarReserva() {
        this.estado = "Confirmada";
    }
    
    public void cancelarReserva() {
        this.estado = "Cancelada";
    }
    
    public void realizarDeposito() {
        this.depositoPagado = true;
    }

    public String getNombre() {
        return nombre;
    }
    public String getEncargado() {
        return encargado;
    }
    public String getTipoEvento() {
        return tipoEvento;
    }
    public String getFecha() {
        return fecha;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public String getHoraFin() {
        return horaFin;
    }
    public int getNumeroAsistentes() {
        return numeroAsistentes;
    }
    public String getEstado() {
        return estado;
    }
    public boolean isDepositoPagado() {
        return depositoPagado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setDepositoPagado(boolean depositoPagado) {
        this.depositoPagado = depositoPagado;
    }
}