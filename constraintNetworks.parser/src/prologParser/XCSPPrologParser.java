package prologParser;

import java.util.ArrayList;
import java.util.Arrays;
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

import cspElements.CSP;
import cspElements.Constraint;
import cspElements.Variable;

import org.xcsp.parser.entries.XVariables.XVarInteger;

import com.variamos.compiler.solverSymbols.ConstraintSymbols;
import com.variamos.compiler.solverSymbols.SWIPrologSymbols;


public class XCSPPrologParser implements XCallbacks2{
	private Implem implem;
	private Document doc;
	private XParser parser;
	private Map<XVarInteger, Variable> mapVar = new LinkedHashMap<XVarInteger, Variable>();

	private TreeSet<Variable> variables= new TreeSet<Variable>();
	//private Set<Stri> domains;
	private TreeSet<Constraint> constraints= new TreeSet<Constraint>();
	
	public XCSPPrologParser(String filename) throws Exception{
		
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
		String domain= minValue +SWIPrologSymbols.DOMAIN_INTERVAL  + maxValue;
		Variable x = new Variable(varName(xx.id),domain ); // Build your solver variable x here using xx.id, minValue and maxValue
		//TODO remove print
		//System.out.println(varName(xx.id)+ " "+ domain);
		mapVar.put(xx,x);
		variables.add(x);
	}
	/**
	 * VAriables con dominio de la forma X in [z, w, v, r]
	 */
	@Override
	public void buildVarInteger(XVarInteger xx, int[] values) {
		String listOfValues= values[0]+"";
		for (int i = 1; i < values.length; i++) {
			listOfValues+= SWIPrologSymbols.ORDOMAIN+ ConstraintSymbols.SPACE + values[i];
		}
		//String domain= SWIPrologSymbols.IN +ConstraintSymbols.SPACE + listOfValues;
		String domain=   listOfValues;
		//TODO remove print
		//System.out.println(varName(xx.id) + " "+ domain);
		Variable x = new Variable(varName(xx.id),domain ); // Build your solver variable x here using xx.id, minValue and maxValue
		mapVar.put(xx,x); 
		variables.add(x);
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
	private Variable trVar(Object x) { 
		return mapVar.get((XVarInteger) x);
	}
	private Variable[] trVars(Object vars) {
		return Arrays.stream((XVarInteger[]) vars).map(x -> mapVar.get(x)).toArray(
				Variable[]::new);
	}
	private Variable[][] trVars2D(Object vars) {
		return Arrays.stream((XVarInteger[][]) vars).map(t -> trVars(t)).toArray(Variable
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
	public void extension(String id, Variable[] vars, int[][] tuples, boolean positive) {
		Constraint cons;
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
			cons= new Constraint(id, expression, new ArrayList<Variable>(Arrays.asList(vars)));
			
			constraints.add(cons);
		}else{
			System.out.println("Error: negative extension");
		}
	}
	public String listOfVars(Variable [] vars){
		String list=ConstraintSymbols.OPEN_BRACKET+  ConstraintSymbols.OPEN_BRACKET+ vars[0].getId();
		for (int i = 1; i < vars.length; i++) {
			Variable v= vars[i];
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
	
	public CSP getCSP(){
		CSP csp= new CSP();

		csp.setConstraints(constraints);
		csp.setVariables(variables);
		return csp;
	}
	
	



}
