import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.PriorityQueue;
import javax.swing.SwingUtilities;

/**
 *  La clase representa un vértice en el grafo para ser visualizado por la aplicación.
 *  Dibuja un componente circular.
 * @author Luis-Rangel
 * @version 1.1
 */
public class Vertice extends JPanel implements MouseMotionListener {
    private final String ID;
    // Se guardan sus aristas conectadas según su peso, esto es muy útil para construir árboles de expansión mínima.
    private final PriorityQueue<Arista> aristasConectadas = new PriorityQueue<>(Comparator.comparingInt(Arista::getPeso));
    private static final int V_SIZE = 50;
    private static Vertice vOrigen;
    private Color colorDeVertice = Color.decode("#ED94FF");

    /**
     * @return Las aristas conectadas al vértice, en orden ascendente.
     */
    public PriorityQueue<Arista> getAristasConectadas() {
        return aristasConectadas;
    }

    /**
     * @return El identificador del vértice.
     */
    public String getID() {
        return ID;
    }

    /**
     * @return El tamaño de los vértices.
     */
    public static int getvSize() {
        return V_SIZE;
    }

    /**
     * @return El vértice origen seleccionado.
     */
    public static Vertice getvOrigen() {
        return vOrigen;
    }

    /**
     * @param colorDeVertice Actualiza el color del vértice.
     */
    public void setColorDeVertice(Color colorDeVertice) {
        this.colorDeVertice = colorDeVertice;
        repaint();
    }

