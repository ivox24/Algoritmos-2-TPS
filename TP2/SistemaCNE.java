package aed;

import aed.Tripla;
import aed.Heap;

public class SistemaCNE {
    private String[] nombresPartidos;
    private String[] nombresDistritos;
    private int[] diputadosPorDistrito;
    private int[] ultimasMesasDistritos;
    private int[] votosAPresidente;
    private int[][] votosADiputados;
    private int[] votosTotalesPorDistrito;
    private Heap[] heapsDiputados;
    private int votosTotalesPresidenciales;
    private int[] votosPartidosMasVotados;
    private int[][] resultadosDiputadosArray;
    private boolean[] bancasRepartidasPorDistrito;

    /*
     * Donde P es la cantidad de partidos y D la cantidad de distritos tenemos que:
     * P, D > 0
     * 
     * InvRep:
     * 
     * |nombresDistritos| = D
     * |diputadosPorDistrito| = D
     * |ultimasMesasDistritos| = D
     * Los votos de los partidos en votosADiputados no pueden ser negativos y |votosADiputados| = D
     * |nombresPartidos| = P
     * Los votos de los partidos en votosAPresidente no pueden ser negativos y |votosAPresidente| = P
     * Para todo idDistrito indice de votosADiputados, |votosADiputados[idDistrito]| = P 
     * Para todo idDistrito indice de diputadosPorDistrito, |diputadosPorDistrito[idDistrito]| = |resultadosDiputadosArray|
     * El ultimo elemento de nombresPartidos son siempre los votos en blanco
     * votosTotalesPorDistrito[i] es el total de votos en el distrito i.
     * votosTotalesPresidenciales es la suma de los votosAPresidente
     * votosPartidosMasVotados[0] contiene los votos del partido mas votado y votosPartidosMasVotados[1] contiene los votos del segundo partido mas votado para cargo presidente
     * |bancasRepartidasPorDistrito| = D
     * bancasRepartidasPorDistrito[i] es true <==> se ejecutó la función resultadosDiputados previamente sin ninguna llamada a la función registrarMesa en el medio. bancasRepartidasPorDistrito[i] es false en caso contrario
     * |heapsDiputados| = D
     * En heapsDiputados[i] se tiene un maxHeapArray que representan los votos que sacaron los partidos en el distrito i
     */

    public class VotosPartido{
        private int presidente;
        private int diputados;

        VotosPartido(int presidente, int diputados){this.presidente = presidente; this.diputados = diputados;}
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}

    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        
        // Inicializo los atributos del objeto
        this.nombresDistritos = new String[nombresDistritos.length]; // O(D)
        this.diputadosPorDistrito = new int[diputadosPorDistrito.length]; // O(D)
        this.nombresPartidos = new String[nombresPartidos.length]; // O(P)
        this.ultimasMesasDistritos = new int[ultimasMesasDistritos.length]; // O(D)
        this.votosAPresidente = new int[nombresPartidos.length]; // O(P)
        this.votosADiputados = new int[nombresDistritos.length][nombresPartidos.length]; // O(D + P)
        this.resultadosDiputadosArray = new int[nombresDistritos.length][nombresPartidos.length]; // O(D + P)
        this.bancasRepartidasPorDistrito = new boolean[nombresDistritos.length]; // O(D)

        this.nombresPartidos[nombresPartidos.length - 1] = "Blanco"; // O(1)

        for(int i = 0; i < nombresPartidos.length - 1; i++) { // O(P)
            this.nombresPartidos[i] = nombresPartidos[i]; // O(1)
        }

        this.ultimasMesasDistritos = ultimasMesasDistritos; // O(1)
        this.diputadosPorDistrito = diputadosPorDistrito; // O(1)
        this.nombresDistritos = nombresDistritos; // O(1)

        this.heapsDiputados = new Heap[nombresDistritos.length]; // O(D)
        this.votosTotalesPorDistrito = new int[nombresDistritos.length]; // O(D)

