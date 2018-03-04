/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.app.exp2;

import ec.*;
import ec.simple.*;
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

public class exp2 extends Problem implements SimpleProblemForm
    {
    
    private char[] _expected = "AAAAAAAAAAAAAAA".toCharArray();
    private int FirstTime = 0;
    private String runningspace = "/home/gpsr/NetBeansProjects/GARunningSpace/";    
    private String pgm = "perl";
    private String perlPath = "/home/gpsr/webserver/cgibin/ifnepitope/svm.pl";
    private String perlParm = "/home/gpsr/NetBeansProjects/GAWorkspace";
        
    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {             
            
        if (ind.evaluated)
            return;
        
        // one time running section
        if (FirstTime == 0){
            runningspace = runningspace + "G" + state.generation + ".txt";
            File file = new File(runningspace);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(exp2.class.getName()).log(Level.SEVERE, null, ex);
            }
            FirstTime = 1;
        }
        
        float fitnessValue = 0;
        
        GeneVectorIndividual EpitopeSequence = (GeneVectorIndividual) ind;
        fitnessValue = runPerl(perlParm + "/test.fasta", EpitopeSequence.genotypeToStringForHumans(), runningspace);
        
        SimpleFitness fitness 
                         = (SimpleFitness) EpitopeSequence.fitness;
        fitness.setFitness(state, fitnessValue, 
                fitnessValue == 10000);

        EpitopeSequence.evaluated = true;       
        }

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
            
            ps.println(str.replaceAll(" ", ""));
            
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
            File perlOutput = new File("/home/gpsr/NetBeansProjects/GAWorkspace/out.txt");
            BufferedReader in2 = new BufferedReader(new FileReader(perlOutput)); 
            File gaOutput   = new File(ofn);
            FileWriter ps2 = new FileWriter(gaOutput, true);
            String result = in2.readLine();           
            System.out.println(result);
            
            if (result.equals("")){
               ps2.write(str);
               ps2.append("\r\n");
               fValue = -100;
            }else{
               ps2.write(result);
               fValue = Float.valueOf(result.substring(63, result.length()));
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


