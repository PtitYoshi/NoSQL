package Request_Engine;

public class RDFTriplets {

	int s;
	int p;
	int o;
	
	public RDFTriplets(int ns, int np, int no) {
		// TODO Auto-generated constructor stub
		this.s = ns;
		this.p = np;
		this.o = no;
	}
	
	public int getS() { return s; }
	public int getP() { return p; }
	public int getO() { return o; }

	public String toString() {
		return "{" + s + "," + p + "," + o + "}";
	}

}
