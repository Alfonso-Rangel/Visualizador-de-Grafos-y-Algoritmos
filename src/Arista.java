import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 * La clase Arista representa una arista en el grafo visualizado por la aplicación.
 * Se utiliza para dibujar una línea que conecta dos vértices.
 * @author Luis-Rangel
 * @version 1.0
 */
public class Arista extends JComponent {
    private final Vertice origen;
    private final Vertice destino;
    private int origenX;
    private int origenY;
    private int destX;
    private int destY;
    private final JLabel pesoLabel;

    /**
     * Obtiene la etiqueta de peso asociada a la arista.
     *
     * @return La etiqueta de peso de la arista.
     */
    public JLabel getPesoLabel() {
        return pesoLabel;
    }

    /**
     * Crea una instancia de la clase Arista que dibuja una línea que va desde el vértice origen hasta
     * el vértice destino y añade una etiqueta con su peso.
     *
     * @param origen El vértice de origen de la arista.
     * @param destino El vértice de destino de la arista.
     * @param peso El peso de la arista.
     */
    public Arista(Vertice origen, Vertice destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.origenX = (origen.getX() + origen.getWidth() / 2);
        this.origenY = (origen.getY() + origen.getHeight() / 2);
        this.destX = destino.getX() + destino.getWidth() / 2;
        this.destY = destino.getY() + destino.getHeight() / 2;
        setName("Arista <" + origen.getID() + " <-> " + destino.getID() + ">");
        setBounds(0, 0, MainFrame.getMainPanel().getWidth(), MainFrame.getMainPanel().getHeight());
        setOpaque(false);
        pesoLabel = new JLabel(String.valueOf(peso));
        pesoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        int labelX = (origenX + destX) / 2;
        int labelY = (origenY + destY) / 2;
        pesoLabel.setBounds(labelX, labelY, 30, 20);
        pesoLabel.setName("EdgeLabel <" + origen.getID() + " -> " + destino.getID() + ">");
        JPanel mainPanel = MainFrame.getMainPanel();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickX = e.getX();
                int clickY = e.getY();
                if (estaTocandoArista(clickX, clickY)) {
                    if (MainFrame.getModo().equals("Eliminar Vértice/Arista")) {
                        MainFrame.getGrafo().eliminaArista(Arista.this);
                        mainPanel.remove(Arista.this);
                        mainPanel.remove(pesoLabel);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                    }
                }
            }
        });
        mainPanel.add(pesoLabel);
    }

    /**
     * Verifica si el punto (x, y) está tocando la arista.
     *
     * @param x La coordenada x del punto.
     * @param y La coordenada y del punto.
     * @return true si el punto está tocando la arista, de lo contrario false.
     */
    private boolean estaTocandoArista(int x, int y) {
        int[] inicio = new int[2];
        int[] fin = new int[2];
        encuentraXY(inicio, fin);
        // Verifica si el punto está en los puntos de origen o destino:
        if ((x == inicio[0] && y == inicio[1]) || (x == fin[0] && y == fin[1])) {
            return false;
        }
        // Calcula la distancia más corta entre el punto dado y el segmento de línea:
        Line2D.Double linea = new Line2D.Double(inicio[0], inicio[1], fin[0], fin[1]);
        double distanciaAlPunto = linea.ptSegDist(x, y);
        // Puede ser difícil darle clic a la posición exacta, por eso se le añade un mayor "hitbox":
        int tolerancia = 10;
        return distanciaAlPunto <= tolerancia;
    }

    /**
     * Calcula las coordenadas de los puntos de inicio y fin de la arista.
     *
     * @param inicio Arreglo de dos elementos para almacenar las coordenadas del punto de inicio.
     * @param fin Arreglo de dos elementos para almacenar las coordenadas del punto de fin.
     */
    private void encuentraXY(int[] inicio, int[] fin) {
        inicio[0] = origenX;
        inicio[1] = origenY;
        fin[0] = destX;
        fin[1] = destY;

        int radio = Vertice.getvSize() / 2;
        int deltaX = fin[0] - inicio[0];
        int deltaY = fin[1] - inicio[1];
        double distancia = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        int offsetX = (int) (radio * deltaX / distancia);
        int offsetY = (int) (radio * deltaY / distancia);
        inicio[0] += offsetX;
        inicio[1] += offsetY;
        fin[0] -= offsetX;
        fin[1] -= offsetY;
    }

    /**
     * Actualiza la posición de la etiqueta de peso de la arista.
     */
    private void actualizaPosicionLabel() {
        if (pesoLabel != null) {
            int labelX = (origenX + destX) / 2;
            int labelY = (origenY + destY) / 2;
            pesoLabel.setLocation(labelX, labelY);
        }
    }

    /**
     * Establece la coordenada x del vértice de origen.
     *
     * @param origenX La coordenada x del vértice de origen.
     */
    public void setOrigenX(int origenX) {
        this.origenX = origenX;
        actualizaPosicionLabel();
    }

    /**
     * Establece la coordenada y del vértice de origen.
     *
     * @param origenY La coordenada y del vértice de origen.
     */
    public void setOrigenY(int origenY) {
        this.origenY = origenY;
        actualizaPosicionLabel();
    }

    /**
     * Establece la coordenada x del vértice de destino.
     *
     * @param destX La coordenada x del vértice de destino.
     */
    public void setDestX(int destX) {
        this.destX = destX;
        actualizaPosicionLabel();
    }

    /**
     * Establece la coordenada y del vértice de destino.
     *
     * @param destY La coordenada y del vértice de destino.
     */
    public void setDestY(int destY) {
        this.destY = destY;
        actualizaPosicionLabel();
    }

    /**
     * Obtiene el vértice de origen de la arista.
     *
     * @return El vértice de origen.
     */
    public Vertice getOrigen() {
        return origen;
    }

    /**
     * Obtiene el vértice de destino de la arista.
     *
     * @return El vértice de destino.
     */
    public Vertice getDestino() {
        return destino;
    }

    /**
     * Verifica si los IDs de los vértices de origen y destino son iguales a los IDs especificados.
     * De momento estoy trabajando con grafos no-dirigidos, por lo que la arista A -> B es igual que B -> A
     *
     * @param v1 El ID del vértice de origen a comparar.
     * @param v2 El ID del vértice de destino a comparar.
     * @return true si los IDs coinciden, de lo contrario false.
     */
    public boolean sonIguales(String v1, String v2) {
        if (v1.equals(origen.getID()) && v2.equals(destino.getID())) {
            return true;
        } else return v2.equals(origen.getID()) && v1.equals(destino.getID());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));

        int[] inicio = new int[2];
        int[] fin = new int[2];
        encuentraXY(inicio, fin);

        g2d.drawLine(inicio[0], inicio[1], fin[0], fin[1]);
        g2d.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        // Verificar si las coordenadas del clic están en la línea dibujada
        return estaTocandoArista(x, y);
    }
}