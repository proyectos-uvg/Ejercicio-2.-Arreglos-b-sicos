import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class InterfazUsuario extends JFrame {
    private ControladorReservas controlador;
    private JPanel panelPrincipal;
    private JTextArea areaResultados;
    
    public InterfazUsuario() {
        configurarVentana();
        inicializarComponentes();
    }
    
    public void setControlador(ControladorReservas controlador) {
        this.controlador = controlador;
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Reservas de Salones - Centro de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void inicializarComponentes() {
        JPanel panelBotones = new JPanel(new GridLayout(3, 4, 5, 5));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Opciones del Sistema"));

        JButton btnRegistrarSalon = new JButton("Registrar Salón");
        JButton btnSolicitudReserva = new JButton("Solicitud de Reserva");
        JButton btnAsignarSalon = new JButton("Asignar Salón");
        JButton btnPagarDeposito = new JButton("Pagar Depósito");
        JButton btnConsultarDisponibilidad = new JButton("Consultar Disponibilidad");
        JButton btnListaEspera = new JButton("Ver Lista de Espera");
        JButton btnEventosPendientes = new JButton("Eventos Pendientes");
        JButton btnEstadisticas = new JButton("Ver Estadísticas");
        JButton btnLiberarSalon = new JButton("Liberar Salón");
        JButton btnReservasActivas = new JButton("Reservas Activas");
        JButton btnLimpiarPantalla = new JButton("Limpiar Pantalla");
        JButton btnSalir = new JButton("Salir");
        
        btnRegistrarSalon.addActionListener(e -> mostrarFormularioSalon());
        btnSolicitudReserva.addActionListener(e -> mostrarFormularioEvento());
        btnAsignarSalon.addActionListener(e -> procesarAsignacionSalon());
        btnPagarDeposito.addActionListener(e -> mostrarFormularioPagoDeposito());
        btnConsultarDisponibilidad.addActionListener(e -> mostrarDisponibilidad());
        btnListaEspera.addActionListener(e -> mostrarListaEspera());
        btnEventosPendientes.addActionListener(e -> mostrarEventosPendientes());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        btnLiberarSalon.addActionListener(e -> liberarSalon());
        btnReservasActivas.addActionListener(e -> mostrarReservasActivas());
        btnLimpiarPantalla.addActionListener(e -> limpiarPantalla());
        btnSalir.addActionListener(e -> System.exit(0));
        
        panelBotones.add(btnRegistrarSalon);
        panelBotones.add(btnSolicitudReserva);
        panelBotones.add(btnAsignarSalon);
        panelBotones.add(btnPagarDeposito);
        panelBotones.add(btnConsultarDisponibilidad);
        panelBotones.add(btnListaEspera);
        panelBotones.add(btnEventosPendientes);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnLiberarSalon);
        panelBotones.add(btnReservasActivas);
        panelBotones.add(btnLimpiarPantalla);
        panelBotones.add(btnSalir);
        
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        areaResultados.setText("=== SISTEMA DE RESERVAS DE SALONES ===\n" +
                             "Centro de Eventos UVG\n" +
                             "Flujo recomendado:\n" +
                             "1. Registrar salones\n" +
                             "2. Crear solicitudes de reserva\n" +
                             "3. Pagar depósitos pendientes\n" +
                             "4. Asignar salones\n" +
                             "5. Consultar reservas activas\n\n" +
                             "Seleccione una opción del menú superior\n\n");
    }
    
    private void mostrarFormularioSalon() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        JTextField txtNumero = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        JTextField txtCapacidad = new JTextField();
        JTextField txtCosto = new JTextField();
        
        panel.add(new JLabel("Número de Salón:"));
        panel.add(txtNumero);
        panel.add(new JLabel("Tipo de Salón:"));
        panel.add(cbTipo);
        panel.add(new JLabel("Capacidad Máxima:"));
        panel.add(txtCapacidad);
        panel.add(new JLabel("Costo por Hora:"));
        panel.add(txtCosto);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Nuevo Salón", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int numero = Integer.parseInt(txtNumero.getText());
                String tipo = (String) cbTipo.getSelectedItem();
                int capacidad = Integer.parseInt(txtCapacidad.getText());
                double costo = Double.parseDouble(txtCosto.getText());
                
                String[] resultado = controlador.procesarRegistroSalon(numero, tipo, capacidad, costo);
                mostrarResultado(resultado);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese valores numéricos válidos");
            }
        }
    }
    
    private void mostrarFormularioEvento() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        
        JTextField txtNombre = new JTextField();
        JTextField txtEncargado = new JTextField();
        JComboBox<String> cbTipoEvento = new JComboBox<>(new String[]{"VIP", "Corporativo", "Social", "Familiar"});
        JTextField txtFecha = new JTextField("dd/mm/yyyy");
        JTextField txtHoraInicio = new JTextField("HH:MM");
        JTextField txtHoraFin = new JTextField("HH:MM");
        JTextField txtAsistentes = new JTextField();
        JCheckBox chkDeposito = new JCheckBox("Pagar Depósito del 30% ahora");
        
        panel.add(new JLabel("Nombre del Evento:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Encargado:"));
        panel.add(txtEncargado);
        panel.add(new JLabel("Tipo de Evento:"));
        panel.add(cbTipoEvento);
        panel.add(new JLabel("Fecha (dd/mm/yyyy):"));
        panel.add(txtFecha);
        panel.add(new JLabel("Hora Inicio (HH:MM):"));
        panel.add(txtHoraInicio);
        panel.add(new JLabel("Hora Fin (HH:MM):"));
        panel.add(txtHoraFin);
        panel.add(new JLabel("Número de Asistentes:"));
        panel.add(txtAsistentes);
        panel.add(chkDeposito);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Solicitud de Reserva", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                String encargado = txtEncargado.getText().trim();
                String tipoEvento = (String) cbTipoEvento.getSelectedItem();
                String fecha = txtFecha.getText().trim();
                String horaInicio = txtHoraInicio.getText().trim();
                String horaFin = txtHoraFin.getText().trim();
                int asistentes = Integer.parseInt(txtAsistentes.getText().trim());
                boolean deposito = chkDeposito.isSelected();
                
                if (nombre.isEmpty() || encargado.isEmpty()) {
                    mostrarMensaje("Error: Nombre del evento y encargado son obligatorios");
                    return;
                }
                
                String[] resultado = controlador.procesarSolicitudReserva(nombre, encargado, tipoEvento, 
                                                                        fecha, horaInicio, horaFin, asistentes, deposito);
                mostrarResultado(resultado);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese un número válido de asistentes");
            }
        }
    }
    
    private void mostrarFormularioPagoDeposito() {
        String nombreEvento = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre exacto del evento para pagar el depósito:",
            "Pago de Depósito",
            JOptionPane.QUESTION_MESSAGE);
            
        if (nombreEvento != null && !nombreEvento.trim().isEmpty()) {
            String[] resultado = controlador.pagarDeposito(nombreEvento.trim());
            mostrarResultado(resultado);
        }
    }
    
    private void procesarAsignacionSalon() {
        String[] resultado = controlador.procesarAsignacionSalon();
        mostrarResultado(resultado);
    }
    
    private void mostrarDisponibilidad() {
        String[] resultado = controlador.procesarConsultaDisponibilidad();
        mostrarResultado(resultado);
    }
    
    private void mostrarListaEspera() {
        String[] resultado = controlador.mostrarListaEspera();
        mostrarResultado(resultado);
    }
    
    private void mostrarEventosPendientes() {
        String[] resultado = controlador.mostrarEventosPendientes();
        mostrarResultado(resultado);
    }
    
    private void mostrarEstadisticas() {
        String[] resultado = controlador.mostrarEstadisticas();
        mostrarResultado(resultado);
    }
    
    private void liberarSalon() {
        String numeroStr = JOptionPane.showInputDialog(this, "Ingrese el número del salón a liberar:");
        if (numeroStr != null && !numeroStr.trim().isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                String[] resultado = controlador.liberarSalon(numero);
                mostrarResultado(resultado);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese un número válido");
            }
        }
    }
    
    private void mostrarReservasActivas() {
        String[] resultado = controlador.mostrarReservasActivas();
        mostrarResultado(resultado);
    }

    private void limpiarPantalla() {
        areaResultados.setText("Pantalla limpiada. Seleccione una opción del menú.\n\n");
    }
    
    private void mostrarResultado(String[] resultado) {
        StringBuilder sb = new StringBuilder();
        for (String linea : resultado) {
            if (linea != null) {
                sb.append(linea).append("\n");
            }
        }
        areaResultados.setText(sb.toString());
        areaResultados.setCaretPosition(0);
    }
    
    private void mostrarMensaje(String mensaje) {
        areaResultados.setText(mensaje + "\n");
    }
}