package lbg.simpletron;

import java.io.*;
//run the simpletron machine
public class Computer {
	
	private static Simpletron simpletron;
	private static InputStream fileSource;

	public static void main (String args[]) {
		
		simpletron = new Simpletron();
		
		File f = new File(System.getenv("JAVADAT")+"//math.sml");
		System.out.println(f.getPath());
		
		try {
			fileSource = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		simpletron.init(500);
		simpletron.runFile(fileSource);
		//use simpletron start method to run dynamically 
	}

}
