import java.util.ArrayList;

/**
 * Clase que representa un grafo.
 * @author Luis-Rangel
 * @version 1.0
 */
public class Grafo {
    private final ArrayList<Vertice> vertices = new ArrayList<>();
    private final ArrayList<Arista> aristas = new ArrayList<>();

    /**
     * Crea un nuevo vértice y lo agrega al grafo.
     *
     * @param ID El ID del vértice.
     * @param x  La coordenada x del vértice.
     * @param y  La coordenada y del vértice.
     * @return El vértice creado, o null si ya existe un vértice con el mismo ID.
     */
    public Vertice creaVertice(String ID, int x, int y) {
        if (existeVertice(ID)) {
            return null;
        } else {
            Vertice vertice = new Vertice(x, y, ID);
            vertices.add(vertice);
            return vertice;
        }
    }

    /**
     * Elimina un vértice del grafo.
     *
     * @param vertice El vértice a eliminar.
     */
    public void eliminaVertice(Vertice vertice) {
        vertices.remove(vertice);
    }

    /**
     * Crea una nueva arista entre dos vértices y la agrega al grafo.
     *
     * @param origenID  El ID del vértice de origen.
     * @param destinoID El ID del vértice de destino.
     * @param peso      El peso de la arista.
     * @return La arista creada, o null si alguno de los vértices no existe.
     */
    public Arista creaArista(String origenID, String destinoID, int peso) {
        Vertice vOrigen = encuentraVerticeID(origenID);
        Vertice vDestino = encuentraVerticeID(destinoID);

        if (vOrigen != null && vDestino != null) {
            Arista arista = new Arista(vOrigen, vDestino, peso);
            aristas.add(arista);
            return arista;
        }
        return null;
    }

    /**
     * Elimina una arista del grafo.
     *
     * @param arista La arista a eliminar.
     */
    public void eliminaArista(Arista arista) {
        aristas.remove(arista);
    }

    /**
     * Elimina todos los vértices y aristas del grafo.
     */
    public void clear() {
        vertices.clear();
        aristas.clear();
    }

    /**
     * Verifica si un vértice con el ID especificado existe en el grafo.
     *
     * @param ID El ID del vértice a verificar.
     * @return true si el vértice existe, de lo contrario false.
     */
    public boolean existeVertice(String ID) {
        for (Vertice vertice : vertices) {
            if (vertice.getID().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si existe una arista entre dos vértices.
     *
     * @param v1 El ID del vértice de origen.
     * @param v2 El ID del vértice de destino.
     * @return true si la arista existe, de lo contrario false.
     */
    public boolean existeArista(String v1, String v2) {
        for (Arista arista : aristas) {
            if (arista.sonIguales(v1, v2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Encuentra un vértice en el grafo utilizando su ID.
     *
     * @param ID El ID del vértice a buscar.
     * @return El vértice encontrado, o null si no se encontró ningún vértice con el ID especificado.
     */
    public Vertice encuentraVerticeID(String ID) {
        for (Vertice vertice : vertices) {
            if (vertice.getID().equals(ID)) {
                return vertice;
            }
        }
        return null;
    }
}
