import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 * La clase representa una arista en el grafo para ser visualizado por la aplicación.
 * Se utiliza para dibujar una línea que conecta dos vértices.
 * @author Luis-Rangel
 * @version 1.1
 */
public class Arista extends JComponent implements Comparable<Arista>{
    private final Vertice origen;
    private final Vertice destino;
    private final int peso;
    private int origenX;
    private int origenY;
    private int destX;
    private int destY;
    private final JLabel pesoLabel;
    private Color colorDeLinea = Color.decode("#7094FF");

    /**
     * @param origenX Establece la coordenada x del vértice de origen.
     */
    public void setOrigenX(int origenX) {
        this.origenX = origenX;
        actualizaPosicionLabel();
    }

    /**
     * @param origenY Establece la coordenada y del vértice de origen.
     */
    public void setOrigenY(int origenY) {
        this.origenY = origenY;
        actualizaPosicionLabel();
    }

    /**
     * @param destX Establece la coordenada x del vértice de destino.
     */
    public void setDestX(int destX) {
        this.destX = destX;
        actualizaPosicionLabel();
    }

    /**
     * @param destY Establece la coordenada y del vértice de destino.
     */
    public void setDestY(int destY) {
        this.destY = destY;
        actualizaPosicionLabel();
    }

    /**
     * @return El vértice de origen de la arista.
     */
    public Vertice getOrigen() {
        return origen;
    }

    /**
     * @return El vértice de destino de la arista.
     */
    public Vertice getDestino() {
        return destino;
    }

    /**
     * @return La etiqueta de peso de la arista.
     */
    public JLabel getPesoLabel() {
        return pesoLabel;
    }

    /**
     * @return Peso de la arista.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Crea una instancia de la clase Arista que dibuja una línea que va desde el vértice origen hasta
     * el vértice destino y añade una etiqueta con su peso.
     * @param origen El vértice de origen de la arista.
     * @param destino El vértice de destino de la arista.
     * @param peso El peso de la arista.
     */
    public Arista(Vertice origen, Vertice destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.origenX = (origen.getX() + origen.getWidth() / 2);
        this.origenY = (origen.getY() + origen.getHeight() / 2);
        this.destX = destino.getX() + destino.getWidth() / 2;
        this.destY = destino.getY() + destino.getHeight() / 2;
        origen.conectaArista(this);
        destino.conectaArista(this);
        setName("Arista <" + origen.getID() + " <-> " + destino.getID() + ">");
        setBounds(0, 0, MainFrame.getMainPanel().getWidth(), MainFrame.getMainPanel().getHeight());
        setOpaque(false);
        pesoLabel = new JLabel(String.valueOf(peso));
        pesoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pesoLabel.setForeground(colorDeLinea);
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
                        getOrigen().desconectaArista(Arista.this);
                        getDestino().desconectaArista(Arista.this);
                        MainFrame.getGrafo().eliminaArista(Arista.this);
                        mainPanel.remove(pesoLabel);
                        mainPanel.remove(Arista.this);
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
     * @param x La coordenada x del punto.
     * @param y La coordenada y del punto.
     * @return true si el punto está tocando la arista, de lo contrario false.
     */
    private boolean estaTocandoArista(int x, int y) {
        int[] inicio = new int[2];
        int[] fin = new int[2];
        encuentraXY(inicio, fin);
        /*Verifica si el punto está en los puntos de origen o destino:
        Esto se debe a que la coordenada de origen/destino de la arista está "tocando" a un vértice.
        Retorna falso para evitar que el usuario haga miss-click."
        */
        if ((x == inicio[0] && y == inicio[1]) || (x == fin[0] && y == fin[1])) {
            return false;
        }
        // Calcula la distancia más corta entre el punto dado y el segmento de línea:
        Line2D.Double linea = new Line2D.Double(inicio[0], inicio[1], fin[0], fin[1]);
        double distanciaAlPunto = linea.ptSegDist(x, y);
        // Puede ser difícil darle click a la posición exacta, por eso se le añade un mayor "hit-box":
        int tolerancia = 10;
        return distanciaAlPunto <= tolerancia;
    }

    /**
     * Calcula las coordenadas de los puntos de inicio y fin de la arista.
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
     * Verifica si los ID de los vértices de origen y destino son iguales a los ID especificados.
     * De momento estoy trabajando con grafos no-dirigidos, por lo que la arista A → B es igual que B → A
     * @param v1 El ID del vértice de origen a comparar.
     * @param v2 El ID del vértice de destino a comparar.
     * @return true si los ID coinciden, de lo contrario false.
     */
    public boolean sonIguales(String v1, String v2) {
        if (v1.equals(origen.getID()) && v2.equals(destino.getID())) {
            return true;
        } else return v2.equals(origen.getID()) && v1.equals(destino.getID());
    }

    /**
     * @param color Actualiza el color de la arista y su peso.
     */
    public void setColor(Color color) {
        this.colorDeLinea = color;
        pesoLabel.setForeground(color);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(colorDeLinea);
        g2d.setStroke(new BasicStroke(2.0f));

        int[] inicio = new int[2];
        int[] fin = new int[2];
        encuentraXY(inicio, fin);

        g2d.drawLine(inicio[0], inicio[1], fin[0], fin[1]);
        g2d.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        return estaTocandoArista(x, y);
    }

    @Override
    public int compareTo(Arista otra) {
        return Integer.compare(this.peso, otra.peso);
    }
}