//I have neither given nor received any unauthorized aid on this assignment
import java.io.*;
import java.util.Set;
import java.util.*;

public class MIPSsim {
	
	String st = new String();

	String [] instr = new String[4];
	String [] reg = new String[2];
	String [] datamem = new String[2];
	
	List<String> instructions = new ArrayList<>();
	//Set<String> registers = new LinkedHashSet<String>();
	Map<Integer,Integer> rgfMap = new TreeMap<>();
	Map<Integer,Integer> dataMap = new TreeMap<>();
	
	MIPSsim() throws Exception{
	File fileINM = new File("instructions.txt");	 
	BufferedReader br = new BufferedReader(new FileReader(fileINM));
	while ((st = br.readLine()) != null)
		instructions.add(st);
	br.close();
	
	File fileRGF = new File("registers.txt");
	br = new BufferedReader(new FileReader(fileRGF));
	
	while ((st = br.readLine()) != null)
	{
		//registers.add(st);
		st = st.replaceAll(","," ");
		st = st.replaceAll("<"," ");
		st = st.replaceAll(">"," ");
		instr = st.split(" ");
		rgfMap.put(Integer.parseInt(instr[1].substring(1)), Integer.parseInt(instr[2]));
	}
	br.close();
	
	File fileDAM = new File("datamemory.txt");
	br = new BufferedReader(new FileReader(fileDAM));
	while ((st = br.readLine()) != null)
	{
		st = st.replaceAll(","," ");
		st = st.replaceAll("<"," ");
		st = st.replaceAll(">"," ");
		instr = st.split(" ");
		dataMap.put(Integer.parseInt(instr[1]),Integer.parseInt(instr[2]));
	}
	br.close();
	} //Close Constructor
	
	
	String inb = new String();
	String sib = new String();
	String aib = new String();
	String adb = new String();
	String [] reb = {"",""};
	String prb = new String();
	Integer result = 0;
	
	// Function to check if all buffers are empty
	boolean bufferEmpty() 
	{
		if(inb.equals("") && sib.equals("") && adb.equals("") && aib.equals("") && prb.equals("") && reb[0].equals(""))
			return true;
		else
			return false;
		
	}
	
	//Function to store from ADB to DAM
	void adbTOdam()
	{
		if(adb.equals("")==false)
		{
			st = adb;
			st = st.replaceAll(","," ");
			st = st.replaceAll("<"," ");
			st = st.replaceAll(">"," ");
			instr = st.split(" ");
			dataMap.put(Integer.parseInt(instr[2]),rgfMap.get(Integer.parseInt(instr[1].substring(1))));
		}
	}
	
	//Function to store from REB to RGF
	void rebTOrgf()
	{
		if(reb[0].equals("")==false){
			
			st = reb[0];
			st = st.replaceAll(","," ");
			st = st.replaceAll("<"," ");
			st = st.replaceAll(">"," ");
			instr = st.split(" ");
			rgfMap.put(Integer.parseInt(instr[1].substring(1)),Integer.parseInt(instr[2]));
			reb[0]=reb[1];
			reb[1]="";
		} 
	}
	
	//Function to store from AIB to PRB or REB
	void aibTOprbORreb()
	{
		
		if(prb.equals("")==false)
		{
			
			st = prb;
			st = st.replaceAll(","," ");
			st = st.replaceAll("<"," ");
			st = st.replaceAll(">"," ");
			instr = st.split(" ");
			result = Integer.parseInt(instr[3])*Integer.parseInt(instr[4]);
			reb[0] = "<" + instr[2] + "," + result.toString() + ">";
			
			if(aib.equals("")==false)
			{
				
				
				if(aib.contains("MUL"))
					prb = aib;
				else if(aib.contains("ADD"))
				{
					prb = "";
					st = aib;
					st = st.replaceAll(","," ");
					st = st.replaceAll("<"," ");
					st = st.replaceAll(">"," ");
					instr = st.split(" ");
					result = Integer.parseInt(instr[3].substring(1))+Integer.parseInt(instr[4]);
					reb[1]=reb[0];
					reb[1] = "<" + instr[2] + "," + result.toString() + ">";
				}
				else if(aib.contains("SUB"))
				{
					prb = "";
					st = aib;
					st = st.replaceAll(","," ");
					st = st.replaceAll("<"," ");
					st = st.replaceAll(">"," ");
					instr = st.split(" ");
					//.out.println(instr[3] + " --- " + instr[4]);
					result = Integer.parseInt(instr[3])-Integer.parseInt(instr[4]);
					reb[1]=reb[0];
					reb[1] = "<" + instr[2] + "," + result.toString() + ">";
				}
			}
			else
			{
				prb = "";
			}
		}
		else
		{
			if(aib.equals("")==false)
			{
				
				
				if(aib.contains("MUL"))
				{
					prb = aib;
					reb[0] = reb[1];
					reb[1] = "";
				}
				else if(aib.contains("ADD"))
				{
					st = aib;
					st = st.replaceAll(","," ");
					st = st.replaceAll("<"," ");
					st = st.replaceAll(">"," ");
					instr = st.split(" ");
					
					result = Integer.parseInt(instr[3])+Integer.parseInt(instr[4]);
					reb[0] = "<" + instr[2] + "," + result.toString() + ">";
				}
				else if(aib.contains("SUB"))
				{
					st = aib;
					st = st.replaceAll(","," ");
					st = st.replaceAll("<"," ");
					st = st.replaceAll(">"," ");
					instr = st.split(" ");
					result = Integer.parseInt(instr[3])-Integer.parseInt(instr[4]);
					reb[0] = "<" + instr[2] + "," + result.toString() + ">";
				}	
			}
		}
	}

