package TP3;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;


public class Commune {

	public Commune() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException
	{
		// Construction de la BD
		Configuration hc = HBaseConfiguration.create();
		HTableDescriptor ht = new HTableDescriptor( "patient" ); //Nom de la table
		ht.addFamily( new HColumnDescriptor( "allergy" ) ); //Nom d'une colonne
		
		//Insertion de données
		Put pierrePut = new Put(new String("P_M_001").getBytes()); //Un put
		
		//Ces lignes equivalent à : put "patient", "P_M_001", "allergy:sneezing", "mild"
		pierrePut.add(new String("allergy").getBytes(),
		new String("sneezing").getBytes(),
		new String("mild").getBytes());
		
		//Passage en mode admin
		HBaseAdmin hba = new HBaseAdmin( hc );
		
		//drop table si elle existe
		hba.disableTable(ht.getNameAsString());
		hba.deleteTable(ht.getNameAsString());
		
		
		System.out.println( "creating table...patient " );
		hba.createTable( ht );
		HTable table = new HTable(hc, "patient");
		
		System.out.println( "creating row...Pierre " );
		table.put(pierrePut);
		
		//Affichage de la table
		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);
		System.out.println("Scanning table... ");
		for (Result result : scanner)								//Pour chaque élément "Result" de l'objet scanner (qui est un ResultScanner)
		{
			for (KeyValue kv : result.raw())						//Les valeurs sont retournées en format lignes
			{
				System.out.println("kv:"+kv +", Key: " +
				Bytes.toString(kv.getRow())							//Les valeurs sont de la classe Bytes
				+ ", Value: " +Bytes.toString(kv.getValue()));
			}
		}
		scanner.close();
	}
		
		/*
		 * FilterList fl = new FilterList( FilterList.Operator.
MUST_PASS_ALL, filters);
Scan scan = new Scan();
scan.setFilter(fl);
ResultScanner scanner = table.getScanner(scan);
System.out.println("Scanning table... ");
for (Result result : scanner) {
for (KeyValue kv : result.raw()) {
System.out.println("kv:"+kv +", Key: " +
Bytes.toString(kv.getRow())
+ ", Value: " +Bytes.toString(kv.getValue()));
}
}
scanner.close(); }*/

}
