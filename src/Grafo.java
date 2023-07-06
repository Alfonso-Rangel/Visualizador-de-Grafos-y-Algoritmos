import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Clase que representa un grafo, se encarga llevar un control de los vértices y aristas, así como mostrar y
 * actualizar información para así poder aplicar los algoritmos sobre el grafo representado en el panel.
 * @author Luis-Rangel
 * @version 1.1
 */
public class Grafo {
    private final ArrayList<Vertice> vertices = new ArrayList<>();
    private final ArrayList<Arista> aristas = new ArrayList<>();
    private ArrayList<Arista> aristasDeRecubrimiento;
    private ArrayList<ArrayList<Integer>> matrizAdyacencia;
    private int dimension;

    /**
     * Crea un nuevo vértice y lo agrega a la lista.
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
     * @param vertice Elimina vértice dado de la lista de vértices.
     */
    public void eliminaVertice(Vertice vertice) {
        vertices.remove(vertice);
    }

    /**
     * Crea una nueva arista entre dos vértices y la agrega a la lista.
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
     * @param arista Elimina la arista de la lista de aristas.
     */
    public void eliminaArista(Arista arista) {
        aristas.remove(arista);
    }

    /**
     * Elimina todos los vértices y aristas de sus respectivas listas.
     */
    public void clear() {
        vertices.clear();
        aristas.clear();
    }

