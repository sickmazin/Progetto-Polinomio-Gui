import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PolinomioSet extends PolinomioAstratto{
    private Set<Monomio> set= new TreeSet<>();
    @Override
    public Polinomio factory() {
        return new PolinomioSet();
    }
    private Monomio get(Monomio m){
        for (Monomio monomio : set) {
            if(m.equals(monomio)) return monomio;
        }
        return null;
    }
    @Override
    public void add(Monomio m) {
        if(m.getCoeff()==0) return;
        if(set.contains(m)) {
            Monomio n=get(m);
            set.remove(m);
            set.add(m.add(n));
        }
        else{set.add(m);}
    }

    @Override
    public Iterator<Monomio> iterator() {
        return set.iterator();
    }
    public PolinomioSet copy() {
        PolinomioSet p= new PolinomioSet();
        for (Monomio m : this) {
            p.add(m);
        }
        return p;
    } 
}