	//Function to store from SIB to ADB
	void sibTOadb()
	{
		if(sib.equals("")==false) 
		{	st = sib;
			st = st.replaceAll(","," ");
			st = st.replaceAll("<"," ");
			st = st.replaceAll(">"," ");
			instr = st.split(" ");
			//System.out.println(instr[3] + "----" + instr[4]);
			result = Integer.parseInt(instr[3])+Integer.parseInt(instr[4]);
			adb = "<" + instr[2] + "," + result.toString() + ">";
		}
		else
			adb="";
	}
	
	//Function to store from INB to SIB or AIB
	void inbTOsibORaib() 
	{
		if(inb.contains("ST"))
		{
			sib = inb;
			aib = "";
		}
		else
		{
			aib = inb;
			sib = "";
		}
	}
	
	//Function to store from INM to INB
	void inmTOinb()
	{
		//Store first instruction in the String st amd remove instrution from INM
		if(instructions.isEmpty()==false) 
		{
			st = instructions.iterator().next();
			instructions.remove(st);
		
			//Replace every other char with a space
			st = st.replaceAll(","," ");
			st = st.replaceAll("<"," ");
			st = st.replaceAll(">"," ");
			instr = st.split(" ");
		
			if(instr[1].equals("ST"))
				inb = "<" + instr[1]+ "," +instr[2] + "," + rgfMap.get(Integer.parseInt(instr[3].substring(1))) + "," + instr[4] + ">";
			else
				inb = "<" + instr[1]+ "," +instr[2] + "," + rgfMap.get(Integer.parseInt(instr[3].substring(1))) + "," + rgfMap.get(Integer.parseInt(instr[4].substring(1))) + ">";
		}
		else 
			inb = "";
	}
	

	
	// Function for Buffer Transition
	void buff() 
	{
		//Store from REB to RGF
		rebTOrgf();
		//Store from ADB to DAM
		adbTOdam();
		//Store from AIB to PRB or REB
		aibTOprbORreb();
		//Store from SIB to ADB
		sibTOadb();
		//Store from INB to SIB or AIB
		inbTOsibORaib();
		//Store from INM to INB
		inmTOinb();
	}
	
	//Function to display output
	int count = 0;
	int mapIterator=0;
	
	
	void disp()
	{
		if(count!=0) System.out.println("\n");
		System.out.print("STEP " + count +":");
		
		
		mapIterator=0;
		System.out.print("\nINM:");
		for(String iterator : instructions) {
			System.out.print(iterator);
			if(mapIterator<instructions.size()-1) System.out.print(",");
			mapIterator++;}
				
		System.out.print("\nINB:"+inb +"\nAIB:"+aib +"\nSIB:"+sib+"\nPRB:"+prb +"\nADB:"+adb  +"\nREB:"+reb[0]);
		if(reb[1].equals("")==false)
			System.out.print(","+ reb[1] );
		
		mapIterator=0;
		System.out.print("\nRGF:");
		for (Map.Entry<Integer, Integer> m:rgfMap.entrySet()) {
			   		System.out.print("<"+ "R" + m.getKey()+","+m.getValue()+">");
			   		if(mapIterator<rgfMap.size()-1)  System.out.print(",");
			   		mapIterator++;}
		
		mapIterator=0;
		System.out.print("\nDAM:");
		for (Map.Entry<Integer, Integer> m:dataMap.entrySet()) {
			   		System.out.print("<"+m.getKey()+","+m.getValue()+">");
			   		if(mapIterator<dataMap.size()-1)  System.out.print(",");
			   		mapIterator++;}
		
		count++;
			   		
	}

	
	public static void main(String[] args) throws Exception {
		
		
		MIPSsim obj = new MIPSsim();
		
		PrintStream out = new PrintStream(new FileOutputStream("simulation.txt"));
		System.setOut(out);
		
		obj.disp();
		while(obj.bufferEmpty()==false||obj.instructions.isEmpty()==false) {
			
			obj.buff();
			obj.disp();
		}
		
		out.close();
				
	}
}
