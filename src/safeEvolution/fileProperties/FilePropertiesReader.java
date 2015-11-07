package safeEvolution.fileProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.edu.ufcg.dsc.Approach;
import br.edu.ufcg.dsc.Lines;
import br.edu.ufcg.dsc.am.AMFormat;
import br.edu.ufcg.dsc.ck.CKFormat;
import br.edu.ufcg.saferefactor.core.Criteria;

public class FilePropertiesReader {
	
	private String filePropertiesDirectory;
	private Properties properties;
	private FilePropertiesObject propertiesObject;
	private boolean isLoaded;
	
	public FilePropertiesReader() {
		this.properties = new Properties();
		this.isLoaded = false;
	}
	
	public FilePropertiesReader(String filePropertiesDirectory) {
		super();
		
		this.filePropertiesDirectory = filePropertiesDirectory;
		this.propertiesObject = new FilePropertiesObject();
		
		this.createFileProperty();
		this.loadData();
		
	}
	
	private void createFileProperty() {
		try {
			InputStream is = new FileInputStream(this.filePropertiesDirectory);
			this.properties.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private FilePropertiesObject loadData() {
		String evolutionDescription = this.properties.getProperty("evolutionDescription");
		this.propertiesObject.setEvolutionDescription(evolutionDescription);
		
		String sourceLineDirectory = this.properties.getProperty("sourceLineDirectory");
		this.propertiesObject.setSourceLineDirectory(sourceLineDirectory);
		
		String targetLineDirectory = this.properties.getProperty("targetLineDirectory");
		this.propertiesObject.setTargetLineDirectory(targetLineDirectory);
		
		String sourceLineLibDirectory = this.properties.getProperty("sourceLineLibDirectory");
		this.propertiesObject.setSourceLineLibDirectory(sourceLineLibDirectory);
		
		String targetLineLibDirectory = this.properties.getProperty("targetLineLibDirectory");
		this.propertiesObject.setTargetLineLibDirectory(targetLineLibDirectory);
		
		/* TaRGeT, MobileMeida, Default */
		Lines splType = findOutSplType(this.properties.getProperty("line"));
		this.propertiesObject.setLine(splType);
		
		String timeOut = this.properties.getProperty("timeout");
		this.propertiesObject.setTimeOut(Integer.parseInt(timeOut)); 
		
		String inputLimit = this.properties.getProperty("inputlimit");
		this.propertiesObject.setInputLimit(Integer.parseInt(inputLimit));
		
		/* APP, AP, IC, IP, EIC */
		Approach ap = findOutApproachType(this.properties.getProperty("approach")); 
		this.propertiesObject.setApproach(ap);
		
		String aspectsInSourceSPL = this.properties.getProperty("aspectsInSourceSPL");
		this.propertiesObject.setAspectsInSourceSPL(findOutTrueOrFalse(aspectsInSourceSPL));
		
		String aspectsInTargetSPL = this.properties.getProperty("aspectsInTargetSPL");
		this.propertiesObject.setAspectsInTargetSPL(findOutTrueOrFalse(aspectsInTargetSPL));
		
		String whichMethods = this.properties.getProperty("whichMethods");
		this.propertiesObject.setWhichMethods(findOutCriteria(whichMethods));
		
		String ckFormatSourceSPL = this.properties.getProperty("ckFormatSourceSPL");
		this.propertiesObject.setCkFormatSourceSPL(findOutCKFormat(ckFormatSourceSPL));
		
		String ckFormatTargetSPL = this.properties.getProperty("ckFormatTargetSPL");
		this.propertiesObject.setCkFormatTargetSPL(findOutCKFormat(ckFormatTargetSPL));
		
		String amFormatSourceSPL = this.properties.getProperty("amFormatSourceSPL");
		this.propertiesObject.setAmFormatSourceSPL(findOutAmFormat(amFormatSourceSPL));
		
		String amFormatTargetSPL = this.properties.getProperty("amFormatTargetSPL");
		this.propertiesObject.setAmFormatTargetSPL(findOutAmFormat(amFormatTargetSPL));
		
		String artifactsSourceDir = this.properties.getProperty("artifactsSourceDir");
		this.propertiesObject.setArtifactsSourceDir(artifactsSourceDir);
		
		String artifactsTargetDir = this.properties.getProperty("artifactsTargetDir");
		this.propertiesObject.setArtifactsTargetDir(artifactsTargetDir);
		
		String generateTestsWith = this.properties.getProperty("generateTestsWith");
		this.propertiesObject.setGenerateTestsWith(generateTestsWith);
		
		String extendedImpactedClasses = this.properties.getProperty("extended-impacted-classes");
		this.propertiesObject.setExtendedImpactedClasses(extendedImpactedClasses);
		
		this.isLoaded = true;
		return this.propertiesObject;
	}

	private Lines findOutSplType(String property) {
		Lines splType = Lines.getEnum(property);
		
		if(splType == null) {
			splType = Lines.DEFAULT;
		}
		
		return splType;
	}

	private Approach findOutApproachType(String property) {
		return Approach.valueOf( property.trim().toUpperCase() );
	}

	private boolean findOutTrueOrFalse(String question) {
		return Boolean.parseBoolean( question.trim().toLowerCase() );
	}

	private Criteria findOutCriteria(String whichMethods) {
		/*  <commonMethods>, <allMethods> */
		Criteria criteria = Criteria.ONLY_COMMON_METHODS_SUBSET_DEFAULT;
		if (whichMethods.trim().toLowerCase().equals("commonMethods")) {
			return Criteria.ONLY_COMMON_METHODS_SUBSET_DEFAULT;
		}
		else if (whichMethods.trim().toLowerCase().equals("allMethods")){
			return Criteria.ALL_METHODS_IN_SOURCE_AND_TARGET;
		}
		return criteria;
	}

	private CKFormat findOutCKFormat(String ckFormat) {
		return CKFormat.valueOf( ckFormat.trim().toUpperCase() );
	}

	private AMFormat findOutAmFormat(String amFormatSourceSPL) {
		return AMFormat.valueOf( amFormatSourceSPL.trim().toUpperCase() );
	}
		
	public FilePropertiesObject getPropertiesObject() {
		if (this.isLoaded)
			return this.propertiesObject;
		else
			return this.loadData();
	}
	
}
