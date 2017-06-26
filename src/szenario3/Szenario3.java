package szenario3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import szenario1.LinuxInteractor;

public class Szenario3 extends Thread {
	final static int[] FILEAMOUNT = { 0, 1, 2, 4, 8, 16/*, 32, 64 */};
	final static String GSUTILPATH = "/home/coolpharaoh/.opt/google-cloud-sdk/bin/gsutil";
	final static String[] FILENAME = { "0Byte", "1Byte"/*, "512Byte", "1KB", "512KB", "1MB", "512MB", "1GB", "4GB" */};
	final static String BASEPATH = System.getProperty("user.dir") + "/../data/";
	final static String ResultsFileName = "Szenario3_Groundtruth-"+ System.currentTimeMillis() +".txt";

	// values needed to trace the threads
	String threadCommand = "";
	Vector<Long> threadReturnValue = new Vector<>();

	public Szenario3(String command, Vector<Long> returnValues) {
		threadCommand = command;
		threadReturnValue = returnValues;
	}

	@Override
	public void run() {
		LinuxInteractor interactor = new LinuxInteractor();
		String result = interactor.executeCommand(threadCommand, true);
		threadReturnValue.addElement(interactor.elapsedTime);
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter output;
		output = new BufferedWriter(new FileWriter(ResultsFileName, false));
		StringBuilder dataToWrite = new StringBuilder();
		for (int i : FILEAMOUNT) {
			dataToWrite.append(i + " Threads , ");
		}
		output.write("Filesize , " + dataToWrite.toString());
		output.newLine();
		output.close();
			
		for (String nameVal : FILENAME) {
			// start all threads
			output = new BufferedWriter(new FileWriter(ResultsFileName, true));
			output.write(nameVal + ", ");
			output.close();
			for (int amountVal : FILEAMOUNT) {
				String command = GSUTILPATH + " cp " + BASEPATH + nameVal + " gs://iot-test_hda ";

				Vector<Long> returnValues = new Vector<>();
				for (int j = 0; j < amountVal; j++) {
					Thread r = new Szenario3(command, returnValues);
					new Thread(r).start();
				}

				// Wait for Returnvalue from the threads
				while (returnValues.size() < amountVal) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(nameVal + " , " + amountVal + " times complete");

				output = new BufferedWriter(new FileWriter(ResultsFileName, true));
				long max = 0;
				for (long val : returnValues) {
					if (val > max) {
						max = val;
					}
				}
				output.write(max + ",");
				output.close();
			}
			output = new BufferedWriter(new FileWriter(ResultsFileName, true));
			output.newLine();
			output.close();
		}

	}

}