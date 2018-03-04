/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.buildparams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gpsr
 */
public class GABuildParams {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // input
        String infl         = "";
        String outf         = ""; 
        String tmpf         = "";
        String default_infl = "/home/gpsr/NetBeansProjects/GA_Output/GA_input.txt";
        String default_outf = "/home/gpsr/NetBeansProjects/GA_Output/";
        String default_tmpf = "/home/gpsr/NetBeansProjects/GA_Output/GA_tmpfile.params";
        
        // gloabl settings
        String JavaPath         = "/home/gpsr/java/jdk1.8.0_45/bin/java";
        String EntryProgramPath = "NetBeansProjects/GA Project 23/build/classes";
        String ECJMain          = "ec.Evolve";
        String InputKeyword     = "loop";
        
        
        if (infl.equals("")) infl = default_infl;
        if (outf.equals("")) outf = default_outf;
        if (tmpf.equals("")) tmpf = default_tmpf;
        
        System.out.println("The input file    : " + infl);
        System.out.println("The output folder : " + outf);
        System.out.println("the template para : " + tmpf);
        
        // build paraDB
        GAParams params = new GAParams();
        int combinations = build_paraDB(infl, InputKeyword, params);
        
        // build combinations
        System.out.println("There are " + combinations + " to be generated. ");
        String[][] paraSettings  = new String [combinations][params.size()];
          
        for (int i=0; i<params.size(); i++) {
            
            int element_repeat = 1;
            
            for (int j=i; j<params.size()-1; j++) {
                element_repeat = element_repeat * params.get(j+1).ParamValues.length;
            }
            //System.out.println("element_repeat : " + element_repeat);
            
            int loop_repeat = combinations / element_repeat / params.get(i).ParamValues.length;
            //System.out.println("loop_repeat : " + loop_repeat);
            
            int iteral = 0;
            for (int j=0; j<loop_repeat; j++){
                for (int k=0; k<params.get(i).ParamValues.length; k++){
                    for (int h=0; h<element_repeat; h++){
                        paraSettings[iteral][i] = params.get(i).ParamValues[k];
                        iteral ++;
                    }
                }
            }
            
        }
        
        String[] s = new String[combinations];
        String str = "";
        for (int i=0; i<combinations; i++) {
            for (int j=0; j<params.size(); j++) {
                str = str + params.get(j).ParamName + " = " +paraSettings[i][j] + ";";
            }
            s[i] = str;
            str = "";
        }
        
        
        // Generate parameter files
        try {
            // Generate parameter summary file 

            FileWriter fw = new FileWriter(new File(outf + "Param summary.txt"));
            fw.write("*---------------------------------------------------------\n");
            fw.write("               The summary of GA Parramater files         \n");
            fw.write("*---------------------------------------------------------\n");
            fw.write("There are " + s.length + " param files to be generated.   \n");
            fw.write("*---------------------------------------------------------\n");
            for (int i=0; i<s.length; i++){
                fw.write("Param " + i + " : " + s[i] + "\n");
            }
            fw.close();
            
            // Generate each param file 
            for (int i=0; i<s.length; i++){
                String tmpFolder = outf + "PFiles/" + "Param" + i + "/";
                File f = new File(tmpFolder);
                f.mkdirs();
                fw = new FileWriter(new File(outf + "PFiles/" + "Param" + i + "/param.params" ));
                fw.write("parent.0 = " + tmpf + "\n");
                String[] output = s[i].split(";");
                
                for (int j=0; j<output.length; j++){
                    fw.write(output[j] + "\n");
                }
                
                fw.write("eval.problem.rs = " + tmpFolder + "\n");
                fw.write("stat.child.0.generation-log-folder = " + tmpFolder + "summary/\n");
            
                fw.close();
            }
            
            // Generate Perl scripts to run ECJ
            fw = new FileWriter(new File(outf + "ECJScript.pl" ));
            fw.write("chdir(\"NetBeansProjects/GA Project 23/build/classes\"); \n");
            String perlCmd = "system(\"/home/gpsr/java/jdk1.8.0_45/bin/java ec.Evolve ";
            for (int i=0; i<s.length; i++){
                fw.write(perlCmd + "-file " + outf + "PFiles/Param" + i + "/param.params" + "\");\n");
            }
            fw.close();            
            
        } catch (IOException e){
            System.out.println(e);
        }
        
        
        
        
    }
    
    /* 
    build paraDB
    */  
    public static int build_paraDB(String infl, String InputKeyword, GAParams params){
        
        File file = new File(infl);
        BufferedReader reader; 
        int combinations = 1;
        
        // read file 
        try{
            reader = new BufferedReader (new FileReader(file));
            String row;
            
            while ((row = reader.readLine()) != null) {       
                if (row.length() > 4) {
                    if (row.substring(0,4).equals(InputKeyword)){                        
                        row = row.replaceAll(" ", "");
                        row = row.substring(5, row.length());
                        String[] paramInput = row.split("=");
                        
                        if (paramInput.length == 2){
                            String[] paramValues = paramInput[1].split(",");
                            GAParam param = new GAParam(paramInput[0], paramValues);
                            combinations = combinations * param.ParamValues.length;
                            params.add(param);
                        } else {
                            System.out.println("The input param file is not correct.");
                        }
                    }
                    
                }
            }
            
        } catch (FileNotFoundException e){
            System.out.println(e);
        } catch (IOException e){
            System.out.println(e);
        } finally {
            return combinations;
    }
        
    }
    
    
}
