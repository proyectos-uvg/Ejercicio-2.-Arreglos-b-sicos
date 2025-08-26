public class Estadisticas {
    private int[] eventosPorMes;
    private double[] ingresosPorMes;
    private String[] nombresMeses;
    
    public Estadisticas() {
        eventosPorMes = new int[12];
        ingresosPorMes = new double[12];
        nombresMeses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                                   "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    }
    
    public void registrarEventoCompletado(int mes, double costo) {
        if (mes >= 0 && mes < 12) {
            eventosPorMes[mes]++;
            ingresosPorMes[mes] += costo;
        }
    }
    
    public String obtenerReporteEstadisticas() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE ESTADÍSTICAS MENSUALES ===\n\n");
        
        for (int i = 0; i < 12; i++) {
            reporte.append(String.format("%-12s: %d eventos - $%.2f\n", 
                          nombresMeses[i], eventosPorMes[i], ingresosPorMes[i]));
        }
        
        reporte.append(String.format("\nTOTAL AÑO: %d eventos - $%.2f", 
                      obtenerTotalEventos(), getTotalIngresos()));
        reporte.append(String.format("\nMes con más eventos: %s", 
                      nombresMeses[obtenerMesMasEventos()]));
        
        return reporte.toString();
    }
    
    public int obtenerMesMasEventos() {
        int maxEventos = 0;
        int mesMax = 0;
        for (int i = 0; i < 12; i++) {
            if (eventosPorMes[i] > maxEventos) {
                maxEventos = eventosPorMes[i];
                mesMax = i;
            }
        }
        return mesMax;
    }
    
    public double getTotalIngresos() {
        double total = 0;
        for (double ingreso : ingresosPorMes) {
            total += ingreso;
        }
        return total;
    }
    
    private int obtenerTotalEventos() {
        int total = 0;
        for (int eventos : eventosPorMes) {
            total += eventos;
        }
        return total;
    }
}