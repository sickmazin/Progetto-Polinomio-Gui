import java.util.StringTokenizer;

public class RiconosciPolinomio {
    static Monomio primoMonomio( StringTokenizer st ) {
		int segno=1, coeff, grado=0;
		String tk=st.nextToken();

		if( tk.charAt(0)=='-' ) {
			segno=-1;
			tk=st.nextToken();
		}
		
		if( tk.matches("\\d+") ) {
			coeff=Integer.parseInt(tk);
		}
		else if( tk.matches("x(\\^\\d+)?") ) {
			coeff=1;
			int i=tk.indexOf('^');
			if( i>=0 ) 
				grado=Integer.parseInt( tk.substring(i+1) );
			else
				grado=1;
		}
		else {
			int i=tk.indexOf('x');
			coeff=Integer.parseInt( tk.substring(0,i) );
			i=tk.indexOf('^',i+1);
			if( i>=0 )
				grado=Integer.parseInt( tk.substring(i+1) );
			else
				grado=1;
		}
		coeff=coeff*segno;
		return new Monomio(coeff,grado);
	}//primoMonomio
	
	static Monomio prossimoMonomio( StringTokenizer st ) {
		int coeff, grado=0;
		int segno=st.nextToken().charAt(0)=='+'? 1 : -1;
		String tk=st.nextToken();
		if( tk.matches("\\d+") ) {
			coeff=Integer.parseInt(tk);
		}
		else if( tk.matches("x(\\^\\d+)?") ) {
			coeff=1;
			int i=tk.indexOf('^');
			if( i>=0 ) 
				grado=Integer.parseInt(tk.substring(i+1));
			else
				grado=1;
		}
		else {
			int i=tk.indexOf('x');
			coeff=Integer.parseInt( tk.substring(0,i) );
			i=tk.indexOf('^',i+1);
			if( i>=0 )
				grado=Integer.parseInt( tk.substring(i+1) );
			else
				grado=1;
		}
		coeff=coeff*segno;
		return new Monomio(coeff,grado);
	}//prossimoMonomio

	@SuppressWarnings("all")
	public static PolinomioSet riconosciPolinomio(String s) {
		PolinomioSet p=new PolinomioSet();	
		
		String MONOMIO="(\\d+|x(\\^\\d+)?|\\d+x(\\^\\d+)?)";
		String SEGNO="[\\+\\-]";
		String POLINOMIO="\\-?"+MONOMIO+"("+SEGNO+MONOMIO+")*";
        if( s.length()==0 ) throw new IllegalArgumentException();
		if( s.matches(POLINOMIO) ){
            StringTokenizer st=new StringTokenizer(s,"+-",true);
		    p.add( primoMonomio(st) );
		    while( st.hasMoreTokens() ) {
		    	p.add( prossimoMonomio(st) );
		    }
            
        }
        else throw new IllegalArgumentException();
        return p;
	}
}
