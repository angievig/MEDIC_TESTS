package test;

import prologParser.XCSPPrologParser;
import transform.CSP2File;

public class XCSPPrologParserTest {
	static private String path= "C:/Users/Angela Villota/Documents/Tests/Souffleusse/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String filename="instancesTest/testExtension2.xml";
		String filename="/Users/Angela/Google Drive/Doctorate_Angela/Publications/In_preparation/Renault/Desarrollo/Test/Test1/Renault-souffleuse-pos.xml";
//		try {
//			try(Stream<Path> paths = Files.walk(Paths.get("/Users/Angela/Google Drive/Doctorate_Angela/Publications/In_preparation/Renault/Desarrollo/Test/Test1"))) 
//			{ 
//			    paths.forEach(filePath -> 
//			    {
//			        if (Files.isRegularFile(filePath)) {
//			            
						XCSPPrologParser Xparser;
						try {
							Xparser = new XCSPPrologParser(filename);
							CSP2File converter= new CSP2File(Xparser.getCSP());
							converter.transform(path, 1, "Renault-souffleuse-pos");
							System.out.println("ok_" +"Renault-souffleuse-pos" );
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Xparser.print();

//			          
//			        }
//			    });
//			}
////			int i=1;
//
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("Exception");
//		}

	}

}
