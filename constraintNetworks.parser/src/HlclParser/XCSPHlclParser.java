package HlclParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

//import javax.swing.text.Utilities;

import org.w3c.dom.Document;
import org.xcsp.common.Types.TypeFlag;
import org.xcsp.common.Utilities;
import org.xcsp.parser.XCallbacks2;
import org.xcsp.parser.XParser;



import org.xcsp.parser.entries.XVariables.XVarInteger;

import com.variamos.compiler.solverSymbols.ConstraintSymbols;
import com.variamos.compiler.solverSymbols.SWIPrologSymbols;
import com.variamos.hlcl.core.HlclProgram;
import com.variamos.hlcl.model.domains.IntervalDomain;
import com.variamos.hlcl.model.domains.RangeDomain;
import com.variamos.hlcl.model.expressions.HlclFactory;
import com.variamos.hlcl.model.expressions.Identifier;
import com.variamos.hlcl.model.expressions.LiteralBooleanExpression;


public class XCSPHlclParser implements XCallbacks2{
	private Implem implem;
	private Document doc;
	private XParser parser;
	private Map<XVarInteger, Identifier> mapVar = new LinkedHashMap<XVarInteger, Identifier>();
	
	Map<String, Identifier> idMap= new HashMap<String, Identifier>();
	HlclProgram prog= new HlclProgram();
	static private HlclFactory f = new HlclFactory();

//	
//	TreeSet<Variable> variables= new TreeSet<Variable>();
//	//private Set<Stri> domains;
//	private TreeSet<Constraint> constraints= new TreeSet<Constraint>();
	
	public XCSPHlclParser(String filename) throws Exception{
		
		implem= new Implem(this);
		loadInstance(filename);
//		doc= Utilities.loadDocument(filename);
//		parser= new XParser(doc);		
	}
	@Override
	public Implem implem() {
		return implem;
	}
	
	/**
	 * variables con dominio de la forma x in min..max
	 */
	@Override
	public void buildVarInteger(XVarInteger xx, int minValue, int maxValue) {
		//String domain= SWIPrologSymbols.IN + ConstraintSymbols.SPACE +minValue +SWIPrologSymbols.DOMAIN_INTERVAL  + maxValue;
		//String domain= minValue +SWIPrologSymbols.DOMAIN_INTERVAL  + maxValue;
		//Variable x = new Variable(varName(xx.id),domain ); // Build your solver variable x here using xx.id, minValue and maxValue
		Identifier x = f.newIdentifier(varName(xx.id));
		RangeDomain domain= new RangeDomain();
		domain.setLowerValue(minValue);
		domain.setUpperValue(maxValue);
		x.setDomain(domain);
		//TODO remove print
		//System.out.println(varName(xx.id)+ " "+ domain);
		mapVar.put(xx,x);
		idMap.put(varName(xx.id), x);
		//variables.add(x);
	}
	/**
	 * VAriables con dominio de la forma X in [z, w, v, r]
	 */
	@Override
	public void buildVarInteger(XVarInteger xx, int[] values) {


		//TODO remove print
		//System.out.println(varName(xx.id) + " "+ domain);
		// Build your solver variable x here using xx.id, minValue and maxValue
		Identifier x = f.newIdentifier(varName(xx.id));
		IntervalDomain domain= new IntervalDomain();
		
		
		for (int i = 0; i < values.length; i++) {
			domain.add(values[i]);
		}
		x.setDomain(domain); 
		mapVar.put(xx,x); 
		idMap.put(varName(xx.id), x);
	
	}
	/**
	 * Converts the id of a variable into a valid variable name in swiProlog
	 * @param id from the parser
	 * @return name
	 */
	public String varName(String id){
		String name=id.toUpperCase();

		if (name.contains("[")){
			name= name.replace("[", "_");
			name= name.replace("]", "");	
		}
		return name;
	}
	
	/********** Metodos sugeridos por el tutorial, se cambió VarInteger por Variable */
	private Identifier trVar(Object x) { 
		return mapVar.get((XVarInteger) x);
	}
	private Identifier[] trVars(Object vars) {
		return Arrays.stream((XVarInteger[]) vars).map(x -> mapVar.get(x)).toArray(
				Identifier[]::new);
	}
	private Identifier[][] trVars2D(Object vars) {
		return Arrays.stream((XVarInteger[][]) vars).map(t -> trVars(t)).toArray(Identifier
				[][]::new);
	}
	
