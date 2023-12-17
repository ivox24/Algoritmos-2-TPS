package aed;

public class Tripla {
    public int votos;
    public int idPartido;
    public int divisor;
    public int cociente;

    public Tripla(int votos, int idPartido, int divisor) {
        this.votos = votos;
        this.idPartido = idPartido;
        this.divisor = divisor;
        this.cociente = votos / divisor;
    }

    public void sumarDivisor() {
        this.divisor++;
        this.cociente = this.votos / this.divisor;
    }

    public int compareTo(Tripla otraTripla) {
        if(this.cociente > (otraTripla.cociente)) {
            return 1;
        }
        else if (this.cociente < (otraTripla.cociente)) {
            return -1;
        }
        return 0;
    }
}
