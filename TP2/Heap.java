package aed;

public class Heap {
    private Tripla[] heap; // array que almacena el heap
    private int size; // tamaño actual del heap
    private int capacidad; // Capacidad máxima del heap

    public Heap(Tripla[] triplas){ //Constructor de Heap que recibe una tripla (array, partido, divisor)

        this.capacidad = triplas.length; // Asignar la capacidad
        this.size = 0; // Inicializar el tamaño
        this.heap = triplas; // Crear el arreglo
        heapificar(triplas);
    }

    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int hijoIzq(int i) {
        return 2 * i + 1;
    }

    private int hijoDer(int i) {
        return 2 * i + 2;
    }

    private void swap(int indice, int padre) { //Cambia los valores de las posiciones
        Tripla tmp = heap[indice]; // O(1)
        heap[indice] = heap[padre]; // O(1)
        heap[padre] = tmp; // O(1)
    }

    private void heapifyUp(int indice) { // Cada vez que encolemos algo al final queremos que si es mayor vaya subiendo.
        while (indice > 0 && heap[indice].compareTo(heap[padre(indice)]) == 1) { // O(log(P))
            swap(indice, padre(indice));
            indice = padre(indice);
        }
    }

    private void heapifyDown(int indice) { // Despues de desencolar la raiz tenemos que rebalancear el arbol
        while (true) { // O(log(P))
            int izq = hijoIzq(indice);
            int der = hijoDer(indice);
            int nuevoPadre = indice;

            if (izq < size
            && heap[izq].compareTo(heap[nuevoPadre]) == 1
            ) {
                nuevoPadre = izq;
            }

            if (der < size
            && heap[der].compareTo(heap[nuevoPadre]) == 1
            ) {
                nuevoPadre = der;
            }

            /*
             * Si hay izq && izq > indice =>
             *  - Si hay der y der > izq => cambiar indice por derecho
             *  - Else: cambiar indice por izq
             * 
             * Si el izquierdo es más grande que el indice y el derecho es más grande que el izquierdo entonces cambiar indice por derecho
             * Si el izquierdo es más grande que el índice y el derecho no es más grande que el izquierdo entonces cambiar indice por izquierdo
             * Si el izquierdo no es más grande que el indice y el derecho es más grande que el indice entonces cambiar indice por derecho 
             * 
             * Si no hay izq && der > indice =>
             * - Cambiar indice por derecho
             * 
             * Cambiar indice por el padre
             * 
             * Si no hay ni der ni izq => break
             */


            if (nuevoPadre != indice) { //Si se hicieron cambios de indice, hacer el swap
                swap(indice, nuevoPadre);
                indice = nuevoPadre;
            }
            else { 
                break; // Cuando ya este ordenado va a parar
            }
        }
    }

    private void heapificar(Tripla[] triplas){ // O(P)
        size = triplas.length;
        int inicio = (size / 2) - 1;

        while(0 <= inicio){
            heapifyDown(inicio);
            inicio--;
        }
    }

    public void encolar(Tripla elem) { // O(log(P))
        if(size < capacidad){
            size ++;
            heap[size - 1] = elem; // El indice es el tamaño actual
            heapifyUp(size - 1);
        }
        else {
            return; // El heap está lleno. No se puede encolar más elementos.
        }
    } 


    public Tripla desencolar() { // O(log(P))
        if(0 < size){
            Tripla max = heap[0];
            heap[0] = heap[size - 1]; //subimos el ultimo
            size--;
            heapifyDown(0);
            return max;
        }
        else {
            return null; // El heap está vacío. No se puede desencolar.
        }
    }

    public Tripla reencolarYDevolverTripla() { // O(O log (P))
        Tripla max = heap[0];
        heap[0].sumarDivisor();
        heapifyDown(0);
        return max;
    }
}
