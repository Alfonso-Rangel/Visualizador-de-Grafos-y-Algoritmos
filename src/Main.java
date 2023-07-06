import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

/**
 * Aplicación gráfica de escritorio hecha con Java Swing que permite visualizar grafos ponderados no dirigidos y
 * aplicar algoritmos como recorrido en amplitud, en profundidad, camino más corto con Dijkstra y árbol de expansión mínima
 * @author Luis-Rangel
 * @version 1.1
 */
public class Main {
    public static void main(String[] args) {
        /*
         * ADVERTENCIA: Para visualizar el tema de color correctamente, es necesario
         * agregar los archivos de la carpeta "Look_and_Feel" ("flatlaf-3.1.1.jar" y "flatlaf-intellij-themes-3.1.1.jar")
         * al proyecto. (https://youtu.be/Dqlwr3uIeVM?t=61).
         * Si no te es posible añadir los archivos a las librerías, aún puedes correr el proyecto eliminando
         * la línea: "FlatDarkPurpleIJTheme.setup();" y el import.
         * Ten en cuenta que los colores de la aplicación están seleccionados basándome en este tema.
         * Eliminar o no agregar los archivos de la carpeta "Look_and_Feel" puede afectar la presentación visual
         * de la aplicación.
         */
        FlatDarkPurpleIJTheme.setup();
        new MainFrame();
    }
}