    /**
     * Crea una nueva instancia de Vertice y lo pinta en las coordenadas dadas.
     * @param x  La coordenada x del vértice.
     * @param y  La coordenada y del vértice.
     * @param ID El identificador del vértice.
     */
    public Vertice(int x, int y, String ID) {
        this.ID = ID;
        setName("Vertice " + ID);
        setBackground(colorDeVertice);
        setPreferredSize(new Dimension(V_SIZE, V_SIZE));
        setLayout(new GridBagLayout());
        setBounds(x - V_SIZE / 2, y - V_SIZE / 2, V_SIZE, V_SIZE);
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leeClick();
            }
        });
        JLabel vertexLabel = new JLabel(String.valueOf(ID));
        vertexLabel.setName("VerticeLabel: " + ID);
        vertexLabel.setForeground(Color.BLACK);
        add(vertexLabel);
        addMouseMotionListener(this);
    }

    /**
     * @param arista Añade la arista a la cola de aristas conectadas al vértice.
     */
    public void conectaArista(Arista arista) {
        aristasConectadas.add(arista);
    }

    /**
     * @param arista Elimina la arista de la cola de aristas conectadas al vértice.
     */
    public void desconectaArista(Arista arista) {
        aristasConectadas.remove(arista);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(colorDeVertice);
        g.fillOval(0, 0, V_SIZE, V_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, V_SIZE, V_SIZE);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Actualiza la posición del vértice al arrastrarlo:
        Point nuevoPunto = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), getParent());
        int x = nuevoPunto.x - V_SIZE / 2;
        int y = nuevoPunto.y - V_SIZE / 2;
        setLocation(x, y);
        repaint();

        // Actualiza las aristas al mover el vértice:
        for (Arista arista : aristasConectadas) {
            if (arista.getOrigen().getID().equals(this.ID)) {
                arista.setOrigenX(getX() + V_SIZE / 2);
                arista.setOrigenY(getY() + V_SIZE / 2);
            } else if (arista.getDestino().getID().equals(this.ID)) {
                arista.setDestX(getX() + V_SIZE / 2);
                arista.setDestY(getY() + V_SIZE / 2);
            }
            arista.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // No se sobreescribe, pero se implementa por la interfaz MouseMotionListener.
    }

    /**
     * Maneja el evento de click del ratón en el vértice.
     * Hay varios escenarios donde el usuario dará click al vértice:
     * Si está en el modo "Agregar..." quiere decir que quiere agregar una arista entre dos vértices.
     * Si está en el modo "Eliminar..." quiere decir que quiere eliminar el vértice.
     * Si está en algún algoritmo, este vértice será el punto de partida.
     * Este método implementa la lógica para los anteriores casos.
     */
    private void leeClick() {
        String modo = MainFrame.getModo();
        JPanel mainPanel = MainFrame.getMainPanel();
        Grafo grafo = MainFrame.getGrafo();
        switch (modo) {
            case "Agregar Vértice/Arista" -> {
                grafo.descoloreaGrafo();
                //Si no hay un vértice seleccionado anteriormente, este vértice es el origen de la arista
                if (vOrigen == null) {
                    vOrigen = this;
                    vOrigen.setColorDeVertice(Color.decode("#25AD6B"));
                }
                /* Si hay un vértice seleccionado previamente y el vértice actual es diferente al seleccionado,
                   entonces este vértice es el destino de la arista
                 */
                else if (vOrigen != this) {
                    this.setColorDeVertice(Color.decode("#25AD6B"));
                    if (grafo.existeArista(vOrigen.getID(), ID)) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "Ya existe la arista que está intentando agregar",
                                "Arista existente",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JTextField textField = new JTextField(10);
                        Object[] mensaje = {"Peso:", textField};
                        int opcion = JOptionPane.showConfirmDialog(
                                mainPanel,
                                mensaje,
                                "Ingrese el peso de la Arista",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );
                        if (opcion == JOptionPane.OK_OPTION) {
                            String peso = textField.getText();
                            boolean pesoValido;
                            int valor = 0;
                            try {
                                valor = Integer.parseInt(peso);
                                pesoValido = valor > 0;
                            } catch (NumberFormatException numberFormatException) {
                                pesoValido = false;
                            }
                            if (!pesoValido) {
                                JOptionPane.showMessageDialog(
                                        mainPanel,
                                        "El peso debe ser un número entero mayor a 0.",
                                        "Peso inválido",
                                        JOptionPane.WARNING_MESSAGE
                                );

                            } else {
                                Arista arista = grafo.creaArista(vOrigen.getID(), ID, valor);
                                if (arista != null) {
                                    mainPanel.add(arista);
                                    mainPanel.setComponentZOrder(arista, 0);
                                    mainPanel.revalidate();
                                    mainPanel.repaint();
                                }
                            }
                        }
                    }
                    vOrigen.setColorDeVertice(Color.decode("#ED94FF"));
                    this.setColorDeVertice(Color.decode("#ED94FF"));
                    vOrigen = null;
                } else {
                    /* Si el usuario hace click dos veces en el mismo vértice, vuelve a su color por
                       defecto una especie de "cancelar acción".
                     */
                    vOrigen.setColorDeVertice(Color.decode("#ED94FF"));
                    this.setColorDeVertice(Color.decode("#ED94FF"));
                    vOrigen = null;
                }
            }
            case "Eliminar Vértice/Arista" -> {
                grafo.descoloreaGrafo();
                // Para eliminar el vértice, tenemos que eliminar todas las aristas conectadas a él:
                while (!aristasConectadas.isEmpty()) {
                    Arista arista = aristasConectadas.poll();
                    arista.getOrigen().desconectaArista(arista);
                    arista.getDestino().desconectaArista(arista);
                    grafo.eliminaArista(arista);
                    mainPanel.remove(arista.getPesoLabel());
                    mainPanel.remove(arista);
                    mainPanel.revalidate();
                }
                grafo.eliminaVertice(Vertice.this);
                mainPanel.remove(Vertice.this);
                mainPanel.repaint();
                mainPanel.revalidate();
            }
            case "Recorrido en Amplitud" -> {
                grafo.descoloreaGrafo();
                MainFrame.getInfoLabel().setText(grafo.recorreEnAmplitud(this));
            }
            case "Recorrido en Profundidad" -> {
                grafo.descoloreaGrafo();
                MainFrame.getInfoLabel().setText(grafo.recorreEnProfundidad(this));
            }
            case "Algoritmo de Dijkstra" -> {
                grafo.descoloreaGrafo();
                MainFrame.getInfoLabel().setText(grafo.algoritmoDijkstra(this));
            }
            case "Árbol de expansión mínima" -> {
                grafo.descoloreaGrafo();
                grafo.arbolExpansionMinima(this);
                MainFrame.getInfoLabel().setText("Click en el panel para borrar aristas grises");
            }
            default -> {}
        }
    }
}