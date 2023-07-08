import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase principal que representa el marco de la aplicación "Visualizador de Grafos".
 * Proporciona una interfaz gráfica para visualizar y manipular grafos.
 *
 * @author Luis-Rangel
 * @version 1.1
 */
public class MainFrame extends JFrame {

    /**
     * Panel principal de la aplicación.
     * Este panel contiene los vértices y aristas del grafo.
     */
    private static final JPanel mainPanel = new JPanel();

    /**
     * Objeto de tipo Grafo utilizado para tener control y métricas de los vértices y aristas.
     * Se utiliza para realizar operaciones y obtener información sobre el estado actual del grafo.
     */
    private static final Grafo GRAFO = new Grafo();

    /**
     * Etiqueta inferior que muestra información sobre el modo de la aplicación.
     * Proporciona indicaciones y mensajes relacionados con las acciones que se pueden realizar.
     */
    private static JLabel infoLabel;

    /**
     * Modo actual de la aplicación.
     * Representa el tipo de acción que se realizará al interactuar con el grafo.
     */
    private static String modo = "Agregar Vértice/Arista";

    /**
     * @return El panel principal de la aplicación, este panel contiene elementos hijos como Vértices y Aristas.
     */
    public static JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * @return El modo actual de la aplicación, que indica el comportamiento seleccionado por el usuario.
     */
    public static String getModo() {
        return modo;
    }

    /**
     * @return El panel de información de la aplicación, el infoLabel le muestra información contextual al usuario.
     */
    public static JLabel getInfoLabel() {
        return infoLabel;
    }

    /**
     * @return El objeto Grafo que representa el estado actual del el grafo de la aplicación.
     */
    public static Grafo getGrafo() {
        return GRAFO;
    }

