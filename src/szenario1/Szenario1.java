package szenario1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Szenario1 {
	final static int[] FILEAMOUNT = { 0, 1, 5, 10, 100, 1000, 10000 };
	final static String GSUTILPATH ="/home/coxtor/.opt/google-cloud-sdk/bin/gsutil";
	final static String[] FILENAME = {	"0Byte", "1Byte", "512Byte", "1KB", "512KB", "1MB"};//, "512MB", "1GB", "4GB" };
	final static String BASEPATH = System.getProperty("user.dir") + "/../data/";
	public static void main(String[] args) throws IOException {
	BufferedWriter output;
	output = new BufferedWriter(new FileWriter("GroundtruthData.txt", false));
	String dataToWrite = "";
	for(int i : FILEAMOUNT){
		dataToWrite += "Anticipated time Ms " + i + " , "; 
	};	
	
	output.write("Filesize , Elapsed Time Ms, " + dataToWrite);
	output.newLine();
	output.close();
		System.out.println("Transfer started");
		for(String file: FILENAME){
			String command = GSUTILPATH +" cp " + BASEPATH + file + " gs://iot-test_hda ";
			LinuxInteractor interactor = new LinuxInteractor();
			String result = interactor.executeCommand(command, true);
			//System.out.println("Command: "+ command);
			//System.out.println("result: "+ result);
			System.out.println("Transferred " + file + " in " +  interactor.elapsedTime + " Milliseconds");	
			output = new BufferedWriter(new FileWriter("GroundtruthData.txt", true));
			dataToWrite = "";
			for(int i : FILEAMOUNT){
				dataToWrite += interactor.elapsedTime*i + " , "; 
			};
			output.append(file + " , " +  interactor.elapsedTime + " , " +  dataToWrite);
			output.newLine();
			output.close();
		}
		System.out.println("Operation terminated as expected");
		
		
		
	}
}
