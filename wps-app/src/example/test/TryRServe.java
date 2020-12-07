package example.test;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

// ... add additional imports here ...

public class TryRServe {
  public static void main(String[] args) throws Exception {
   
	  RConnection c = new RConnection();
	  REXP x = c.eval("R.version.string");
	  System.out.println(x.asString()); 
	  	 
  }
  

}