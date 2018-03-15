package prologParser;

//import javax.swing.text.Utilities;

import org.w3c.dom.Document;
import org.xcsp.common.Utilities;
import org.xcsp.parser.XCallbacks2;
import org.xcsp.parser.XParser;

public class XCSPPrologParser2Copy implements XCallbacks2{
	private Implem implem;
	private Document doc;
	private XParser parser;
	
	public XCSPPrologParser2Copy(String filename) throws Exception{
		
		implem= new Implem(this);
		doc= Utilities.loadDocument(filename);
		parser= new XParser(doc);		
	}
	
	public void print(){
		parser.vEntries.stream().forEach(e ->System.out.println(e.toString()) );
		parser.cEntries.stream().forEach(e ->System.out.println(e.toString()) );
		parser.oEntries.stream().forEach(e ->System.out.println(e.toString()) );
	}

	@Override
	public Implem implem() {
		return implem;
	}

}
