package szenario1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Szenario1 {
	final static int[] FILEAMOUNT = { 0, 1, 5, 10, 100, 1000, 10000 };
	final static String[] FILENAME = {	"0Byte", "1Byte", "512Byte", "1KB", "512KB", "1MB", "512MB", "1GB", "4GB" };
	final static String BASEPATH = "/home/coolpharaoh/sdn/data/";
	public static void main(String[] args) throws IOException {
		Process process = new ProcessBuilder(
				"/bin/bash", "-c", "gsutil","cp",BASEPATH+FILENAME[2], "gs://iot-test_hda").start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;

				System.out.printf("Output of running %s is:", Arrays.toString(args));

				while ((line = br.readLine()) != null) {
				  System.out.println(line);
				}
	}

}