	//********** Metodos sugeridos por el tutorial
	/**
	 * Metodo que maneja las restricciones por extension
	 */
	@Override
	public void buildCtrExtension(String id, XVarInteger[] list, int[][] tuples, boolean positive, Set<TypeFlag> flags) {
		if (flags.contains(TypeFlag.STARRED_TUPLES)) {
			// Can you manage short tables ? i.e., tables with tuples containing symbol * ? // If not, throw an exception.
			//...
			//throw new Exception();
			System.out.println("starred tuples");
		}
		if (flags.contains(TypeFlag.UNCLEAN_TUPLES)) {
			// You have possibly to clean tuples here, in order to remove invalid tuples.
			// A tuple is invalid if it contains a value a for a variable x, not present in dom(x) 
			// Note that most of the time, tuples are already cleaned by the parser
			//...
			//throw new Exception();
			System.out.println("unclean tuples");
		}
		extension(id, trVars(list), tuples , positive); 
		
	}
	public void extension(String id, Identifier[] vars, int[][] tuples, boolean positive) {
		LiteralBooleanExpression cons;
		//Constraint cons;
		String expression="";
		if (positive){
			//create the expression
			String listOfVars=listOfVars(vars);
			String listOfTuples=listOfTuples(tuples);
			expression+= SWIPrologSymbols.RELATION + ConstraintSymbols.OPEN_PARENTHESIS;
			expression+=listOfVars;
			expression+=ConstraintSymbols.SPACE;
			expression+=ConstraintSymbols.COMMA;
			expression+=listOfTuples;
			expression+=ConstraintSymbols.CLOSE_PARENHESIS;
			cons= new LiteralBooleanExpression(expression);
			cons.setIdentifierExpressionList(Arrays.asList(vars));
					//new Constraint(id, expression, new ArrayList<Variable>(Arrays.asList(vars)));
			prog.add(cons);
			
			//constraints.add(cons);
		}else{
			System.out.println("Error: negative extension");
		}
	}
	public String listOfVars(Identifier [] vars){
		String list=ConstraintSymbols.OPEN_BRACKET+  ConstraintSymbols.OPEN_BRACKET+ vars[0].getId();
		for (int i = 1; i < vars.length; i++) {
			Identifier v= vars[i];
			list+= ","+ ConstraintSymbols.SPACE + v.getId();
		}
		list+= ConstraintSymbols.CLOSE_BRACKET+ ConstraintSymbols.CLOSE_BRACKET; //es una lista de listas necesita doble corchete
		return list;
	}
	
	public String listOfTuples(int[][] tuples){
		String list=ConstraintSymbols.OPEN_BRACKET;
		
		String firstList= ConstraintSymbols.OPEN_BRACKET+ tuples[0][0];
		for (int j = 1; j < tuples[0].length; j++) {
			firstList+= ","+ ConstraintSymbols.SPACE+ tuples[0][j];
		}
		firstList+=ConstraintSymbols.CLOSE_BRACKET;
		
		
		list+= firstList;
		
		for (int i = 1; i < tuples.length; i++) {
			String innerList= ConstraintSymbols.OPEN_BRACKET + tuples[i][0];
			for (int j = 1; j < tuples[0].length; j++) {
				innerList+= ","+ ConstraintSymbols.SPACE+ tuples[i][j];
			}
			innerList+=ConstraintSymbols.CLOSE_BRACKET;
			list+=","+ ConstraintSymbols.SPACE + innerList;
		}
		list+= ConstraintSymbols.CLOSE_BRACKET;
		return list;
	}

	/**
	 * Method from the tutorial
	 */
	public void print(){
		parser.vEntries.stream().forEach(e ->System.out.println(e.toString()) );
		parser.cEntries.stream().forEach(e ->System.out.println(e.toString()) );
		parser.oEntries.stream().forEach(e ->System.out.println(e.toString()) );
	}
	
	public HlclProgram getCSP(){

		return prog;
	}
	
	public Map<String, Identifier> getIdMap(){

		return idMap;
	}
	
	



}
