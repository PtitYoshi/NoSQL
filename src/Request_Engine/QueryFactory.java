package Request_Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QueryFactory {
	
	public ArrayList<Query> createFromFile(String file) throws IOException
	{
		ArrayList<Query> queries = new ArrayList<Query>();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		while((line = br.readLine())!=null)			//Chaque ligne est sous la forme SELECT ?x WHERE {}
													//Peut s'étendre sur plusieurs lignes.
		{
			String select="";
			ArrayList<String> where = new ArrayList<String>();
			String tmpline="";
			
			while(!line.contains("}")&&tmpline!=null)				//Recuperation d'une requête
			{
				tmpline=br.readLine();
				line+=tmpline;
			}
			if(tmpline==null)
				break;
			
			//System.out.println("ligne contient : "+line);
			
			line = line.toLowerCase();
			//System.out.println(line+" ENDLINE\n");
			if(line.contains("select"))				//Extraction du select
			{
				select = line.substring(line.indexOf("select")+6, line.indexOf("where")).replaceAll(" ", "");
				//System.out.println("{"+select+"}\n");
			}
			
			if(line.contains("where"))				//Extraction du where
			{
				 String tmpwhere = line.substring(line.indexOf("where")+5, line.indexOf("}")).replaceAll("[{}	]+", "");
				 String tmpstring = "";
				 //System.out.println("{"+tmpwhere+"}\n");
				 while(!tmpwhere.isEmpty())
				 {
					 //System.out.println("AGAIN : {"+tmpwhere+"}\n");
					 if(!tmpwhere.startsWith(" "))
					 {
						 //System.out.println("PASS");
						 tmpstring += tmpwhere.substring(0,tmpwhere.indexOf(" ")+1);
						 tmpwhere = tmpwhere.replace(tmpwhere.substring(0,tmpwhere.indexOf(" ")), "");
					 }
					 else
					 {
						 tmpwhere = tmpwhere.replaceFirst(" ", "");
						 if(tmpwhere.startsWith("."))
						 {
							 where.add(tmpstring+".");
							 tmpstring="";
							 tmpwhere = tmpwhere.replaceFirst(".", "");
						 }
					 }
				 }
			}
			
			//System.out.println(where);
			queries.add(new Query(select, where));
		}
		
		//System.out.println("FINISH");
		
		br.close();
		
		return queries;
	}
	/*
	public static void main(String[] args) throws IOException
	{
		ArrayList<Query> queries = createFromFile("Q_1_includes.queryset");
		for(int i=0;i<queries.size();i++)
		{
			System.out.println(queries.get(i).toString());
		}
	}*/
}
