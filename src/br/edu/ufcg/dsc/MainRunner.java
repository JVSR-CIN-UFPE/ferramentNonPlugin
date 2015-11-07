package br.edu.ufcg.dsc;


/**
 * @author Jefferson Almeida - jra at cin dot ufpe dot br 
 */

import java.io.IOException;

import safeEvolution.fileProperties.FilePropertiesObject;
import safeEvolution.fileProperties.FilePropertiesReader;

import br.edu.ufcg.dsc.builders.ProductGenerator;
import br.edu.ufcg.dsc.ck.xml.Xml;
import br.edu.ufcg.dsc.util.AssetNotFoundException;
import br.edu.ufcg.dsc.util.DirectoryException;

import edu.mit.csail.sdg.alloy4.Err;

public class MainRunner implements Runnable {

	public void run() {
		long startTime = System.currentTimeMillis();
		ProductGenerator.MAX_TENTATIVAS = 2000;

		/* arguments */
		String stringFile = "inputFiles/allPairs.xml";

		/* Create several evolution pairs with this file and Run */
		severalPairsInput(stringFile);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("\nTotal Time Spent: " + elapsedTime/60000 + " minutes");
	}

	private void severalPairsInput(String stringFile) {
		Xml xml = new Xml(stringFile, "evolutionPairs");

		for (Xml pair : xml.children("pair")) {
			String source = pair.child("source").content();
			String target = pair.child("target").content();
			
			System.out.println("Source: " + source);
			System.out.println("Target: " + target );
			
			onePairInput(source, target);
		}
	}

	private void onePairInput(String source, String target) {
		// FIXME Improve this code
		String stringFile = "";
		String evolutionDescription = "";
		
		if(source.matches("(.*)branch\\d+\\.\\d")){ // TaRGeT
			stringFile = "inputFiles/branchTemplate.properties";
			evolutionDescription = "branch";
		}
		else if (source.matches("(.*)MobileMedia\\d+\\.\\d")){ // Mobile Media
			stringFile = "inputFiles/MobileMediaTemplate.properties";
			evolutionDescription = "MobileMedia";
		}
		// End FIX

		FilePropertiesObject propertiesObject = new FilePropertiesReader(stringFile).getPropertiesObject();
		
		propertiesObject.setSourceLineDirectory(source);
		propertiesObject.setTargetLineDirectory(target);
		propertiesObject.setTimeOut(300);
		propertiesObject.setArtifactsSourceDir(source + "src/TaRGeT Hephaestus/");
		propertiesObject.setArtifactsTargetDir(target + "src/TaRGeT Hephaestus/");
		propertiesObject.setEvolutionDescription(evolutionDescription);
		
		System.out.println(propertiesObject);
		
		ToolCommandLine toolCommandLine = new ToolCommandLine(propertiesObject.getLine());
		
		try {
			
			toolCommandLine.commonInfoBetweenApproaches(propertiesObject);
			
		} catch (Err e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AssetNotFoundException e) {
			e.printStackTrace();
		} catch (DirectoryException e) {
			e.printStackTrace();
		}
		
		runApproach(propertiesObject, toolCommandLine);
	}

	private void runApproach(FilePropertiesObject input, ToolCommandLine toolCommandLine) {
		Approach[] approaches = {Approach.IC, Approach.EIC};
		String[] tools = {"evosuite", "randoop"};
		
		for(Approach approach : approaches){
			for(String tool : tools){
				System.out.println("\n Run tool for approach: " +  approach + " and tool: " + tools );
				
				input.setApproach(approach);
				input.setGenerateTestsWith(tool);
				
				try {
					toolCommandLine.runApproach(input);
					
					System.out.println("\n\t SPL REPORT: \n");

					//write approach plus tool in property file
					toolCommandLine.persitResultsInPropertyFile(input);
					
				} catch (AssetNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (DirectoryException e) {
					e.printStackTrace();
				}
			}
		}
		
		// write pair results in spreadsheet
		toolCommandLine.writeResultsInSpreadSheet();
	}
	
	public static void main(String[] args) {
		new MainRunner().run();
	}
}
