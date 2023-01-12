/* Authors: Smith Gakuya, Yaqi Huang
**We have neither given nor received unauthorized aid in this assignment
**All group members were present and contributing during all work on this project
*/

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SentAnalysisBest {

	final static File TRAINFOLDER = new File("train");
	
	final static HashMap<String, Integer> posCount = new HashMap<String, Integer>();
	final static HashMap<String, Integer> negCount = new HashMap<String, Integer>();
	static double posDocCount = 0;
	static double negDocCount = 0;
		
	public static void main(String[] args) throws IOException
	{	
		ArrayList<String> files = readFiles(TRAINFOLDER);		
		
		train(files);
		//if command line argument is "evaluate", runs evaluation mode
		if (args.length==1 && args[0].equals("evaluate")){
			evaluate();
		}
		else{//otherwise, runs interactive mode
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			System.out.print("Text to classify>> ");
			String textToClassify = scan.nextLine();
			System.out.println("Result: "+classify(textToClassify));
		}
		
	}
	

	
	/*
	 * Takes as parameter the name of a folder and returns a list of filenames (Strings) 
	 * in the folder.
	 */
	public static ArrayList<String> readFiles(File folder){
		
		System.out.println("Populating list of files");
		
		//List to store filenames in folder
		ArrayList<String> filelist = new ArrayList<String>();
		
	
		for (File fileEntry : folder.listFiles()) {
	        String filename = fileEntry.getName();
	        filelist.add(filename);
		}
	    
		/*
		for (String fileEntry : filelist) {
	        System.out.println(fileEntry);
		}
		
		System.out.println(filelist.size());
		*/
		
		
		return filelist;
	}
	
	

	
	/*
	 * TO DO
	 * Trainer: Reads text from data files in folder datafolder and stores counts 
	 * to be used to compute probabilities for the Bayesian formula.
	 * You may modify the method header (return type, parameters) as you see fit.
	 */
	public static void train(ArrayList<String> files) throws FileNotFoundException
	{

		for (String fileName: files){
			//split filename to find if review is negative or positive
			boolean label = (fileName.split("-")[1].equals("5"));
			
			//get numbers of each type of file pos and neg
			if (label){
				posDocCount ++;
			} else {
				negDocCount ++;
			}

			File file = new File(TRAINFOLDER, fileName);
			Scanner scan = new Scanner(file);
			String prevWord = "";
			
			while (scan.hasNext()){
				//using lowercase for all words
				String word = scan.next().toLowerCase();
				
				//keep track of this word and the previous word
				String doubleWord = prevWord + " " + word;
				
				//put this word a combination of the last and this in the respective hash table
				if (label){
					posCount.put(word, posCount.getOrDefault(word, 0) + 1);
					if (!word.equals(doubleWord))
						posCount.put(doubleWord, posCount.getOrDefault(doubleWord, 0) + 1);
				} else {
					negCount.put(word, negCount.getOrDefault(word, 0) + 1);
					if (!word.equals(doubleWord))
						negCount.put(doubleWord, negCount.getOrDefault(doubleWord, 0) + 1);
				}
				prevWord = word;
				
			}
			scan.close();
		}

	}


	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static String classify(String text)
	{	
		String[] wordList = text.toLowerCase().split(" ");
		
		//calculate probabilty  of a doc being positive & neg
		double pPos = posDocCount/(posDocCount + negDocCount);
		double pNeg = 1 - pPos;
		
		double posResult = log2(pPos);
		double negResult = log2(pNeg);
		
		//number of unique words in positive and negative docs
		double p = posCount.size();
		double n = negCount.size();
				
			
		//calculate probability of words in text being positive and negative
		String prevWord = "";
		for (String word : wordList){
			String doubleWord = prevWord + " " + word;
			doubleWord = doubleWord.strip();
			
			//get probabilities of being negative of single and double words
			negResult += log2((negCount.getOrDefault(word, 0) + 0.0001)/(n + 0.0001* (p+n)));
			if (!word.equals(doubleWord))
				negResult += log2((negCount.getOrDefault(doubleWord, 0) + 0.0001)/(n + 0.0001* (p+n)));
			
			//get probabilities of being positive of single and double words
			posResult += log2((posCount.getOrDefault(word, 0) + 0.0001)/(p + 0.0001* (p+n)));
			if (!word.equals(doubleWord))
				posResult += log2((posCount.getOrDefault(doubleWord, 0) + 0.0001)/(p + 0.0001* (p+n)));
			
			//keep track of current word to get probability of this and next word in next iteration
			prevWord = word;
		}

		String result = posResult > negResult ? "positive" : "negative";
		
		return result;
		
	}
	
	/*	
	 * Computes log base 2 of (d) 
	 */
	private static double log2(double d){
		return Math.log(d)/Math.log(2);
	}
	

	/*
	 * TO DO
	 * Classifier: Classifies all of the files in the input folder (type: File) as positive or negative
	 * You may modify the method header (return type, parameters) as you like.
	 */
	public static void evaluate() throws FileNotFoundException 
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter folder name of files to classify: ");
		String foldername = scan.nextLine();
		File folder = new File(foldername);
		
		ArrayList<String> filesToClassify = readFiles(folder);
		
		//keep track of number of pos/neg docs as well as number of correctly classified docs
		double numPos = 0;
		double numNeg = 0;
		double posCorrect = 0;
		double negCorrect = 0;

		//read each file and classify each text
		for (String file : filesToClassify) {
			String text = "";
			File f = new File(file);
			Scanner s = new Scanner(f);
			while (s.hasNext()) {
				text += " " + s.next();
			}
			text = text.strip();
			s.close();
			String sentiment = classify(text);
			boolean label = (file.split("-")[1].equals("5"));
			if (label) {
				//number of positive files in test file
				numPos++;
				if (sentiment == "positive")
					posCorrect++;
				}
			else {
				//number of negative files in test file
				numNeg++; 
				if (sentiment == "negative")
					negCorrect++;
			}

		}
		
		System.out.println("Accuracy: " + (posCorrect+negCorrect)/(numPos+numNeg) * 100 + "%");
		System.out.println("Precision(positive): " + posCorrect/(numPos) * 100 + "%");
		System.out.println("Precision(negative): " + negCorrect/(numNeg) * 100 + "%");
	}
	
	
	
}
