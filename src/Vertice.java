import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

/**
 * Representa un vértice en el grafo visualizado por la aplicación.
 * @author Luis-Rangel
 * @version 1.0
 */
public class Vertice extends JPanel implements MouseMotionListener {
    private final String ID;
    private static final int V_SIZE = 50;
    private static Vertice vOrigen;
    private Color color = Color.WHITE;

    /**
     * Crea una nueva instancia de Vertice y lo pinta en las coordenadas dadas.
     *
     * @param x  La coordenada x del vértice.
     * @param y  La coordenada y del vértice.
     * @param ID El identificador del vértice.
     */
    public Vertice(int x, int y, String ID) {
        this.ID = ID;
        setName("Vertice " + ID);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(V_SIZE, V_SIZE));
        setLayout(new GridBagLayout());
        setBounds(x - V_SIZE / 2, y - V_SIZE / 2, V_SIZE, V_SIZE);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leeClick(e);
            }
        });

        JLabel vertexLabel = new JLabel(String.valueOf(ID));
        vertexLabel.setName("VerticeLabel: " + ID);
        add(vertexLabel);
        addMouseMotionListener(this);
    }

    /**
     * Devuelve el identificador del vértice.
     *
     * @return El identificador del vértice.
     */
    public String getID() {
        return ID;
    }

    /**
     * Devuelve el tamaño de los vértices.
     *
     * @return El tamaño de los vértices.
     */
    public static int getvSize() {
        return V_SIZE;
    }

    /**
     * Devuelve el vértice origen actualmente seleccionado.
     *
     * @return El vértice origen seleccionado.
     */
    public static Vertice getvOrigen() {
        return vOrigen;
    }

    /**
     * Establece el color del vértice.
     *
     * @param color El color a establecer.
     */
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
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
        for (Component component : getParent().getComponents()) {
            if (component instanceof Arista arista) {
                if (arista.getOrigen().getID().equals(getID())) {
                    arista.setOrigenX(getX() + V_SIZE / 2);
                    arista.setOrigenY(getY() + V_SIZE / 2);
                } else if (arista.getDestino().getID().equals(getID())) {
                    arista.setDestX(getX() + V_SIZE / 2);
                    arista.setDestY(getY() + V_SIZE / 2);
                }
                arista.repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // No se utiliza, pero se implementa por la interfaz MouseMotionListener.
    }

    /**
     * Maneja el evento de clic del ratón en el vértice.
     * Hay dos escenarios donde el usuario dará click al vértice:
     * Si está en el modo "Agregar..." quiere decir que quiere agregar una arista entre dos vértices.
     * Si está en el modo "Eliminar..." quiere decir que quiere eliminar el vértice.
     * Este método implementa la lógica para ambos casos.
     *
     * @param e El evento de clic del ratón.
     */
    private void leeClick(MouseEvent e) {
        String modo = MainFrame.getModo();
        JPanel mainPanel = MainFrame.getMainPanel();
        Grafo grafo = MainFrame.getGrafo();

        switch (modo) {
            case "Agregar Vértice/Arista" -> {
                //Si no hay un vértice seleccionado anteriormente, este vértice es el origen de la arista
                if (vOrigen == null) {
                    vOrigen = this;
                    vOrigen.setColor(Color.GREEN);
                }
                /* Si hay un vértice seleccionado previamente y el vértice actual es diferente al seleccionado,
                   entonces este vértice es el destino de la arista
                 */
                else if (vOrigen != this) {
                    this.setColor(Color.GREEN);
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
                                "Peso de Arista",
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
                    vOrigen.setColor(Color.WHITE);
                    this.setColor(Color.WHITE);
                    vOrigen = null;
                } else {
                    vOrigen.setColor(Color.WHITE);
                    this.setColor(Color.WHITE);
                    vOrigen = null;
                }
            }
            case "Eliminar Vértice/Arista" -> {
                // Para eliminar el vértice, tenemos que eliminar todas las aristas conectadas a él:
                Component[] components = mainPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof Arista arista) {
                        if (arista.getOrigen() == Vertice.this || arista.getDestino() == Vertice.this) {
                            grafo.eliminaArista(arista);
                            mainPanel.remove(arista.getPesoLabel());
                            mainPanel.remove(arista);
                            mainPanel.revalidate();
                        }
                    }
                }
                grafo.eliminaVertice(Vertice.this);
                mainPanel.remove(Vertice.this);
                mainPanel.repaint();
                mainPanel.revalidate();
            }
            default -> {
                // No se realiza ninguna acción para otros modos.
            }
        }
    }
}
