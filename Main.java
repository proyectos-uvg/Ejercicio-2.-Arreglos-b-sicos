import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel predeterminado");
        }
        
        mostrarInformacionSistema();

        ControladorReservas controlador = new ControladorReservas();
        InterfazUsuario vista = new InterfazUsuario();
        
        controlador.setVista(vista);
        vista.setControlador(controlador);
        
        controlador.iniciarSistema();
        
        SwingUtilities.invokeLater(() -> {
            vista.setVisible(true);
        });
        
        ejecutarCicloPrincipal(controlador);
    }
    
    private static void mostrarInformacionSistema() {
        System.out.println("SISTEMA DE RESERVAS DE SALONES");
        System.out.println("Centro de Eventos UVG");
        System.out.println("Iniciando sistema...");
        System.out.println("Cargando interfaz gráfica...");
        System.out.println("Sistema listo para usar.");
    }
    
    private static void ejecutarCicloPrincipal(ControladorReservas controlador) {
        System.out.println("Sistema activo - Interfaz gráfica cargada");
        System.out.println("Use la ventana gráfica para interactuar con el sistema");
        System.out.println("Para cerrar el sistema, use el botón 'Salir' o cierre la ventana");
    }
}