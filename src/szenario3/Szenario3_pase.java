package szenario3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import szenario1.LinuxInteractor;

public class Szenario3 extends Thread {
	final static int[] FILEAMOUNT = { 0, 1, 10, 100, 1000, 10000 };
	final static String GSUTILPATH = "/home/coolpharaoh/.opt/google-cloud-sdk/bin/gsutil";
	final static String[] FILENAME = { "0Byte", "1Byte", "512Byte", "1KB", "512KB", "1MB", "512MB", "1GB", "4GB" };
	final static String BASEPATH = System.getProperty("user.dir") + "/../data/";
	final static String ResultsFileName = "Szenario3_";
	
	//values needed to trace the threads
	String threadCommand = "";
	String threadFile = "";
	int threadNumber = 0;
	Vector<ThreadReturnValue> threadReturnValue = new Vector<>();

	public Szenario3(int number, String file, String command, Vector<ThreadReturnValue> returnValues) {
		threadNumber = number;
		threadFile = file;
		threadCommand = command;
		threadReturnValue = returnValues;
	}

	@Override
	public void run() {
		LinuxInteractor interactor = new LinuxInteractor();
		String result = interactor.executeCommand(threadCommand, true);
		threadReturnValue.addElement(new ThreadReturnValue(threadNumber, interactor.elapsedTime));
		System.out.println("Thread " + threadNumber + " transferred " + threadFile + " in " + interactor.elapsedTime
				+ " Ms (" + interactor.elapsedTime / 1000 + " sec.)");
	}

	public static void main(String[] args) throws IOException {
		//enter the parameters
		Scanner sc = new Scanner(System.in);

		System.out.print("0: 0Byte, 1: 1Byte, 2: 512Byte, 3: 1KB, 4: 512KB, 5: 1MB, 6: 512MB, 7: 1GB, 8: 4GB\n"
				+ "Choose a Size: ");
		int fileSize = sc.nextInt();

		System.out.print("Enter the amount: ");
		int fileAmount = sc.nextInt();

		sc.close();
		
		
		//start all threads
		String command = GSUTILPATH + " cp " + BASEPATH + FILENAME[fileSize] + " gs://iot-test_hda ";

		System.out.println("\nTransferring " + FILENAME[fileSize] + " " + fileAmount + " times");
		
		Vector<ThreadReturnValue> returnValues = new Vector<>();
		for (int j = 0; j < fileAmount; j++) {
			Thread r = new Szenario3(j, FILENAME[fileSize], command, returnValues);
			new Thread(r).start();
		}

		System.out.println("\nAll Threads started.");
		
		//Wait for Returnvalue from the threads
        while (returnValues.size() < fileAmount) {
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        System.out.println("All files transferred");
        
        //write output in File (filename example: "Szenario 3_512Byte_15")
        BufferedWriter output;
    	output = new BufferedWriter(new FileWriter(ResultsFileName + FILENAME[fileSize] + "_" + fileAmount + ".txt", false));
    	output.write("Transferring " + FILENAME[fileSize] + " " + fileAmount + " times");
    	output.newLine();
    	output.newLine();
    	ThreadReturnValue max = new ThreadReturnValue(0,0);
    	for(ThreadReturnValue val : returnValues){
    		output.write("Thread " + val.count + " transferred " + FILENAME[fileSize] + " in " + val.time
    				+ " Ms (" + (int)(val.time / 1000) + " sec.)");
    		output.newLine();
    		if(val.time > max.time){
    			max = val;
    		}
    	}
    	//write maximum
    	output.newLine();
    	output.write("Thread " + max.count + " had a maximum uploadtime with " + max.time
				+ " Ms (" + (int)(max.time / 1000) + " sec.)");
    	output.close();
    	
	}
	
	
}

//helperclass for the tread returnvalues
class ThreadReturnValue{
	int count;
	double time;
	ThreadReturnValue(int count, double time){
		this.count = count;
		this.time = time;
	}
	
}