    /**
     * Constructor de la clase MainFrame.
     * Crea la interfaz gráfica de la aplicación y configura sus componentes.
     * Se establecen las dimensiones, el título y el comportamiento de la ventana.
     */
    public MainFrame() {
        super("Visualizador de Grafos");
        setSize(850, 650);
        setName(this.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);

        mainPanel.setName("Grafo");
        mainPanel.setLayout(null);
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leeClick(e);
            }
        });
        add(mainPanel);

        infoLabel = new JLabel("Click en espacio vacío para añadir vértice o en vértice para añadir arista");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setOpaque(true);
        infoLabel.setBorder(new EmptyBorder(7, 0, 7, 0));

        infoLabel.setFont(infoLabel.getFont().deriveFont(14.8f));
        add(infoLabel, BorderLayout.SOUTH);

        setJMenuBar(creaMenuBar());
        setVisible(true);
    }

    /**
     * @return La barra de menú creada para la aplicación.
     */
    private JMenuBar creaMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));

        JLabel modoLabel = new JLabel(modo);
        modoLabel.setName("ModoLabel");
        //-------------------------------------------------------------------------------
        JMenu fileMenu = new JMenu("Archivo");

        JMenuItem limpiarItem = new JMenuItem("Limpiar");
        limpiarItem.setName("Limpiar");
        limpiarItem.addActionListener(e -> {
            GRAFO.clear();
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        fileMenu.add(limpiarItem);

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.setName("Exit");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        //-------------------------------------------------------------------------------
        JMenu modoMenu = new JMenu("Modo");

        JMenuItem agregarItem = new JMenuItem("Agregar Vértice/Arista");
        agregarItem.setName("Agregar Vértice/Arista");
        agregarItem.addActionListener(e -> {
            modo = "Agregar Vértice/Arista";
            modoLabel.setText(modo);
            infoLabel.setText("Click en espacio vacío para añadir vértice o en vértice para añadir arista");
            GRAFO.descoloreaGrafo();
        });
        modoMenu.add(agregarItem);

        JMenuItem eliminarItem = new JMenuItem("Eliminar Vértice/Arista");
        eliminarItem.setName("Eliminar Vértice/Arista");
        eliminarItem.addActionListener(e -> {
            modo = "Eliminar Vértice/Arista";
            modoLabel.setText(modo);
            infoLabel.setText("Click en el elemento que quieras eliminar");
            GRAFO.descoloreaGrafo();
        });
        modoMenu.add(eliminarItem);

        JMenuItem ningunoItem = new JMenuItem("Ninguno");
        ningunoItem.setName("Ninguno");
        ningunoItem.addActionListener(e -> {
            modo = "Ninguno";
            modoLabel.setText(modo);
            infoLabel.setText("Ningún modo seleccionado");
        });
        modoMenu.add(ningunoItem);
        menuBar.add(modoMenu);
        //-------------------------------------------------------------------------------
        JMenu algoritmosMenu = new JMenu("Algoritmos");

        JMenuItem amplitudItem = new JMenuItem("Recorrido en Amplitud");
        amplitudItem.setName("Amplitud");
        amplitudItem.addActionListener(e -> {
            modo = "Recorrido en Amplitud";
            modoLabel.setText(modo);
            infoLabel.setText("Seleccione un vértice como punto de partida");
            GRAFO.crearMatrizAdyacencia();
            GRAFO.descoloreaGrafo();
        });
        algoritmosMenu.add(amplitudItem);

        JMenuItem profundidadItem = new JMenuItem("Recorrido en Profundidad");
        profundidadItem.setName("Profundidad");
        profundidadItem.addActionListener(e -> {
            modo = "Recorrido en Profundidad";
            modoLabel.setText(modo);
            infoLabel.setText("Seleccione un vértice como punto de partida");
            GRAFO.crearMatrizAdyacencia();
            GRAFO.descoloreaGrafo();
        });
        algoritmosMenu.add(profundidadItem);

        JMenuItem dijkstraItem = new JMenuItem("Algoritmo de Dijkstra");
        dijkstraItem.setName("Dijkstra");
        dijkstraItem.addActionListener(e -> {
            modo = "Algoritmo de Dijkstra";
            modoLabel.setText(modo);
            infoLabel.setText("Seleccione un vértice como punto de partida");
            GRAFO.crearMatrizAdyacencia();
            GRAFO.descoloreaGrafo();
        });
        algoritmosMenu.add(dijkstraItem);

        JMenuItem arbolItem = new JMenuItem("Árbol de expansión mínima");
        arbolItem.setName("Árbol de Expansión Mínima");
        arbolItem.addActionListener(e -> {
            modo = "Árbol de expansión mínima";
            modoLabel.setText(modo);
            infoLabel.setText("Seleccione un vértice arbitrario");
            GRAFO.descoloreaGrafo();
        });
        algoritmosMenu.add(arbolItem);

        menuBar.add(algoritmosMenu);
        //-------------------------------------------------------------------------------
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelDerecho.setOpaque(false);
        panelDerecho.add(modoLabel);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(panelDerecho);

        return menuBar;
    }

    /**
     * Método para manejar el evento de click del mouse en el panel principal.
     * Se evalúa el input del usuario y manda mensajes de error en caso de una entrada incorrecta.
     * Este método solo evalúa clicks en el panel principal, pero no en Vértices o Aristas, pues estos tienen sus propios eventos.
     * @param e El evento de click del mouse en el panel principal de la aplicación.
     */
    private static void leeClick(MouseEvent e) {
        Point click = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), mainPanel);
        Component component = mainPanel.getComponentAt(click);
        String ID = "";
        switch (modo) {
            case "Agregar Vértice/Arista" -> {
                if (!(component instanceof Vertice) && Vertice.getvOrigen() == null) {
                    ID = (String) JOptionPane.showInputDialog(
                            mainPanel,
                            "Ingrese el nombre:",
                            "Nombre del vértice",
                            JOptionPane.PLAIN_MESSAGE,
                            null, null, ID
                    );
                    if (ID == null) {
                        break;
                    }
                    if (ID.isBlank()) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "El nombre no puede estar vacío",
                                "Nombre inválido",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else if (ID.length() > 3) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "El nombre no puede tener más de 3 caracteres",
                                "Nombre muy largo",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        Vertice vertice = GRAFO.creaVertice(ID, e.getX(), e.getY());
                        if (vertice != null) {
                            mainPanel.add(vertice);
                            mainPanel.setComponentZOrder(vertice, 0);
                            mainPanel.revalidate();
                            mainPanel.repaint();
                        }
                        else {
                            JOptionPane.showMessageDialog(
                                    mainPanel,
                                    "El vértice ya existe en el grafo",
                                    "Vértice repetido",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                    }
                }
            }
            case "Árbol de expansión mínima" -> {
                int opcion = JOptionPane.showConfirmDialog(
                        mainPanel,
                        "¿Quieres eliminar los aristas grises?",
                        "Confirma eliminación", JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    for (Arista arista : GRAFO.aristasInnecesarias()) {
                        arista.getOrigen().desconectaArista(arista);
                        arista.getDestino().desconectaArista(arista);
                        GRAFO.eliminaArista(arista);
                        mainPanel.remove(arista.getPesoLabel());
                        mainPanel.remove(arista);
                        mainPanel.revalidate();
                    }
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }
            }
            default -> {}
        }
    }
}
