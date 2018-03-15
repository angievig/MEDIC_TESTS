package testSplotModels;

import java.io.File;
import java.util.List;

import javax.xml.transform.TransformerException;

import com.variamos.common.core.utilities.FileUtils;
import com.variamos.common.model.enums.NotationType;
import com.variamos.reasoning.core.transformer.FeatureModelSPLOTransformer;
import com.variamos.reasoning.core.transformer.VariabilityModelTransformer;
import com.variamos.reasoning.defectAnalyzer.model.dto.VMTransformerInDTO;
import com.variamos.reasoning.defectAnalyzer.model.transformation.VariabilityModel;

import util.TestCaseFile;

public class TestSPLOTModels {
	public final static String IN_TEST_PATH="/Users/Angela/Test/Tests_Medic_HLCL/SPLOT_Models/Models/";
	public final static String OUT_TEST_PATH="/Users/Angela/Test/Tests_Medic_HLCL/SPLOT_Models/Logs/";
	
	public static final String SPLOT_EXTENSION = ".splx";
	static String report="";

	private int numberTests;
	
	public TestSPLOTModels(){
		numberTests=0;
		
	}
	
	
	public void runTest(String name, VariabilityModel vmod, String root){
		
		// con la ruta del archivo es necesario
		// transformar el archivo en un variamibility model
		// obtener el hlcl
		// organizar los parámetros para las pruebas : nombre del problema
		// ruta en donde se alojan los resultados de las pruebas
		
		
	
		String path=OUT_TEST_PATH;
		
		TestCaseFile test = new TestCaseFile(path, name, vmod,"RAND");
		test.startTests(10, 1);
		report +=test.getReport();
//		VariabilityModelTransformer varMod = new VariabilityModelTransformer();
//		System.out.println("Modelo "+ name);
//		System.out.println();
//		
//		varMod.printTransformedModelSWIProlog(vmod);
		
		numberTests++;
		System.out.println();
		System.out.println();
		
		
	}
	public static void main(String[] args){
		
		TestSPLOTModels tester= new  TestSPLOTModels();
		int cantidadModelos=0;
		List<File> filesList = FileUtils.readFileFromDirectory(IN_TEST_PATH);
		
		FeatureModelSPLOTransformer splotTransformer= new FeatureModelSPLOTransformer();
		VMTransformerInDTO params= new VMTransformerInDTO();
		
		for (File file : filesList) {
			try {
				String fileName= file.getName();
				System.out.println(fileName);


				if (fileName.endsWith(SPLOT_EXTENSION) || fileName.endsWith("xml")) {
					String shortName, path;
					shortName= fileName.replaceFirst(SPLOT_EXTENSION, "");
					path=IN_TEST_PATH+ fileName;
					//organizar los parametros
					params.setNotationType(NotationType.FEATURES_MODELS);
					params.setPathToTransform(path);
					
					//System.out.println(shortName+" "+params.getPathToTransform());
					tester.runTest(shortName, splotTransformer.transform(params), splotTransformer.getRootName());
				}
				cantidadModelos++;
			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("Excepcion transformando modelo"
						+ file.getName());
			}
		}



			
		System.out.println("end tests");
		System.out.println(report);

	}
	
	

}
