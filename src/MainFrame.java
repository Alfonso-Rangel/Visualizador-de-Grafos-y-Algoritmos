import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase principal que representa el marco de la aplicación "Visualizador de Grafos".
 * Proporciona una interfaz gráfica para visualizar y manipular grafos.
 *
 * @author Luis-Rangel
 * @version 1.0
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
     * Devuelve el panel principal de la aplicación.
     *
     * @return El panel principal que contiene el grafo.
     */
    public static JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Devuelve el modo actual de la aplicación.
     *
     * @return El modo actual de la aplicación.
     */
    public static String getModo() {
        return modo;
    }

    /**
     * Devuelve el grafo utilizado en la aplicación.
     *
     * @return El objeto Grafo que representa el grafo de la aplicación.
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
        setSize(800, 600);
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
        infoLabel.setBackground(Color.DARK_GRAY);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(infoLabel, BorderLayout.SOUTH);

        setJMenuBar(creaMenuBar());
        setVisible(true);
    }

    /**
     * Crea y devuelve la barra de menú de la aplicación.
     *
     * @return La barra de menú creada para la aplicación.
     */
    private JMenuBar creaMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BorderLayout());
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.setForeground(Color.WHITE);
        menuBar.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel modoLabel = new JLabel(modo + "   ");
        modoLabel.setName("ModoLabel");
        modoLabel.setBackground(Color.BLACK);
        modoLabel.setForeground(Color.WHITE);

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

        fileMenu.setForeground(Color.WHITE);

        menuBar.add(fileMenu, BorderLayout.WEST);

        JMenu modoMenu = new JMenu("Modo");

        JMenuItem agregarItem = new JMenuItem("Agregar Vértice/Arista");
        agregarItem.setName("Agregar Vértice/Arista");
        agregarItem.addActionListener(e -> {
            modo = "Agregar Vértice/Arista";
            modoLabel.setText(modo + "   ");
            infoLabel.setText("Click en espacio vacío para añadir vértice o en vértice para añadir arista");
        });
        modoMenu.add(agregarItem);

        JMenuItem eliminarItem = new JMenuItem("Eliminar Vértice/Arista");
        eliminarItem.setName("Eliminar Vértice/Arista");
        eliminarItem.addActionListener(e -> {
            modo = "Eliminar Vértice/Arista";
            modoLabel.setText(modo + "   ");
            infoLabel.setText("Click en el elemento que quieras eliminar");
        });
        modoMenu.add(eliminarItem);

        JMenuItem ningunoItem = new JMenuItem("Ninguno");
        ningunoItem.setName("Ninguno");
        ningunoItem.addActionListener(e -> {
            modo = "Ninguno";
            modoLabel.setText(modo + "   ");
            infoLabel.setText("Ningún modo seleccionado");
        });
        modoMenu.add(ningunoItem);
        modoMenu.setForeground(Color.WHITE);

        menuBar.add(modoMenu, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.add(modoLabel, BorderLayout.EAST);

        menuBar.add(panelDerecho, BorderLayout.EAST);

        return menuBar;
    }


    /**
     * Método para manejar el evento de click del mouse en el panel principal.
     * Se evalúa el input del usuario y manda mensajes de error en caso de una entrada incorrecta.
     * Este método solo evalúa clicks en el panel principal pero no en Vértices o Aristas.
     * @param e El evento de click del mouse.
     */
    private static void leeClick(MouseEvent e) {
        Point click = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), mainPanel);
        Component component = mainPanel.getComponentAt(click);
        String ID = "";
        switch (modo) {
            case "Agregar Vértice/Arista" -> {
                if (!(component instanceof Vertice isAVertice) && Vertice.getvOrigen() == null) {
                    ID = (String) JOptionPane.showInputDialog(
                            mainPanel,
                            "Ingrese el nombre:",
                            "Nombre",
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
                    }
                }
            }
        }
    }
}
