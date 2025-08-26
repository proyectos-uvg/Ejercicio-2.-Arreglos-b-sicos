import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingInterfaz extends JFrame {
    private SistemaReservas sistema;
    private Estadisticas estadisticas;
    private JTabbedPane pestanas;
    private DefaultTableModel modeloSalones;
    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloEspera;
    private JTable tablaSalones;
    private JTable tablaReservas;
    private JTable tablaEspera;
    
    public SwingInterfaz() {
        sistema = new SistemaReservas();
        estadisticas = new Estadisticas();
        cargarDatosPrueba();
        configurarVentana();
        crearComponentes();
        actualizarDatos();
        setVisible(true);
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Reservas de Salones - UVG");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void crearComponentes() {
        pestanas = new JTabbedPane();
        crearPestanaDashboard();
        crearPestanaSalones();
        crearPestanaEventos();
        crearPestanaReservas();
        crearPestanaEstadisticas();
        add(pestanas, BorderLayout.CENTER);
        
        JPanel barraEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraEstado.setBorder(BorderFactory.createEtchedBorder());
        barraEstado.add(new JLabel("Sistema de Reservas - Universidad del Valle de Guatemala"));
        add(barraEstado, BorderLayout.SOUTH);
    }
    
    private void crearPestanaDashboard() {
        JPanel panelDashboard = new JPanel(new BorderLayout());
        
        JPanel panelStats = new JPanel(new GridLayout(2, 2, 10, 10));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas del Sistema"));
        panelStats.setBackground(Color.WHITE);
        
        panelStats.add(crearPanelEstadistica("Total Salones", "6", Color.BLUE));
        panelStats.add(crearPanelEstadistica("Disponibles", "4", Color.GREEN));
        panelStats.add(crearPanelEstadistica("Reservas Activas", "2", Color.ORANGE));
        panelStats.add(crearPanelEstadistica("En Espera", "0", Color.RED));
        
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Sistema"));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        infoArea.setText(
            "SISTEMA DE RESERVAS DE SALONES\n" +
            "Universidad del Valle de Guatemala\n\n" +
            "Funcionalidades:\n" +
            "• Gestión completa de salones\n" +
            "• Asignación automática\n" +
            "• Lista de espera inteligente\n" +
            "• Estadísticas mensuales\n\n" +
            "Reglas de Negocio:\n" +
            "• Depósito del 30% requerido\n" +
            "• Reservas con 48h anticipación\n" +
            "• Capacidad máxima del 90%\n" +
            "• Salones grandes solo VIP"
        );
        
        JScrollPane scrollInfo = new JScrollPane(infoArea);
        panelInfo.add(scrollInfo, BorderLayout.CENTER);
        
        panelDashboard.add(panelStats, BorderLayout.NORTH);
        panelDashboard.add(panelInfo, BorderLayout.CENTER);
        pestanas.addTab("📊 Dashboard", panelDashboard);
    }
    
    private JPanel crearPanelEstadistica(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(color, 2));
        panel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel lblValor = new JLabel(valor, JLabel.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 24));
        lblValor.setForeground(color);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        return panel;
    }
    
    private void crearPestanaSalones() {
        JPanel panelSalones = new JPanel(new BorderLayout());
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Salón"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNumero = new JTextField(10);
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        JTextField txtCapacidad = new JTextField(10);
        JTextField txtCosto = new JTextField(10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNumero, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        panelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3;
        panelForm.add(cmbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Capacidad:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCapacidad, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        panelForm.add(new JLabel("Costo/Hora:"), gbc);
        gbc.gridx = 3;
        panelForm.add(txtCosto, gbc);
        
        JButton btnRegistrar = new JButton("Registrar Salón");
        btnRegistrar.setBackground(new Color(52, 152, 219));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numero = Integer.parseInt(txtNumero.getText());
                    String tipo = (String) cmbTipo.getSelectedItem();
                    int capacidad = Integer.parseInt(txtCapacidad.getText());
                    double costo = Double.parseDouble(txtCosto.getText());
                    
                    Salon nuevoSalon = new Salon(numero, tipo, capacidad, costo);
                    
                    if (sistema.registrarSalon(nuevoSalon)) {
                        JOptionPane.showMessageDialog(SwingInterfaz.this, 
                            "Salón registrado exitosamente", "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        txtNumero.setText("");
                        txtCapacidad.setText("");
                        txtCosto.setText("");
                        actualizarTablaSalones();
                    } else {
                        JOptionPane.showMessageDialog(SwingInterfaz.this, 
                            "No se pudo registrar el salón", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SwingInterfaz.this, 
                        "Valores numéricos inválidos", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelForm.add(btnRegistrar, gbc);
        
        String[] columnasSalones = {"Número", "Tipo", "Capacidad", "Costo/Hora", "Estado"};
        modeloSalones = new DefaultTableModel(columnasSalones, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaSalones = new JTable(modeloSalones);
        tablaSalones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollSalones = new JScrollPane(tablaSalones);
        scrollSalones.setBorder(BorderFactory.createTitledBorder("Salones Registrados"));
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnLiberar = new JButton("Liberar Salón");
        JButton btnReservar = new JButton("Reservar Salón");
        
        btnLiberar.addActionListener(e -> liberarSalonSeleccionado());
        btnReservar.addActionListener(e -> reservarSalonSeleccionado());
        
        panelBotones.add(btnLiberar);
        panelBotones.add(btnReservar);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        
        panelSalones.add(panelSuperior, BorderLayout.NORTH);
        panelSalones.add(scrollSalones, BorderLayout.CENTER);
        pestanas.addTab("🏛️ Salones", panelSalones);
    }
    
    private void crearPestanaEventos() {
        JPanel panelEventos = new JPanel(new BorderLayout());
        
        JPanel panelFormEvento = new JPanel(new GridBagLayout());
        panelFormEvento.setBorder(BorderFactory.createTitledBorder("Nueva Solicitud de Evento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNombre = new JTextField(15);
        JTextField txtEncargado = new JTextField(15);
        JComboBox<String> cmbTipoEvento = new JComboBox<>(new String[]{"VIP", "Corporativo", "Social", "Familiar"});
        JTextField txtFecha = new JTextField(10);
        JTextField txtHoraInicio = new JTextField(8);
        JTextField txtHoraFin = new JTextField(8);
        JTextField txtAsistentes = new JTextField(10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormEvento.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panelFormEvento.add(txtNombre, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormEvento.add(new JLabel("Encargado:"), gbc);
        gbc.gridx = 3;
        panelFormEvento.add(txtEncargado, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormEvento.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panelFormEvento.add(cmbTipoEvento, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        panelFormEvento.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 3;
        panelFormEvento.add(txtFecha, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormEvento.add(new JLabel("Hora Inicio:"), gbc);
        gbc.gridx = 1;
        panelFormEvento.add(txtHoraInicio, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        panelFormEvento.add(new JLabel("Hora Fin:"), gbc);
        gbc.gridx = 3;
        panelFormEvento.add(txtHoraFin, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormEvento.add(new JLabel("Asistentes:"), gbc);
        gbc.gridx = 1;
        panelFormEvento.add(txtAsistentes, gbc);
        
        JPanel panelBotonesEvento = new JPanel(new FlowLayout());
        JButton btnSolicitar = new JButton("Enviar Solicitud");
        JButton btnPagarDeposito = new JButton("Pagar Depósito");
        
        btnSolicitar.setBackground(new Color(46, 204, 113));
        btnSolicitar.setForeground(Color.WHITE);
        btnPagarDeposito.setBackground(new Color(230, 126, 34));
        btnPagarDeposito.setForeground(Color.WHITE);
        
        btnSolicitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText();
                    String encargado = txtEncargado.getText();
                    String tipo = (String) cmbTipoEvento.getSelectedItem();
                    String fecha = txtFecha.getText();
                    String horaInicio = txtHoraInicio.getText();
                    String horaFin = txtHoraFin.getText();
                    int asistentes = Integer.parseInt(txtAsistentes.getText());
                    
                    if (nombre.trim().isEmpty() || encargado.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(SwingInterfaz.this, 
                            "Complete todos los campos", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Evento nuevoEvento = new Evento(nombre, encargado, tipo, fecha, 
                                                   horaInicio, horaFin, asistentes);
                    
                    String resultado = sistema.asignarSalon(nuevoEvento);
                    
                    JOptionPane.showMessageDialog(SwingInterfaz.this, resultado, 
                        "Resultado", JOptionPane.INFORMATION_MESSAGE);
                    
                    txtNombre.setText("");
                    txtEncargado.setText("");
                    txtFecha.setText("");
                    txtHoraInicio.setText("");
                    txtHoraFin.setText("");
                    txtAsistentes.setText("");
                    actualizarDatos();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SwingInterfaz.this, 
                        "Número de asistentes inválido", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panelBotonesEvento.add(btnSolicitar);
        panelBotonesEvento.add(btnPagarDeposito);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormEvento.add(panelBotonesEvento, gbc);
        
        panelEventos.add(panelFormEvento, BorderLayout.NORTH);
        pestanas.addTab("🎉 Eventos", panelEventos);
    }
    
    private void crearPestanaReservas() {
        JPanel panelReservas = new JPanel(new BorderLayout());
        
        String[] columnasReservas = {"Evento", "Salón", "Fecha", "Horario", "Estado", "Costo"};
        modeloReservas = new DefaultTableModel(columnasReservas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaReservas = new JTable(modeloReservas);
        JScrollPane scrollReservas = new JScrollPane(tablaReservas);
        scrollReservas.setBorder(BorderFactory.createTitledBorder("Reservas Activas"));
        
        String[] columnasEspera = {"Evento", "Encargado", "Tipo", "Fecha", "Asistentes"};
        modeloEspera = new DefaultTableModel(columnasEspera, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEspera = new JTable(modeloEspera);
        JScrollPane scrollEspera = new JScrollPane(tablaEspera);
        scrollEspera.setBorder(BorderFactory.createTitledBorder("Lista de Espera"));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollReservas, scrollEspera);
        splitPane.setDividerLocation(300);
        
        panelReservas.add(splitPane, BorderLayout.CENTER);
        pestanas.addTab("📅 Reservas", panelReservas);
    }
    
    private void crearPestanaEstadisticas() {
        JPanel panelEstadisticas = new JPanel(new BorderLayout());
        
        JTextArea areaEstadisticas = new JTextArea();
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        JScrollPane scrollEstadisticas = new JScrollPane(areaEstadisticas);
        scrollEstadisticas.setBorder(BorderFactory.createTitledBorder("Estadísticas Mensuales"));
        
        JButton btnActualizarStats = new JButton("Actualizar Estadísticas");
        btnActualizarStats.addActionListener(e -> {
            String reporte = estadisticas.obtenerReporteEstadisticas();
            areaEstadisticas.setText(reporte);
        });
        
        JPanel panelBotonStats = new JPanel(new FlowLayout());
        panelBotonStats.add(btnActualizarStats);
        
        panelEstadisticas.add(scrollEstadisticas, BorderLayout.CENTER);
        panelEstadisticas.add(panelBotonStats, BorderLayout.SOUTH);
        
        areaEstadisticas.setText(estadisticas.obtenerReporteEstadisticas());
        pestanas.addTab("📈 Estadísticas", panelEstadisticas);
    }
    
    private void cargarDatosPrueba() {
        sistema.registrarSalon(new Salon(101, "Pequeño", 30, 150.0));
        sistema.registrarSalon(new Salon(102, "Pequeño", 25, 120.0));
        sistema.registrarSalon(new Salon(201, "Mediano", 60, 250.0));
        sistema.registrarSalon(new Salon(202, "Mediano", 80, 300.0));
        sistema.registrarSalon(new Salon(301, "Grande", 150, 500.0));
        sistema.registrarSalon(new Salon(302, "Grande", 200, 650.0));
        
        Evento evento1 = new Evento("Conferencia Tech", "Ana García", "Corporativo", 
                                   "28/08/2025", "09:00", "17:00", 45);
        evento1.realizarDeposito();
        sistema.asignarSalon(evento1);
        
        Evento evento2 = new Evento("Gala Anual", "Carlos López", "VIP", 
                                   "30/08/2025", "19:00", "23:00", 120);
        evento2.realizarDeposito();
        sistema.asignarSalon(evento2);
    }
    
    private void actualizarDatos() {
        actualizarTablaSalones();
        actualizarTablaReservas();
        actualizarTablaEspera();
    }
    
    private void actualizarTablaSalones() {
        modeloSalones.setRowCount(0);
        Salon[] salones = sistema.getSalones();
        
        for (Salon salon : salones) {
            Object[] fila = {
                salon.getNumero(),
                salon.getTipo(),
                salon.getCapacidadMaxima(),
                "Q" + salon.getCostoPorHora(),
                salon.verificarDisponibilidad() ? "Disponible" : "Ocupado"
            };
            modeloSalones.addRow(fila);
        }
    }
    
    private void actualizarTablaReservas() {
        modeloReservas.setRowCount(0);
        Reserva[] reservas = sistema.getReservas();
        
        for (Reserva reserva : reservas) {
            Object[] fila = {
                reserva.getEvento().getNombre(),
                "Salón " + reserva.getSalon().getNumero(),
                reserva.getEvento().getFecha(),
                reserva.getEvento().getHoraInicio() + "-" + reserva.getEvento().getHoraFin(),
                reserva.isConfirmada() ? "Confirmada" : "Pendiente",
                "Q" + String.format("%.2f", reserva.getCostoTotal())
            };
            modeloReservas.addRow(fila);
        }
    }
    
    private void actualizarTablaEspera() {
        modeloEspera.setRowCount(0);
        Evento[] eventosEspera = sistema.getListaEspera();
        
        for (Evento evento : eventosEspera) {
            Object[] fila = {
                evento.getNombre(),
                evento.getEncargado(),
                evento.getTipoEvento(),
                evento.getFecha(),
                evento.getNumeroAsistentes()
            };
            modeloEspera.addRow(fila);
        }
    }
    
    private void liberarSalonSeleccionado() {
        int filaSeleccionada = tablaSalones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int numeroSalon = (Integer) modeloSalones.getValueAt(filaSeleccionada, 0);
            String resultado = sistema.liberarSalon(numeroSalon);
            
            JOptionPane.showMessageDialog(this, resultado, "Liberar Salón", 
                JOptionPane.INFORMATION_MESSAGE);
            actualizarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un salón", 
                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void reservarSalonSeleccionado() {
        int filaSeleccionada = tablaSalones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            JOptionPane.showMessageDialog(this, "Use la pestaña de Eventos para crear solicitudes completas.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un salón", 
                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        
        SwingUtilities.invokeLater(() -> {
            new SwingInterfaz();
        });
    }
}