    /**
     * Verifica si un vértice con el ID especificado existe en el grafo.
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

    /**
     * Devuelve una arista si existe entre dos vértices dados.
     * @param origen El vértice de origen.
     * @param destino El vértice de destino.
     * @return Arista si existe, null en otro caso.
     */
    public Arista aristaConectada(Vertice origen, Vertice destino) {
        for (Arista a: aristas) {
            if ((a.getOrigen() == origen && a.getDestino() == destino) || (a.getOrigen() == destino && a.getDestino() == origen)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Hasta ahora hemos representado el grafo con dos listas: vértices y aristas, eso es muy útil y práctico para
     * la mayoría de los casos, sin embargo, para poder aplicar algoritmos en el grafo se necesita una representación
     * que nos permita modelar una relación entre ellos, por eso yo he decidido utilizar una matriz de adyacencia,
     * ya que es muy útil cuando se tienen grafos muy densos y podemos aprovechar las dos listas que ya tenemos.
     */
    public void crearMatrizAdyacencia() {
        dimension = vertices.size();
        matrizAdyacencia = new ArrayList<>();

        // Inicializar la matriz de adyacencia con ceros
        for (int i = 0; i < dimension; i++) {
            matrizAdyacencia.add(new ArrayList<>());
            for (int j = 0; j < dimension; j++) {
                matrizAdyacencia.get(i).add(0);
            }
        }

        // Llenar la matriz de adyacencia con las conexiones existentes
        for (Arista arista : aristas) {
            Vertice origen = arista.getOrigen();
            Vertice destino = arista.getDestino();
            int indiceOrigen = vertices.indexOf(origen);
            int indiceDestino = vertices.indexOf(destino);
            matrizAdyacencia.get(indiceOrigen).set(indiceDestino, arista.getPeso());
            matrizAdyacencia.get(indiceDestino).set(indiceOrigen, arista.getPeso());
        }
    }

    /**
     * Realiza un recorrido en amplitud en el gráfico y devuelve los vértices visitados.
     * @param inicio El vértice al que se le aplicará el algoritmo.
     * @return Una representación en cadena de los vértices visitados.
     */
    public String recorreEnAmplitud(Vertice inicio) {
        ArrayList<Vertice> verticesVisitados = new ArrayList<>();
        ArrayList<Arista> aristasVisitadas = new ArrayList<>();
        Queue<Vertice> verticesAVisitar = new LinkedList<>();
        amplitud(inicio, verticesVisitados, verticesAVisitar, aristasVisitadas);
        return muestraRecorrido(verticesVisitados, aristasVisitadas);
    }

    /**
     * Método auxiliar del recorrido en amplitud, recorre el grafo recursivamente.
     * @param origen Es el vértice origen desde donde buscarán vecinos.
     * @param verticesVisitados Un arreglo que representa los vértices visitados.
     * @param verticesAVisitar Una cola que representa los vértices aún por visitar.
     * @param aristasVisitadas Un arreglo que representa las aristas visitadas.
     */
    private void amplitud(Vertice origen, ArrayList<Vertice> verticesVisitados, Queue<Vertice> verticesAVisitar, ArrayList<Arista> aristasVisitadas) {
        if (verticesVisitados.isEmpty()) {
            verticesAVisitar.offer(origen);
            verticesVisitados.add(origen);
            amplitud(origen, verticesVisitados, verticesAVisitar, aristasVisitadas);
        }
        else {
            if (!verticesAVisitar.isEmpty() && origen != null) {
                Vertice destino = null;
                origen = verticesAVisitar.poll();
                int indiceActual = vertices.indexOf(origen);
                for (int i = 0; i < dimension; i++) {
                    if (matrizAdyacencia.get(indiceActual).get(i) != 0) {
                        destino = vertices.get(i);
                        if (!verticesVisitados.contains(destino)) {
                            verticesVisitados.add(destino);
                            verticesAVisitar.offer(destino);
                            aristasVisitadas.add(aristaConectada(origen, destino));
                        }
                    }
                }
                amplitud(destino, verticesVisitados, verticesAVisitar, aristasVisitadas);
            }
        }
    }

    /**
     * Realiza un recorrido en profundidad en el gráfico y devuelve los vértices visitados.
     * @param inicio El vértice al que se le aplicará el algoritmo.
     * @return Una representación en cadena de los vértices visitados.
     */
    public String recorreEnProfundidad(Vertice inicio) {
        ArrayList<Vertice> verticesVisitados = new ArrayList<>();
        ArrayList<Arista> aristasVisitados = new ArrayList<>();
        profundidad(inicio, verticesVisitados, aristasVisitados);
        return muestraRecorrido(verticesVisitados, aristasVisitados);
    }

    /**
     * Método auxiliar del recorrido en profundidad, visita los vértices recursivamente.
     * @param origen Es el vértice origen desde donde buscarán vecinos.
     * @param verticesVisitados Un arreglo que representa los vértices visitados.
     * @param aristasVisitadas Un arreglo que representa las aristas visitadas.
     */
    private void profundidad(Vertice origen, ArrayList<Vertice> verticesVisitados, ArrayList<Arista> aristasVisitadas) {
        if (verticesVisitados.isEmpty()) {
            verticesVisitados.add(origen);
            profundidad(origen, verticesVisitados, aristasVisitadas);
        }
        else {
            int indiceActual = vertices.indexOf(origen);
            for (int i = 0; i < dimension; i++) {
                if (matrizAdyacencia.get(indiceActual).get(i) != 0) {
                    Vertice destino = vertices.get(i);
                    if (!verticesVisitados.contains(destino)) {
                        verticesVisitados.add(destino);
                        aristasVisitadas.add(aristaConectada(origen, destino));
                        profundidad(destino, verticesVisitados, aristasVisitadas);
                    }
                }
            }
        }
    }

    /**
     * El algoritmo de Dijkstra encuentra el camino más corto de una gráfica ponderada.
     * Hace uso de una matriz de adyacencia y eso le permite trabajar tanto con grafos no dirigidos como dirigidos.
     * @param inicio El vértice al que se le aplicará el algoritmo.
     * @return Una representación en cadena del recorrido más corto.
     */
    public String algoritmoDijkstra(Vertice inicio) {
        ArrayList<Arista> aristasVisitadas = new ArrayList<>();
        ArrayList<Vertice> verticesVisitados = new ArrayList<>();
        encontrarCaminoMasCorto(inicio, verticesVisitados, aristasVisitadas);
        return muestraRecorrido(verticesVisitados, aristasVisitadas);
    }

    /**
     * Encuentra el camino más corto desde el vértice de origen hasta todos los demás vértices del grafo.
     * @param origen Es el vértice origen desde donde buscarán vecinos.
     * @param verticesVisitados Un arreglo que representa los vertices visitados.
     * @param aristasVisitadas Un arreglo que representa las aristas visitadas.
     */
    private void encontrarCaminoMasCorto(Vertice origen, ArrayList<Vertice> verticesVisitados, ArrayList<Arista> aristasVisitadas) {
        HashMap<Vertice, Integer> distancias = new HashMap<>();
        HashMap<Vertice, Vertice> padres = new HashMap<>();

        // Inicializar distancias con valor infinito para todos los vértices excepto el origen
        for (Vertice v : vertices) {
            if (v == origen) {
                distancias.put(v, 0);
            } else {
                distancias.put(v, Integer.MAX_VALUE);
            }
        }

        PriorityQueue<Vertice> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
        colaPrioridad.offer(origen);

        while (!colaPrioridad.isEmpty()) {
            Vertice actual = colaPrioridad.poll();
            // Verificar si el vértice actual ya ha sido visitado
            if (verticesVisitados.contains(actual)) {
                continue;
            }
            verticesVisitados.add(actual);
            for (Vertice vecino : obtenerVecinos(actual)) {
                int distancia = distancias.get(actual) + obtenerPesoArista(actual, vecino);
                if (distancia < distancias.get(vecino)) {
                    distancias.put(vecino, distancia);
                    padres.put(vecino, actual);
                    colaPrioridad.offer(vecino);
                }
            }
        }
        // Construir la lista de aristas visitadas a partir de los padres
        for (Vertice vertice : verticesVisitados) {
            Vertice padre = padres.get(vertice);
            if (padre != null) {
                Arista arista = aristaConectada(padre, vertice);
                aristasVisitadas.add(arista);
            }
        }
    }

    /**
     * Obtiene los vértices vecinos del vértice dado.
     * @param vertice El vértice del cual se obtienen los vecinos.
     * @return Una lista de vértices vecinos al vértice dado.
     */
    private List<Vertice> obtenerVecinos(Vertice vertice) {
        List<Vertice> vecinos = new ArrayList<>();
        int indice = vertices.indexOf(vertice);
        for (int i = 0; i < dimension; i++) {
            if (matrizAdyacencia.get(indice).get(i) != 0) {
                vecinos.add(vertices.get(i));
            }
        }
        return vecinos;
    }

    /**
     * Obtiene el peso de la arista entre el vértice de origen y el vértice de destino.
     * @param origen El vértice de origen.
     * @param destino El vértice de destino.
     * @return El peso de la arista entre los vértices origen y destino.
     */
    private int obtenerPesoArista(Vertice origen, Vertice destino) {
        int indiceOrigen = vertices.indexOf(origen);
        int indiceDestino = vertices.indexOf(destino);
        return matrizAdyacencia.get(indiceOrigen).get(indiceDestino);
    }

    /**
     * El árbol de expansión mínima representa las conexiones mínimas necesarias para conectar
     * todos los vértices del grafo con el menor peso posible.
     */
    public void arbolExpansionMinima(Vertice inicio) {
        ArrayList<Vertice> verticesVisitados = new ArrayList<>();
        ArrayList<Arista> aristasVisitados = new ArrayList<>();
        PriorityQueue<Arista> aristasConectadas = new PriorityQueue<>(inicio.getAristasConectadas());
        verticesVisitados.add(inicio);
        algoritmoKruskal(verticesVisitados, aristasVisitados, aristasConectadas);
        aristasDeRecubrimiento = new ArrayList<>(aristasVisitados);
        for (Arista arista : aristas) {
            if (!aristasDeRecubrimiento.contains(arista)) {
                arista.setColor(Color.decode("#463F57"));
            }
        }
    }

    /**
     * El algoritmo de Kruskal es un algoritmo greedy utilizado para encontrar un árbol de expansión mínima en un grafo ponderado.
     * La idea principal del algoritmo es seleccionar las aristas de menor peso y agregarlas a la cola de prioridad
     * hasta visitar todos los vértices conectados.
     * @param verticesVisitados Un arreglo que representa los vertices visitados.
     * @param aristasVisitados Un arreglo que representa las aristas visitadas.
     * @param vecinos Una cola de prioridad que representa las aristas de menor peso.
     */
    private void algoritmoKruskal(ArrayList<Vertice> verticesVisitados, ArrayList<Arista> aristasVisitados, PriorityQueue<Arista> vecinos) {
        if (!vecinos.isEmpty()) {
            Arista aristaOptima = vecinos.poll();
            if (!aristasVisitados.contains(aristaOptima)) {
                Vertice origen = aristaOptima.getOrigen();
                Vertice destino = aristaOptima.getDestino();
                if (!verticesVisitados.contains(origen)) {
                    verticesVisitados.add(origen);
                    aristasVisitados.add(aristaOptima);
                    vecinos = uneColaPrioridad(vecinos, new PriorityQueue<>(origen.getAristasConectadas()));
                }
                if (!verticesVisitados.contains(destino)) {
                    verticesVisitados.add(destino);
                    aristasVisitados.add(aristaOptima);
                    vecinos = uneColaPrioridad(vecinos, new PriorityQueue<>(destino.getAristasConectadas()));
                }
            }
            algoritmoKruskal(verticesVisitados, aristasVisitados, vecinos);
        }
    }

    public ArrayList<Arista> aristasInnecesarias() {
        ArrayList<Arista> aristasInnecesarias = new ArrayList<>();
        if (aristasDeRecubrimiento != null && !aristasDeRecubrimiento.isEmpty()) {
            for (Arista arista : aristas) {
                if (!aristasDeRecubrimiento.contains(arista)) {
                    aristasInnecesarias.add(arista);
                }
            }
        }
        return  aristasInnecesarias;
    }

    private PriorityQueue<Arista> uneColaPrioridad(PriorityQueue<Arista> cola1, PriorityQueue<Arista> cola2) {
        Set<Arista> resultado = new HashSet<>();
        while (!cola1.isEmpty()) {
            Arista arista = cola1.poll();
            resultado.add(arista);
        }
        while (!cola2.isEmpty()) {
            Arista arista = cola2.poll();
            resultado.add(arista);
        }
        return new PriorityQueue<>(resultado);
    }

    /**
     * En los recorridos en profundidad y amplitud y en el algoritmo de Dijkstra se desea poder mostrarle al usuario
     * el recorrido, este método se encarga de mostrarlo en una cadena.
     * @param verticesVisitados Un arreglo de vértices a mostrar en orden.
     * @param aristasVisitados Un arreglo de aristas a mostrar en orden.
     * @return Representación en cadena del recorrido.
     */
    private String muestraRecorrido(ArrayList<Vertice> verticesVisitados, ArrayList<Arista> aristasVisitados) {
        StringBuilder recorrido = new StringBuilder();
        for (int i = 0; i < verticesVisitados.size(); i++) {
            Vertice nodo = verticesVisitados.get(i);
            recorrido.append(nodo.getID());
            if (i < verticesVisitados.size() - 1) {
                recorrido.append(" -> ");
            }
        }
        coloreaGrafo(verticesVisitados, aristasVisitados);
        return "[" + recorrido + "]";
    }

    /**
     * Colorea los vértices para tener una representación visual de los algoritmos.
     * @param verticesVisitados Un arreglo de vértices a colorear, se colorean en orden.
     * @param aristasVisitadas Un arreglo de aristas a colorear, se colorean en orden.
     */
    private void coloreaGrafo(ArrayList<Vertice> verticesVisitados, ArrayList<Arista> aristasVisitadas) {
        SwingWorker<Void, Object> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int espera = 500;
                // Colorea el vértice de azul, espera 1 segundo y lo colorea de verde.
                // Si hay una arista correspondiente, la colorea de amarillo y luego de verde.
                for (int i = 0; i < verticesVisitados.size(); i++) {
                    Vertice vertice = verticesVisitados.get(i);
                    vertice.setColorDeVertice(Color.decode("#FFFF70"));
                    publish(vertice);
                    Thread.sleep(espera);
                    vertice.setColorDeVertice(Color.decode("#25AD6B"));
                    publish(vertice);
                    Thread.sleep(espera);

                    if (i < aristasVisitadas.size()) {
                        Arista arista = aristasVisitadas.get(i);
                        arista.setColor(Color.decode("#FFFF70"));
                        publish(arista);
                        Thread.sleep(espera);
                        arista.setColor(Color.decode("#25AD6B"));
                        publish(arista);
                        Thread.sleep(espera);
                    }
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Object> chunks) {
                for (Object chunk : chunks) {
                    if (chunk instanceof Vertice vertice) {
                        vertice.repaint();
                    } else if (chunk instanceof Arista arista) {
                        arista.repaint();
                    }
                }
            }

            @Override
            protected void done() {
                for (Vertice vertice : verticesVisitados) {
                    vertice.setColorDeVertice(Color.decode("#25AD6B"));
                }
                for (Arista arista : aristasVisitadas) {
                    arista.setColor(Color.decode("#25AD6B"));
                }
            }
        };
        worker.execute();
    }

    /**
     * Devuelve los componentes a su color original.
     */
    public void descoloreaGrafo() {
        for (Vertice v: vertices) {
            v.setColorDeVertice(Color.decode("#ED94FF"));
        }
        for (Arista a :aristas) {
            a.setColor(Color.decode("#7094FF"));
        }
    }
}