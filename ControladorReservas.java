public class ControladorReservas {
    private SistemaReservas modelo;
    private InterfazUsuario vista;
    private Estadisticas estadisticas;
    private boolean sistemaActivo;
    
    public ControladorReservas() {
        modelo = new SistemaReservas();
        estadisticas = new Estadisticas();
        sistemaActivo = true;
    }
    
    public void setVista(InterfazUsuario vista) {
        this.vista = vista;
    }
    
    public void iniciarSistema() {
        modelo.registrarSalon(new Salon(101, "Pequeño", 50, 100.0));
        modelo.registrarSalon(new Salon(102, "Mediano", 100, 200.0));
        modelo.registrarSalon(new Salon(103, "Grande", 200, 400.0));
        modelo.registrarSalon(new Salon(104, "Pequeño", 30, 80.0));
        
        estadisticas.registrarEventoCompletado(0, 500.0);
        estadisticas.registrarEventoCompletado(1, 750.0);
        estadisticas.registrarEventoCompletado(7, 1200.0);
    }
    
    public String[] procesarRegistroSalon(int numero, String tipo, int capacidad, double costo) {
        Salon nuevoSalon = new Salon(numero, tipo, capacidad, costo);
        boolean exito = modelo.registrarSalon(nuevoSalon);
        
        if (exito) {
            return new String[]{
                "=== REGISTRO EXITOSO ===",
                "Salón registrado correctamente:",
                "Número: " + numero,
                "Tipo: " + tipo,
                "Capacidad: " + capacidad + " personas",
                "Costo por hora: $" + costo,
                "Estado: Disponible",
                "",
                "Total de salones registrados: " + modelo.getContadorSalones()
            };
        } else {
            return new String[]{"Error: No se pudo registrar el salón. Capacidad máxima alcanzada."};
        }
    }
    
    public String[] procesarSolicitudReserva(String nombre, String encargado, String tipoEvento,
                                           String fecha, String horaInicio, String horaFin,
                                           int asistentes, boolean deposito) {
        Evento nuevoEvento = new Evento(nombre, encargado, tipoEvento, fecha, horaInicio, horaFin, asistentes);
        if (deposito) {
            nuevoEvento.realizarDeposito();
        }
        
        boolean exito = modelo.recibirSolicitud(nuevoEvento);
        
        if (exito) {
            return new String[]{
                "=== SOLICITUD RECIBIDA ===",
                "Evento: " + nombre,
                "Encargado: " + encargado,
                "Tipo: " + tipoEvento,
                "Fecha: " + fecha,
                "Horario: " + horaInicio + " - " + horaFin,
                "Asistentes: " + asistentes,
                "Depósito: " + (deposito ? "Pagado" : "PENDIENTE - Use 'Pagar Depósito'"),
                "Estado: Pendiente de asignación",
                "",
                "SIGUIENTE PASO:",
                deposito ? "- Use 'Asignar Salón' para procesar la asignación" : 
                          "- Use 'Pagar Depósito' y luego 'Asignar Salón'",
                "",
                "Total de solicitudes: " + modelo.getContadorEventos()
            };
        } else {
            return new String[]{"Error: No se pudo procesar la solicitud"};
        }
    }

    public String[] pagarDeposito(String nombreEvento) {
        String resultado = modelo.pagarDepositoEvento(nombreEvento);
        
        return new String[]{
            "=== PAGO DE DEPÓSITO ===",
            resultado,
            "",
            "SIGUIENTE PASO:",
            "- Use 'Asignar Salón' para procesar la asignación",
            "",
            "Estado actual:",
            "Solicitudes totales: " + modelo.getContadorEventos(),
            "Reservas confirmadas: " + modelo.getContadorReservas()
        };
    }
    
    public String[] procesarAsignacionSalon() {
        String resultado = modelo.asignarSalonAEvento();
        
        return new String[]{
            "=== PROCESANDO ASIGNACIÓN ===",
            resultado,
            "",
            "Estado actual:",
            "Reservas confirmadas: " + modelo.getContadorReservas(),
            "En lista de espera: " + modelo.getContadorEspera(),
            "",
            "NOTA: Solo se procesan eventos con depósito pagado"
        };
    }

    public String[] mostrarEventosPendientes() {
        String resultado = modelo.obtenerEventosPendientesSinDeposito();
        return new String[]{resultado};
    }
    
    public String[] procesarConsultaDisponibilidad() {
        StringBuilder disponibles = new StringBuilder();
        StringBuilder ocupados = new StringBuilder();
        
        disponibles.append("=== SALONES DISPONIBLES ===\n");
        ocupados.append("\n=== SALONES OCUPADOS ===\n");
        
        int contDisponibles = 0;
        int contOcupados = 0;
        
        for (int i = 0; i < modelo.getContadorSalones(); i++) {
            Salon salon = modelo.getSalones()[i];
            String info = String.format("Salón %d - %s - Cap: %d - $%.2f/hora\n",
                                      salon.getNumero(), salon.getTipo(), 
                                      salon.getCapacidadMaxima(), salon.getCostoPorHora());
            
            if (salon.verificarDisponibilidad()) {
                disponibles.append(info);
                contDisponibles++;
            } else {
                ocupados.append(info);
                contOcupados++;
            }
        }
        
        if (contDisponibles == 0) {
            disponibles.append("No hay salones disponibles\n");
        }
        if (contOcupados == 0) {
            ocupados.append("No hay salones ocupados\n");
        }
        
        return new String[]{
            disponibles.toString() + ocupados.toString(),
            "",
            "RESUMEN:",
            "Total salones: " + modelo.getContadorSalones(),
            "Disponibles: " + contDisponibles,
            "Ocupados: " + contOcupados
        };
    }
    
    public String[] mostrarListaEspera() {
        if (modelo.getContadorEspera() == 0) {
            return new String[]{
                "=== LISTA DE ESPERA ===",
                "No hay eventos en lista de espera",
                "",
                "Los eventos se agregan a lista de espera cuando:",
                "- No hay salones disponibles que cumplan requisitos",
                "- No cumplen las reglas de capacidad o compatibilidad"
            };
        }
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== LISTA DE ESPERA ===\n\n");
        
        for (int i = 0; i < modelo.getContadorEspera(); i++) {
            if (modelo.getListaEspera()[i] != null) {
                Evento evento = modelo.getListaEspera()[i];
                resultado.append(String.format("%d. %s - %s - %s - %d asistentes - Depósito: %s\n",
                                              (i + 1), 
                                              evento.getNombre(), 
                                              evento.getEncargado(),
                                              evento.getTipoEvento(), 
                                              evento.getNumeroAsistentes(),
                                              evento.isDepositoPagado() ? "Pagado" : "Pendiente"));
            }
        }
        
        resultado.append("\nTotal en espera: ").append(modelo.getContadorEspera());
        resultado.append("\n\nNOTA: Eventos con depósito pagado se procesarán automáticamente");
        resultado.append("\ncuando se libere un salón apropiado.");
        
        return new String[]{resultado.toString()};
    }
    
    public String[] mostrarEstadisticas() {
        return new String[]{estadisticas.obtenerReporteEstadisticas()};
    }
    
    public String[] liberarSalon(int numeroSalon) {
        String resultado = modelo.liberarSalon(numeroSalon);
        
        return new String[]{
            "=== LIBERACIÓN DE SALÓN ===",
            resultado,
            "",
            "Estado actual:",
            "Reservas activas: " + modelo.getContadorReservas(),
            "Eventos en espera: " + modelo.getContadorEspera()
        };
    }
    
    public String[] mostrarReservasActivas() {
        if (modelo.getContadorReservas() == 0) {
            return new String[]{
                "=== RESERVAS ACTIVAS ===",
                "No hay reservas confirmadas",
                "",
                "Para crear reservas:",
                "1. Registre solicitudes de eventos",
                "2. Pague los depósitos",
                "3. Use 'Asignar Salón'"
            };
        }
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== RESERVAS ACTIVAS ===\n\n");
        
        double totalIngresos = 0;
        int reservasActivas = 0;
        
        for (int i = 0; i < modelo.getContadorReservas(); i++) {
            Reserva reserva = modelo.getReservas()[i];
            if (reserva != null && reserva.isConfirmada()) {
                reservasActivas++;
                resultado.append("Reserva ").append(reservasActivas).append(":\n");
                resultado.append("  Evento: ").append(reserva.getEvento().getNombre()).append("\n");
                resultado.append("  Encargado: ").append(reserva.getEvento().getEncargado()).append("\n");
                resultado.append("  Tipo: ").append(reserva.getEvento().getTipoEvento()).append("\n");
                resultado.append("  Fecha: ").append(reserva.getEvento().getFecha()).append("\n");
                resultado.append("  Horario: ").append(reserva.getEvento().getHoraInicio())
                         .append(" - ").append(reserva.getEvento().getHoraFin()).append("\n");
                resultado.append("  Asistentes: ").append(reserva.getEvento().getNumeroAsistentes()).append("\n");
                resultado.append("  Salón: ").append(reserva.getSalon().getNumero())
                         .append(" (").append(reserva.getSalon().getTipo()).append(")\n");
                resultado.append("  Costo: $").append(String.format("%.2f", reserva.getCostoTotal())).append("\n");
                resultado.append("  Estado: ").append(reserva.getEvento().getEstado()).append("\n\n");
                totalIngresos += reserva.getCostoTotal();
            }
        }
        
        resultado.append("=== RESUMEN FINANCIERO ===\n");
        resultado.append("Reservas confirmadas: ").append(reservasActivas).append("\n");
        resultado.append("Total ingresos proyectados: $").append(String.format("%.2f", totalIngresos)).append("\n");
        resultado.append("Promedio por evento: $").append(reservasActivas > 0 ? 
                        String.format("%.2f", totalIngresos/reservasActivas) : "0.00");
        
        return new String[]{resultado.toString()};
    }
    
    public String[] ejecutarAccion(int opcion) {
        switch (opcion) {
            case 1: return new String[]{"Seleccione 'Registrar Salón' del menú"};
            case 2: return new String[]{"Seleccione 'Solicitud de Reserva' del menú"};
            case 3: return procesarAsignacionSalon();
            case 4: return procesarConsultaDisponibilidad();
            case 5: return mostrarListaEspera();
            case 6: return mostrarEstadisticas();
            default: return new String[]{"Opción no válida"};
        }
    }
}