package szenario3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import szenario1.LinuxInteractor;

public class Szenario3 {
	final static int[] FILEAMOUNT = { 0, 1, 5,};// 10};//, 100, 1000, 10000 };
	final static String GSUTILPATH ="/home/coxtor/.opt/google-cloud-sdk/bin/gsutil";
	final static String[] FILENAME = {	"0Byte", "1Byte", "512Byte", "1KB", "512KB", "1MB"};//, "512MB", "1GB", "4GB" };
	final static String BASEPATH = System.getProperty("user.dir") + "/../data/";
	final static String ResultsFileName = "Szenario2_Realdata-Sequentiell-"+ System.currentTimeMillis() +".txt";
	public static void main(String[] args) throws IOException {
	BufferedWriter output;
	
	output = new BufferedWriter(new FileWriter(ResultsFileName, false));	
	String dataToWrite = "";
	for(int i : FILEAMOUNT){
		dataToWrite += "Time Ms " + i + " , "; 
	};	
	
	output.write("Filesize ,  " + dataToWrite);
	output.newLine();
	output.close();
		System.out.println("Transfer started");
		for(String file: FILENAME){
			String command = GSUTILPATH +" cp " + BASEPATH + file + " gs://iot-test_hda ";
			LinuxInteractor interactor = new LinuxInteractor();
			dataToWrite = "";
			long tmpElapsedTime = 0;
			for(int i: FILEAMOUNT){
				System.out.println("Transferring " + file + " " + i + " times");
				for(int j=0;j < i;j++){
					/*
					 * 
					 * 
					 * HIER MÜSSEN THREADS DANN HIN
					 * 
					 * 
					 * 
					 * 
					 * 
					 */
					String result = interactor.executeCommand(command, true);
					tmpElapsedTime+= interactor.elapsedTime;
				}
				dataToWrite += tmpElapsedTime + " , ";
				System.out.println("Transferred " + file + " " + i +  " times  in " +  tmpElapsedTime + " Ms (" + interactor.elapsedTime/1000 +" sec.)");
			}
			
	
			output = new BufferedWriter(new FileWriter(ResultsFileName, true));

			output.append(file +", " +  dataToWrite);
			output.newLine();
			output.close();
		}
		System.out.println("Operation terminated as expected");
	}
}