        this.votosTotalesPresidenciales = 0; // O(1)
        this.votosPartidosMasVotados = new int[] {0, 0}; // O(1)
    }

    public String nombrePartido(int idPartido) {
        return nombresPartidos[idPartido]; // O(1)
    }

    public String nombreDistrito(int idDistrito) {
        return nombresDistritos[idDistrito]; // O(1)
    }

    public int diputadosEnDisputa(int idDistrito) {
        return diputadosPorDistrito[idDistrito];
    }

    public String distritoDeMesa(int idMesa) {
        int idDistrito = this.idDistritoDeMesa(idMesa);

        return this.nombresDistritos[idDistrito];
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) { //  O(log(D) + P)
        int idDistrito = idDistritoDeMesa(idMesa);// O(log (D))
        int votosAPresidente = 0; // O(1)
        int[] votosPartidosMasVotados = {0, 0}; // O(1)
        int votosTotalesPorDistrito = 0; // O(1)

        // Sumamos votos
        for (int i = 0; i < actaMesa.length ; i++) { //O(P)
            this.votosAPresidente[i] += actaMesa[i].votosPresidente(); // O(1)
            this.votosADiputados[idDistrito][i] += actaMesa[i].votosDiputados(); // O(1)

            votosAPresidente += this.votosAPresidente[i]; // O(1)
            votosTotalesPorDistrito += this.votosADiputados[idDistrito][i]; // O(1)

            if(this.votosAPresidente[i] >= votosPartidosMasVotados[0]) { // O(1)
                votosPartidosMasVotados[1] = votosPartidosMasVotados[0]; // O(1)
                votosPartidosMasVotados[0] = this.votosAPresidente[i]; // O(1)
            }
            else if (
                this.votosAPresidente[i] >= votosPartidosMasVotados[1] // O(1)
            ) {
                votosPartidosMasVotados[1] = this.votosAPresidente[i]; // O(1)
            }
        }

        // Actualizamos los votos totales en los atributos de la clase
        this.votosTotalesPresidenciales = votosAPresidente; // O(1)
        this.votosPartidosMasVotados = votosPartidosMasVotados; // O(1)
        this.votosTotalesPorDistrito[idDistrito] = votosTotalesPorDistrito; // O(1)


        // Instanciamos un array con triplas
        Tripla[] arrayTriplasDiputados = new Tripla[this.nombresPartidos.length - 1]; // O(P)
        int umbral = 3 * votosTotalesPorDistrito / 100; // O(1)

        for (int i = 0; i < actaMesa.length - 1; i++){ // O(P)
            arrayTriplasDiputados[i] = new Tripla(
                this.votosADiputados[idDistrito][i] > umbral ? this.votosADiputados[idDistrito][i] : 0,
                i,
                1
            ); // O(1)
        }

        Heap heap = new Heap(arrayTriplasDiputados); // O(P)
        this.heapsDiputados[idDistrito] = heap; // O(1)
        this.bancasRepartidasPorDistrito[idDistrito] = false; // O(1)
    }

    public int idDistritoDeMesa(int idMesa) {  // O(log(D))
        int inicio = 0;
        int fin = ultimasMesasDistritos.length - 1;

        while(inicio <= fin) { // O(log (D))
            int i = (inicio + fin) / 2;
            
            if(inicio == fin) {
                return inicio;
            }
            else if(ultimasMesasDistritos[i] <= idMesa) {
                if(idMesa < ultimasMesasDistritos[i + 1]) {
                    return i + 1;
                }
                inicio = i;
            }
            else {
                fin = i;
            }
        }

        return 0; //acá no entra nunca
    }

    public int votosPresidenciales(int idPartido) {
        return votosAPresidente[idPartido]; // O(1)
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return votosADiputados[idDistrito][idPartido]; // O(1)
    }

    public int[] resultadosDiputados(int idDistrito){ // Debería tener complejidad: O(Dd * log(P))

        if(this.bancasRepartidasPorDistrito[idDistrito]) { // O(1)
            return this.resultadosDiputadosArray[idDistrito]; // O(1)
        }
        
        Heap heapDistrito = this.heapsDiputados[idDistrito]; // O(1)

        for(int i = 0; i < diputadosPorDistrito[idDistrito]; i ++) { // O(Dd)
            Tripla votosPartido = heapDistrito.reencolarYDevolverTripla(); // O(log P)
            this.resultadosDiputadosArray[idDistrito][votosPartido.idPartido] += 1; // O(1)
        }

        this.bancasRepartidasPorDistrito[idDistrito] = true; // O(1)
        return this.resultadosDiputadosArray[idDistrito]; // O(1)
    }

    public boolean hayBallotage(){ // O(1)
        double porcentajePrimero = this.porcentajeDeVotos(this.votosPartidosMasVotados[0]); // O(1)
        double porcentajeSegundo = this.porcentajeDeVotos(this.votosPartidosMasVotados[1]); // O(1)
        double diferenciaCandidatos = porcentajePrimero - porcentajeSegundo; // O(1)

        return !(porcentajePrimero >= 45 || (porcentajePrimero >= 40 && diferenciaCandidatos >= 10)); // O(1)
    }

    private double porcentajeDeVotos(int votosPartido) { // O(1)
        return ((double) votosPartido / (double) this.votosTotalesPresidenciales) * 100;
    }

}