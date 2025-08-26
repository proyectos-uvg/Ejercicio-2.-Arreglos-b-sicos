public class SistemaReservas {
    private Salon[] salones;
    private Evento[] eventos;
    private Reserva[] reservas;
    private Evento[] listaEspera;
    private int contadorSalones;
    private int contadorEventos;
    private int contadorReservas;
    private int contadorEspera;
    
    public SistemaReservas() {
        salones = new Salon[50];
        eventos = new Evento[200];
        reservas = new Reserva[200];
        listaEspera = new Evento[100];
        contadorSalones = 0;
        contadorEventos = 0;
        contadorReservas = 0;
        contadorEspera = 0;
    }
    
    public boolean registrarSalon(Salon salon) {
        if (contadorSalones < salones.length) {
            salones[contadorSalones] = salon;
            contadorSalones++;
            return true;
        }
        return false;
    }
    
    public boolean recibirSolicitud(Evento evento) {
        if (contadorEventos < eventos.length) {
            eventos[contadorEventos] = evento;
            evento.setEstado("Pendiente");
            contadorEventos++;
            return true;
        }
        return false;
    }

    public String pagarDepositoEvento(String nombreEvento) {
        for (int i = 0; i < contadorEventos; i++) {
            Evento evento = eventos[i];
            if (evento.getNombre().equalsIgnoreCase(nombreEvento.trim()) && 
                "Pendiente".equals(evento.getEstado()) && 
                !evento.isDepositoPagado()) {
                
                evento.realizarDeposito();
                return "Depósito del 30% registrado exitosamente para el evento: " + nombreEvento;
            }
        }
        return "No se encontró evento pendiente sin depósito con ese nombre";
    }
    
    public String asignarSalonAEvento() {
        Evento eventoParaAsignar = null;
        for (int i = 0; i < contadorEventos; i++) {
            Evento evento = eventos[i];
            if ("Pendiente".equals(evento.getEstado()) && evento.isDepositoPagado()) {
                eventoParaAsignar = evento;
                break;
            }
        }

        if (eventoParaAsignar == null) {
            return "No hay eventos pendientes con depósito pagado para asignar";
        }

        return procesarAsignacion(eventoParaAsignar);
    }

    public String asignarSalon(Evento evento) {
        if (!evento.isDepositoPagado()) {
            return "RECHAZADO: Debe realizar pago de depósito del 30% antes de asignar un salón.";
        }
        return procesarAsignacion(evento);
    }
    
    private String procesarAsignacion(Evento evento) {
        Salon salonAsignado = buscarSalonDisponible(evento);
        
        if (salonAsignado == null) {
            if (!estaEnListaEspera(evento)) {
                agregarAListaEspera(evento);
                return "No hay salones disponibles que cumplan los requisitos. Evento agregado a lista de espera.";
            } else {
                return "El evento ya está en lista de espera";
            }
        }
        
        String validacion = validarReglas(evento, salonAsignado);
        
        if (!validacion.equals("APROBADO")) {
            if (!estaEnListaEspera(evento)) {
                agregarAListaEspera(evento);
            }
            return validacion + " Evento agregado a lista de espera.";
        }
        
        salonAsignado.reservar();
        evento.confirmarReserva();
        
        Reserva nuevaReserva = new Reserva(evento, salonAsignado, "25/08/2025");
        nuevaReserva.confirmar();
        
        if (contadorReservas < reservas.length) {
            reservas[contadorReservas] = nuevaReserva;
            contadorReservas++;
        }
        
        return "ÉXITO: Salón " + salonAsignado.getNumero() + " (" + salonAsignado.getTipo() + 
               ") asignado al evento '" + evento.getNombre() + "'. Costo total: $" + 
               String.format("%.2f", nuevaReserva.getCostoTotal());
    }
    
    private boolean estaEnListaEspera(Evento evento) {
        for (int i = 0; i < contadorEspera; i++) {
            if (listaEspera[i] != null && 
                listaEspera[i].getNombre().equals(evento.getNombre()) &&
                listaEspera[i].getEncargado().equals(evento.getEncargado())) {
                return true;
            }
        }
        return false;
    }
    
    public String validarReglas(Evento evento, Salon salon) {
        if (!salon.verificarCapacidad(evento.getNumeroAsistentes())) {
            return "RECHAZADO: Capacidad excedida (máximo " + (int)(salon.getCapacidadMaxima() * 0.9) + " personas)";
        }

        if (!salon.validarCompatibilidadEvento(evento.getTipoEvento())) {
            return "RECHAZADO: Salón Grande solo para eventos VIP";
        }
        
        if (!evento.isDepositoPagado()) {
            return "PENDIENTE: Debe realizar pago de depósito del 30%";
        }
        
        return "APROBADO";
    }
    
    public boolean agregarAListaEspera(Evento evento) {
        if (estaEnListaEspera(evento)) {
            return false;
        }
        
        if (contadorEspera < listaEspera.length) {
            listaEspera[contadorEspera] = evento;
            evento.setEstado("En Espera");
            contadorEspera++;
            return true;
        }
        return false;
    }
    
    public Salon buscarSalonDisponible(Evento evento) {
        for (int i = 0; i < contadorSalones; i++) {
            if (salones[i].verificarDisponibilidad() && 
                salones[i].validarCompatibilidadEvento(evento.getTipoEvento()) &&
                salones[i].verificarCapacidad(evento.getNumeroAsistentes())) {
                return salones[i];
            }
        }
        return null;
    }
    
    public String liberarSalon(int numeroSalon) {
        for (int i = 0; i < contadorSalones; i++) {
            if (salones[i].getNumero() == numeroSalon && !salones[i].verificarDisponibilidad()) {
                salones[i].liberar();
                
                marcarReservaCancelada(numeroSalon);

                String procesamientoEspera = procesarListaEspera();
                
                return "Salón " + numeroSalon + " liberado exitosamente.\n" + procesamientoEspera;
            }
        }
        return "Salón no encontrado o ya está disponible";
    }
    
    private void marcarReservaCancelada(int numeroSalon) {
        for (int i = 0; i < contadorReservas; i++) {
            if (reservas[i] != null && 
                reservas[i].getSalon().getNumero() == numeroSalon && 
                reservas[i].isConfirmada()) {
                reservas[i].cancelar();
                break;
            }
        }
    }
    
    private String procesarListaEspera() {
        if (contadorEspera == 0) {
            return "Lista de espera vacía.";
        }
        
        for (int i = 0; i < contadorEspera; i++) {
            if (listaEspera[i] != null && listaEspera[i].isDepositoPagado()) {
                String resultado = procesarAsignacion(listaEspera[i]);
                if (resultado.contains("ÉXITO")) {
                    String nombreEvento = listaEspera[i].getNombre();
                    removerDeListaEspera(i);
                    return "Evento '" + nombreEvento + "' asignado automáticamente desde lista de espera.";
                }
            }
        }
        return "No se pudieron procesar eventos de la lista de espera (verificar depósitos).";
    }
    
    private void removerDeListaEspera(int indice) {
        for (int j = indice; j < contadorEspera - 1; j++) {
            listaEspera[j] = listaEspera[j + 1];
        }
        listaEspera[contadorEspera - 1] = null;
        contadorEspera--;
    }
    
    public String obtenerEventosPendientesSinDeposito() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EVENTOS PENDIENTES SIN DEPÓSITO ===\n\n");
        
        int contador = 0;
        for (int i = 0; i < contadorEventos; i++) {
            Evento evento = eventos[i];
            if ("Pendiente".equals(evento.getEstado()) && !evento.isDepositoPagado()) {
                contador++;
                sb.append(contador).append(". ").append(evento.getNombre())
                  .append(" - ").append(evento.getEncargado())
                  .append(" - ").append(evento.getTipoEvento())
                  .append(" - ").append(evento.getNumeroAsistentes()).append(" asistentes\n");
            }
        }
        
        if (contador == 0) {
            sb.append("No hay eventos pendientes sin depósito\n");
        } else {
            sb.append("\nTotal eventos pendientes: ").append(contador);
            sb.append("\nPara asignar salones, primero deben pagar el depósito del 30%");
        }
        
        return sb.toString();
    }
    
    public Salon[] getSalones() { 
        return salones; 
    }
    public Evento[] getEventos() { 
        return eventos; 
    }
    public Reserva[] getReservas() { 
        return reservas; 
    }
    public Evento[] getListaEspera() { 
        return listaEspera; 
    }
    public int getContadorSalones() { 
        return contadorSalones; 
    }
    public int getContadorEventos() { 
        return contadorEventos; 
    }
    public int getContadorReservas() { 
        return contadorReservas; 
    }
    public int getContadorEspera() { 
        return contadorEspera; 
    }
}