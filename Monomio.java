public class Monomio implements Comparable<Monomio>{
    private final int coeff,grado;

    public Monomio(final int c, final int g) {
        if(g<0) throw new IllegalArgumentException();
        coeff=c; grado=g;
    }

    public Monomio(Monomio m) {
        coeff=m.coeff;
        grado=m.grado;
    }

    public int getCoeff() {
        return coeff;
    }

    public int getGrado() {
        return grado;
    }
    public Monomio add(Monomio m){
        if(!(this.equals(m))) throw new RuntimeException("Monomio di grado diverso");
        return new Monomio(m.coeff+coeff,grado);
    }
    public Monomio moltipl(Monomio m){
        return new Monomio(m.coeff*coeff,grado+m.grado);
    }
    public Monomio moltipl(int s){
        return new Monomio(coeff*s,grado);
    }

    @Override
    public int compareTo(Monomio m) {
        if(m.grado>grado) return 1;
        if(grado>m.grado) return -1;
        return 0;
    }
    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder(50);
        if(coeff==0) sb.append(0);
        else{
            if(coeff<0) sb.append('-');
            if(Math.abs(coeff)!=1 || grado==0) sb.append(Math.abs(coeff));
            if(grado>0)sb.append("x");
            if(grado>1) sb.append("^"+grado);
             
        }
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if(!(obj instanceof Monomio) ) return false;
        Monomio other = (Monomio) obj;
        return grado==other.grado;
    }
}
