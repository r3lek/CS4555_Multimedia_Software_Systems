package hw1;

import java.util.Scanner;

public class CS4551_Cayetano
{
	public static void main(String[] args)
	{
		// the program expects one commandline argument
		// if there is no commandline argument, exit the program
		if(args.length != 1)
		{
			usage();
			System.exit(1);
		}


		String menu = "\n\nMain Menu\n"
				+ "---------------------------------------------\r\n" + 
				"1. Conversion to Gray-scale Image (24bits->8bits)\r\n" + 
				"2. Conversion to 8bit Indexed Color Image using Uniform Color\r\n" + 
				"Quantization (24bits->8bits)\r\n" + 
				"3. Quit\r\n"
				+ "----------------------------------------------\r\n" + 
				"";

		// Create an Image object with the input PPM file name.
		MImage img = new MImage(args[0]);
		MImage img2 = new MImage(args[0]);
		MImage img3 = new MImage(args[0]);

		//System.out.println(menu);

		//loop continuously
		Scanner in = new Scanner(System.in);
		int choice = -1;
		do{
			System.out.println(menu);
			choice = in.nextInt();

			// switch statement with int data type 
			switch (choice) { 
			case 1: 
				colorToGray(img);
				break; 
			case 2: 
				displayLUT(img2);
				colorToIndex(img2, img3);
				break; 
			case 3: 
				System.exit(1);
				break; 
			default: 
				break; 
			} 

		}while(choice >0 || choice <4);


		//System.out.println(img);

		System.out.println("--Good Bye--");
	}

	public static void usage()
	{
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}  


	//Task 1 done 
	public static void colorToGray(MImage img) {
		MImage image = img;
		int[] rgb = new int[3]; //rgb of pixel
		int[] grayRGB = new int[3]; //rgb in grayscale 

		//loop through every pixel should eventually get to 250 x 273 (H x W)
		for(int height = 0; height < image.getH(); height++) { //increment the height 
			for(int width = 0; width < image.getW(); width++) { //increment width
				//System.out.println("Ducky.ppm coords: " + "(" + width + " , " + height + ")");
				image.getPixel(width, height, rgb);
				//System.out.println("R: " + rgb[0] + "G: " + rgb[1] + "B: " + rgb[2]);
				int gray = (int) Math.floor(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
				grayRGB[0] = gray;
				grayRGB[1] = gray;
				grayRGB[2] = gray;

				image.setPixel(width, height, grayRGB);
			} 
		}
		String grayFile = img.getName();
		grayFile = new StringBuffer(grayFile).insert(grayFile.length()-4, "-gray").toString();
		image.write2PPM(grayFile);
	}

	//function to display look up table WORKS
	public static void displayLUT(MImage img) {
		String bitBin = "";
		String red = "";
		String green = "";
		String blue = "";

		System.out.println(img);
		System.out.println("DISPLAYING LOOK UP TABLE");
		System.out.println("INDEX \t\tR \t\tG \t\tB");
		System.out.println("---------------------------------------------------");
		for(int i = 0; i <= 255; i++) {
			bitBin = String.format("%8s", Integer.toBinaryString(i)).replace(" ", "0");
			red = bitBin.substring(0,3);
			green = bitBin.substring(3,6);
			blue = bitBin.substring(6,8);
			// System.out.println(red + " "+ green + " " + blue);
			System.out.println(""+i + ": \t\t"+(32 * Integer.parseInt(red, 2)+16) + "\t\t" + +(32 * Integer.parseInt(green, 2) +16) + "\t\t" + (64 * Integer.parseInt(blue, 2) +32));
		}


	}

	//DISPLAYS INDEX WORKS
	public static void colorToIndex(MImage img, MImage img2) {
		MImage qt8 = img2;

		MImage image = img;
		int[] rgb = new int[3]; //rgb of pixel
		int[] grayRGB = new int[3]; //rgb in grayscale 
		int[] qt8RGB = new int[3]; //rgb in grayscale 
		int red,green,blue;
		int avgIndex;
		String redBin = "";
		String greenBin = "";
		String blueBin = "";
		String combinedRGBbin = "";


		//loop through every pixel should eventually get to 250 x 273 (H x W)
		for(int height = 0; height < image.getH(); height++) { //increment the height 
			for(int width = 0; width < image.getW(); width++) { //increment width
				image.getPixel(width, height, rgb);
				qt8.getPixel(width, height, qt8RGB);

				red = rgb[0]/32;
				green = rgb[1]/32;
				blue = rgb[2]/64;

				//System.out.println("R" + red);
				//System.out.println("G" + green);
				//System.out.println("B" + blue);


				redBin = String.format("%3s", Integer.toBinaryString(red)).replace(" ", "0");
				greenBin = String.format("%3s", Integer.toBinaryString(green)).replace(" ", "0");
				blueBin = String.format("%2s", Integer.toBinaryString(blue)).replace(" ", "0");
				combinedRGBbin = (redBin + greenBin + blueBin);
				avgIndex = Integer.parseInt(combinedRGBbin, 2);


				grayRGB[0] = avgIndex;
				grayRGB[1] = avgIndex;
				grayRGB[2] = avgIndex;


				image.setPixel(width, height, grayRGB);

				qt8RGB[0] = (32* Integer.parseInt(redBin, 2) +16);
				qt8RGB[1] = (32* Integer.parseInt(greenBin, 2) +16);
				qt8RGB[2] = (64* Integer.parseInt(blueBin, 2) +32);
				qt8.setPixel(width, height, qt8RGB);




				//System.out.println("Binary is " +String.format("%3s", Integer.toBinaryString(red)).replace(" ", "0"));
				//System.out.println("Binary is " +String.format("%3s", Integer.toBinaryString(green)).replace(" ", "0"));
				//System.out.println("Binary is " +String.format("%2s", Integer.toBinaryString(blue)).replace(" ", "0"));
				//System.out.println("GRAY: " + "R: " + grayRGB[0] + "G: " + grayRGB[1] + "B: " + grayRGB[2]);
			} 
		}

		String indexFile = img.getName();
		indexFile = new StringBuffer(indexFile).insert(indexFile.length()-4, "-index").toString();
		// System.out.println("WHAT DID IT WRITE: ?????????????? " + indexFile);

		String qt8File = img.getName();
		qt8File = new StringBuffer(qt8File).insert(qt8File.length()-4, "-QT8").toString();

		image.write2PPM(indexFile);
		qt8.write2PPM(qt8File);
	}

}