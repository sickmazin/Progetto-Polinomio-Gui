import java.util.Iterator;

public abstract class PolinomioAstratto implements Polinomio{
    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder (200);
        boolean flag=true;
        for ( Monomio m: this ) {
            if(flag) flag=!flag;
            else{
                if(m.getCoeff()>0) sb.append("+");
            }
            sb.append(m);
        }
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Polinomio)) return false;
        if(this==obj) return true;
        Polinomio p=(Polinomio) obj;
        if(p.size()!=size()) return false;
        Iterator<Monomio> i1= this.iterator(), i2= p.iterator();
        while(i1.hasNext()){
            Monomio m1= i1.next(), m2=i2.next();
            if(m1.getCoeff()!=m2.getCoeff() || !m1.equals(m2)) return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        final int M=83;
        int h=0;
        for (Monomio m : this) {
            h*=M+ (String.valueOf(m.getCoeff()+m.getGrado())).hashCode();
        }
        return h;
}
}
