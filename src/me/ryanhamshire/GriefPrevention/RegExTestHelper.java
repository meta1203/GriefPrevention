package me.ryanhamshire.GriefPrevention;

import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * given a Inclusion and exclusion pattern, compiles a RegEx Pattern and can be used to test that a
 * string matches the first pattern but not the second.
 * @author BC_Programming
 *
 */
public class RegExTestHelper {

	private String InclusionPattern;
	private String ExclusionPattern;
	private Pattern Include;
	private Pattern Exclude;
	public String getInclusionPattern(){ return InclusionPattern;}
	public String getExclusionPattern(){ return ExclusionPattern;}
	public void setInclusionPattern(String value){InclusionPattern=value;recompile();}
	public void setExclusionPattern(String value){ExclusionPattern = value;recompile();}
	private void recompile(){
		Include = Pattern.compile(InclusionPattern,Pattern.CASE_INSENSITIVE);
		Exclude = Pattern.compile(ExclusionPattern,Pattern.CASE_INSENSITIVE);
	}
	public boolean match(String teststring){
		//return false if inclusion is null.
		if(Include==null) return false;
		//otherwise, return true if the the inclusion string matches and the
		//exclusion string is either null or doesn't match.
		return Include.matcher(teststring).find() && (Exclude==null || !Exclude.matcher(teststring).find());
	}
	public boolean included(String teststring){
		return !(Include==null) && Include.matcher(teststring).find();
	}
	/**
	 * returns true if the given parameter is excluded. 
	 * This will only be true if it also matches the included parameter.
	 * @param teststring
	 * @return
	 */
	public boolean excluded(String teststring){
		return included(teststring) && (!(Exclude==null)|| Exclude.matcher(teststring).find());
	}
	public RegExTestHelper(String IncludePattern,String ExcludePattern){
		InclusionPattern=IncludePattern;
		ExclusionPattern=ExcludePattern;
	}
	/**
	 * Current Default for Container Matcher.
	 */
	public static RegExTestHelper DefaultContainers = new RegExTestHelper("\\schest\\s|\\schests\\s|\\sfurnace\\s|" + 
		"\\sgrinder\\s|\\sextruder\\s|\\smachine\\s|\\sengine\\s|\\sturtle\\s|\\saccumulator\\s|\\sprecipitator\\s|\\sAssembler\\s|\\sinfuser\\s|\\smachine\\s|\\sreceptacle\\s|\\s.*chest\\s|" +
				"\\sTank\\s|\\sCrucible\\s|smelter\\s|\\sworkbench\\s|\\stable\\s","");
	
	public static RegExTestHelper DefaultAccess = new RegExTestHelper("\\sbutton\\s|\\sswitch\\s|\\sDoor\\s|\\sTrapdoor\\s","");
	public static RegExTestHelper DefaultTrash = new RegExTestHelper("\\sOre\\z","");
	
	public RegExTestHelper(FileConfiguration Source,FileConfiguration Target,String NodePath,RegExTestHelper Default){
		//NodePath.IncludeRE
		//NodePath.ExcludeRE
		InclusionPattern = Source.getString(NodePath + ".IncludeRE",Default.getInclusionPattern());
		ExclusionPattern = Source.getString(NodePath + ".ExcludeRE",Default.getExclusionPattern());
		recompile();
		Target.set(NodePath + ".IncludeRE", InclusionPattern);
		Target.set(NodePath + ".ExcludeRE", InclusionPattern);
		
	}
	
	
}