/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Run in CMD Line: 
 * cd "/home/qimeng/Desktop/JWorkspace/NetBeansProjects/GA Project 23/build/classes"
 * java ec.Evolve -file ./ga/app/app1/exp1.params
 */
package ga.app.app1;

import ec.*;
import ec.simple.*;
import ec.util.Parameter;
import ec.vector.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author user
 */
public class exp1 extends Problem implements SimpleProblemForm{
    
    private int FirstTime = 0;
    private String runningspace; 
    private final String pgm = "perl";
    // private String perlPath = "/home/gpsr/webserver/cgibin/ifnepitope/svm.pl";
    private String perlPath = "/home/qimeng/gpsr/galaxy/tools/ifnepitope/progs/svm.pl";
    // private String perlParm = "/home/gpsr/NetBeansProjects/GAWorkspace";
    private String perlParm = "/home/qimeng/Desktop/JWorkspace/NetBeansProjects/GAWorkspace2";
    private String FRFile = "/home/qimeng/Desktop/JWorkspace/NetBeansProjects/GAWorkspace2/out.txt"; 
    
    @Override
    public void setup(final EvolutionState state, final Parameter base)
    {
        // DO NOT FORGET to call super.setup(...) !!
        super.setup(state,base);
        
        // set up infoFile
        runningspace = state.parameters.getString(base.push("rs"),null);
    
    }

    @Override
    public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum) {
        
        if (ind.evaluated) 
            return;
        
        // one time running section
        if (FirstTime == 0){
            System.out.println("Generation: " + state.generation);
            runningspace = runningspace + "G" + state.generation + ".txt";
            File file = new File(runningspace);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
            }
            FirstTime = 1;
        }
        
        float fitnessValue;
        
        GeneVectorIndividual EpitopeSequence = (GeneVectorIndividual) ind;
        fitnessValue = runPerl(perlParm + "/test.fasta", EpitopeSequence.genotypeToStringForHumans(), runningspace);
        
        SimpleFitness fitness = (SimpleFitness) EpitopeSequence.fitness;
        fitness.setFitness(state, fitnessValue, fitnessValue == 10000);
        EpitopeSequence.evaluated = true;    
        
        //System.out.println(ind.genotypeToStringForHumans());
        
    }
    
    // run Perl to fetch fitness values
    private float runPerl(String ifn, String str, String ofn){
        
        float fValue = 0;
        
        try{            
            // Generate test.pasta file
            File of = new File(ifn);
            PrintStream ps = new PrintStream(of);
            ps.println(">ESAT-6_MtbLENGTH_FROM0-15");
            //System.out.println(str);
            str = str.trim();
            if (str.length() == 27){
                str = str + "S";
            }                
            //System.out.println(str.length());
            
            str = str.replaceAll(" ", ""); 
            str = str.replaceAll("Z", "");
            ps.println(str);
            
            ps.close();
        
            // Build perl cmd
            String[] perlCmd = {pgm, perlPath, perlParm};
            InputStream in = null;
        
            // Running perl script
            Process pro = Runtime.getRuntime().exec(perlCmd);
            pro.waitFor();
            in = pro.getInputStream();
            //BufferedReader read = new BufferedReader(new InputStreamReader(in));
            // String result = read.readLine();
            // System.out.println("Info:" + result);
            
            // Read results
            File perlOutput = new File(FRFile);
            BufferedReader in2 = new BufferedReader(new FileReader(perlOutput)); 
            File gaOutput   = new File(ofn);
            FileWriter ps2 = new FileWriter(gaOutput, true);
            in2.readLine();
            String result = in2.readLine();           
            System.out.println(result);
            
            if (result.equals("")){
               ps2.write(str);
               ps2.append("\r\n");
               fValue = -100;
            }else{
               ps2.write(result.substring(48 + str.length(), result.length()));
               // System.out.println(result.substring(48 + str.length(), result.length()));
               fValue = Float.valueOf(result.substring(48 + str.length(), result.length()));
               ps2.append("\r\n");
            } 
           
            in2.close();
            ps2.close();
            
        } catch (Exception e){
            System.out.println(e);
        }
        
        return fValue;

    }

}
