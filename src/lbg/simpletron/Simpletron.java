package lbg.simpletron;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
//machine language simulator
public class Simpletron {
	//operations
	final int READ = 10;
	final int SET = -10;
	final int WRITE = 11;
	final int WRITE_LETTER = 12;
	final int LOAD = 20;
	final int STORE = 21;
	final int ADD = 30;
	final int SUBTRACT = 31;
	final int DIVIDE = 32;
	final int MULTIPLY = 33;
	final int BRANCH = 40;
	final int BRANCHNEG = 41;
	final int BRANCHZERO = 42;
	final int HALT = 43;
	final int CHANGE = 44;
	final int BREAK = 45;
	final int OPERATE = 60;
	final int EQUALS = 70;
	//memory and access
	protected int[] allocations;
	protected int accumulator;
	private InputStream inputSource;
	private Scanner reader;
	private Scanner in;
	private int allocation_block;
	private int instruction_counter;
	//regex
	private Pattern PAT;
	final String COMMENT = "//\\w*|//(\\w*\\s*)+";
	//conditions & loops
	private boolean hasCondition;
	private boolean isTrueCondition;
	
	
	public void init (int allocation_memory) {
		allocations = new int[allocation_memory];
		accumulator = 0;
		allocation_block = 0;
		instruction_counter = 1;
		PAT = Pattern.compile(COMMENT);
		System.out.println("Simpletron Machine Language © 2016");
	}
	
	public void start (InputStream in) {
		System.out.println("Simpletron Machine is executed");
		inputSource = in;
		reader = new Scanner (inputSource);
		int location = findFreeMemory(allocations);
		showMemoryLocation(location);
		while (reader.hasNext()) {
			skipComments();
			int instruction = reader.nextInt();
			allocations[location] = instruction;
			this.process(allocations[location]);
			instruction_counter += 1;
			location = findFreeMemory(allocations);
			showMemoryLocation(location);
		}
		
	}
	
	public void runFile (InputStream in) {
		System.out.println("Simpletron Machine file is executed");
		inputSource = in;
		reader = new Scanner (inputSource);
		int location = findFreeMemory(allocations);
		while (reader.hasNext()) {
			skipComments();
			int instruction = reader.nextInt();
			allocations[location] = instruction;
			this.process(allocations[location]);
			instruction_counter += 1;
			location = findFreeMemory(allocations);
		}
		
	}
	
	private void process (int instruction) {
		
		int func = instruction / 100;
		int loc = instruction % 100;
		loc = Math.abs(loc);
		
		if (hasCondition && !(isTrueCondition) && func != 45) {
	    	//System.out.printf("Skipping Condition %d at %d\n",func,loc);
			return;
		}
		
		
		switch(func) {
		case READ: read(getLoc(loc));break;
		case WRITE: write(getLoc(loc));break;
		case LOAD: load(getLoc(loc));break;
		case STORE: store(getLoc(loc));break;
		case ADD: add(getLoc(loc));break;
		case SUBTRACT: subtract(getLoc(loc));break;
		case MULTIPLY: multiply(getLoc(loc));break;
		case DIVIDE: divide(getLoc(loc));break;
		case HALT: halt();break;
		case OPERATE: process(getLoc(loc));break;
		case BRANCH: branch(getLoc(loc));break;
		case BRANCHNEG: branchNeg(getLoc(loc));break;
		case BRANCHZERO: branchZero(getLoc(loc));break;
		case WRITE_LETTER: writeLetter(getLoc(loc));break;
		case CHANGE: change(loc);break;
		case SET: set(getLoc(loc));break;
		case BREAK: breakCondition();break;
		default: System.err.println("Error:unknown operation");break;
		}
		
		
		
	}
	
	private void skipComments () {
		if (reader.hasNext(PAT)) {
			reader.nextLine();
			}
	}
	
	private int findFreeMemory (int[] allocations) {
		int memory_location = -1;
		for (int i = 0; i < allocations.length;i++) {
			if (allocations[i] == 0) {
				memory_location = i;
				break;
			}
		}
		

		return memory_location;
	}
	
	private void showMemoryLocation (int location) {
		if (location < 10) {
			System.out.printf("0%d. ",location);
		}else {
			System.out.printf("%d. ",location);
		}
	}
	
	//SML Processes
	private void  read (int location) {
		int l = location;
		in = new Scanner(System.in);
		int value  = in.nextInt();
		allocations[l] = value;
	}
	
	private void  set (int location) {
		int l = location;
		int value  = reader.nextInt();
		allocations[l] = value;
	}
	
	private void  write (int location) {
		int l = location;
        System.out.println(allocations[l]);
	}
	
	private void load (int location) {
		int l = location;
		accumulator = allocations[l];
	}
	
	private void store (int location) {
		int l = location;
		allocations[l] = accumulator;
	}
	
	private void add (int location) {
		int l = location;
		accumulator = accumulator + allocations[l];
	}
	
	private void subtract (int location) {
		int l = location;
		accumulator = accumulator - allocations[l];
	}
	
	private void multiply (int location) {
		int l = location;
		accumulator = accumulator + allocations[l];
	}
	
	private void divide (int location) {
		int l = location;
		accumulator = Math.round((accumulator / allocations[l]));
	}
	
	private void halt () {
		System.out.printf("\nSimplteron Machine is terminated\nTotal of %d instructions have been executed",instruction_counter);
		System.exit(0);
	}
	
	private void branchZero (int location) {
		int l = location;
		
		hasCondition = true;
		
		if (allocations[l] == 0) {
			isTrueCondition = true;
	   }else {
		isTrueCondition = false;
	  }
		
	 	
	}
	
	private void branch (int location) {
		int l = location;
		
		hasCondition = true;
		
		if (allocations[l] > 0) {
				isTrueCondition = true;
		}else {
			isTrueCondition = false;
		
		}
			
	}
	
	private void branchNeg (int location) {
		int l = location;
		
		hasCondition = true;

 		if (allocations[l] < 0) {
 			isTrueCondition = true;
	   }else {
		   isTrueCondition = false;
	   
	   }
		
			
	}
	
	private void writeLetter (int location) {
		int l = location;
		if (allocations[l] == 128) {
			System.out.println();
		}else {
		System.out.print((char) allocations[l]);
		}
		
		}
	
	private void change (int location) {
		int l = location;
		allocation_block = l;
	}
	
	private void  breakCondition () {
		hasCondition = isTrueCondition = false;
	}
	
	public int getLoc (int l) {
		return l + (100*allocation_block);
	}
		

   public static void trace (InputStream in) {
	   
	   Scanner s = new Scanner (in);
	   
	   while(s.hasNextInt()) {
		   int i = s.nextInt();
		   System.out.println(i);
	   }
	   
   }


}
