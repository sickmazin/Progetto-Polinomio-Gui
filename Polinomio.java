import java.util.Iterator;

public interface Polinomio extends Iterable<Monomio>{
    
    @SuppressWarnings("unused")
    default int size(){
        int i=0;
        for(Monomio m: this){
            i++;
        }
        return i;
    }
    default void clear(){
        Iterator<Monomio> it= this.iterator();
        while(it.hasNext()){
            it.next();
            it.remove();
        }
    }
    Polinomio factory(); // Utilizzato per ottenere la collezzione che si utilizza 
    
    void add(Monomio m);
    
    default Polinomio add(Polinomio p){
        Polinomio somma= factory();
        for (Monomio monomio : this) {
            somma.add(monomio);
        }
        for (Monomio mo : p) {
            somma.add(mo);
        }
        return somma;
    }
	default Polinomio molt( Monomio m ) {
		Polinomio prodotto=factory();
		for( Monomio m1: this )
			prodotto.add( m.moltipl(m1) );
		return prodotto;
	}
	
	default Polinomio molt( Polinomio p ) {
		Polinomio prodotto=factory();
		for( Monomio m: this )
			prodotto=prodotto.add( p.molt(m) );
		return prodotto;
	}
    default Polinomio derivata(){
        Polinomio derivata= factory();
        for (Monomio m : this) {
            if(m.getGrado()==0) continue;
            Monomio q= new Monomio(m.getCoeff()*m.getGrado(),m.getGrado()-1);
            derivata.add(q);
        }
        return derivata;
    }
    default double valore(double x){ //polinomio definito come funzione do x ricevo y
        double v=0;
        for (Monomio m : this) {
            v+=m.getCoeff()*Math.pow(x,m.getGrado());
        }
        return v;
    }
}