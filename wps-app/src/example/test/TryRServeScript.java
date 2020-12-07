package example.test;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class TryRServeScript {
	public static void main(String[] args) {
		RConnection connection;
		try {
			connection = new RConnection();
			connection.eval("source('C:/Users/ezzjfr/Documents/lucas_git/landsenseR/rserve_examples/myAdd.R')");
			int num1=10;
			int num2=20;
			int sum=connection.eval("myAdd("+num1+","+num2+")").asInteger();
			System.out.println("The sum is=" + sum);
		} catch (RserveException e) {
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			e.printStackTrace();
		}
	}
}
