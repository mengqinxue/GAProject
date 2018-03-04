package ga.app.app1;

import ec.*;
import ec.util.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyStatistics extends Statistics{
    
// The parameter string and log number of the file for our readable population
    public static final String P_POPFILE = "pop-file";
    public int popLog;

    // The parameter string and log number of the file for our best-genome-#3 individual
    public static final String P_INFOFILE = "info-file";
    public int infoLog;
    
    // private parameters
    public  static final String P_REPLACE_WITH_BEST = "replace-with-best";
    public  static final String P_TMP_FILE_PATH = "tmp-file-path"; 
    private String replace_with_best_flag = ""; 
    private String tmp_file_path = "";
    
    // customised log files
    public  static final String P_GENERATION_LOGS = "generation-log-folder";
    public  String generation_log_folder = "";
    
    @Override
    public void setup(final EvolutionState state, final Parameter base)
        {
        // DO NOT FORGET to call super.setup(...) !!
        super.setup(state,base);

        // set up popFile
        File popFile = state.parameters.getFile(base.push(P_POPFILE),null);
        if (popFile!=null) 
            try{
                popLog = state.output.addLog(popFile,true);
            }catch (IOException i){
                state.output.fatal("An IOException occurred while trying to create the log " + popFile + ":\n" + i);
            }

        // set up infoFile
        File infoFile = state.parameters.getFile(base.push(P_INFOFILE),null);
        if (infoFile!=null) 
            try{
                infoLog = state.output.addLog(infoFile,true);
            }catch (IOException i){
                state.output.fatal("An IOException occurred while trying to create the log " + infoFile + ":\n" + i);
            }
        
        // set up infoFile
        replace_with_best_flag = state.parameters.getString(base.push(P_REPLACE_WITH_BEST),null);
        tmp_file_path = state.parameters.getString(base.push(P_TMP_FILE_PATH),null);
        generation_log_folder = state.parameters.getString(base.push(P_GENERATION_LOGS),null);
        }
    
    @Override
    public void preEvaluationStatistics(final EvolutionState state){
        if ((state.generation != 0) & (replace_with_best_flag!=null) & (tmp_file_path != null)) {
            //System.out.println("preEvaluation");
            //System.out.println(state.population.subpops[0].individuals[0].genotypeToString());
            //System.out.println(state.population.subpops[0].individuals[0].fitness.fitness());
            Individual ind = state.population.subpops[0].individuals[0];
            
            DataInputStream in;
            try {
                in = new DataInputStream(new FileInputStream(new File(tmp_file_path)));
                ind.readGenotype(state, in);
                in.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MyStatistics.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MyStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    @Override
    public void postEvaluationStatistics(final EvolutionState state){
        // be certain to call the hook on super!
        super.postEvaluationStatistics(state);

        // write out a warning that the next generation is coming 
        state.output.println("-----------------------\nGENERATION " + state.generation + "\n-----------------------", popLog);

        // print out the population 
        state.population.printPopulation(state, popLog);

        // ---------------------------------------------------------------------
        // print the best fitness of sub population 0.
        // ---------------------------------------------------------------------
        Individual best_i = findBest(state);
        Individual worst_i = findWorst(state);
        
        if ((replace_with_best_flag!=null) & (tmp_file_path != null)){
            try {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(tmp_file_path));
                //PrintWriter pw = new PrintWriter(new File(tmp_file_path));
                best_i.writeGenotype(state, out);
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MyStatistics.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MyStatistics.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // ---------------------------------------------------------------------
        // print generations
        // ---------------------------------------------------------------------
        File outFolder = new File(generation_log_folder);
        if (state.generation == 0) {
            if (outFolder.exists() & outFolder.isDirectory()){
                delAllFile(generation_log_folder);}
            outFolder.mkdirs();
            try {
                BufferedWriter out=new BufferedWriter(new FileWriter(generation_log_folder + "Summary.csv"));
                out.write("Generation, Best_Individual, Best_Fitness, Worst_Individual, Worst_Fitness, Avg_Fitness, Var_Fitness, StdDev_Fitness, Med_Fitness");
                out.newLine();
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(generation_log_folder + "G" + state.generation + ".txt"));
            for(int i=0; i<state.population.subpops[0].individuals.length; i++){
                out.write(state.population.subpops[0].individuals[i].genotypeToStringForHumans().replaceAll(" ", ""));
                out.write(", ");
                out.write("" + state.population.subpops[0].individuals[i].fitness.fitness());
                out.newLine();
            }
            out.close();
            out = new BufferedWriter(new FileWriter(generation_log_folder + "Summary.csv", true));
            out.write(state.generation + ", ");
            out.write(best_i.genotypeToStringForHumans().replaceAll(" ", "") + ", ");
            out.write(best_i.fitness.fitness() + ", ");
            out.write(worst_i.genotypeToStringForHumans().replaceAll(" ", "") + ", ");
            out.write(worst_i.fitness.fitness() + ", ");
            out.write(avgFitness(state) + ", ");
            out.write(varFitness(state) + ", ");
            out.write(stdFitness(state) + ", ");
            out.write(findMedian(state) + "");
            out.newLine();
            out.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MyStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    // Find best individual
    private Individual findBest(final EvolutionState state){
        Individual best_i = state.population.subpops[0].individuals[0];
        
        for(int i=1; i<state.population.subpops[0].individuals.length; i++){
            if (state.population.subpops[0].individuals[i].fitness.betterThan(best_i.fitness))
                    best_i = state.population.subpops[0].individuals[i];
        }
        
        return best_i;
    }
    
    // Worst Individual 
    private Individual findWorst(final EvolutionState state){
        Individual worst_i = state.population.subpops[0].individuals[0];
        
        for(int i=1; i<state.population.subpops[0].individuals.length; i++){
            if (!state.population.subpops[0].individuals[i].fitness.betterThan(worst_i.fitness))
                    worst_i = state.population.subpops[0].individuals[i];
        }
        
        return worst_i;
    }
    
    // Worst Median 
    private double findMedian(final EvolutionState state){
        double med = 0;
        
        double[] tmp = new double[state.population.subpops[0].individuals.length]; 
        
        for(int i=1; i<state.population.subpops[0].individuals.length; i++){
            tmp[i] = state.population.subpops[0].individuals[i].fitness.fitness();
        }
        
        Arrays.sort(tmp);
        
        if (tmp.length % 2 == 0){
            med = (tmp[(tmp.length / 2) - 1] + tmp[(tmp.length / 2)]) / 2 ;
        } else {
            med = tmp[tmp.length / 2];
        }
        
        return med;
    }
    
    // Average fitness 
    private double avgFitness(final EvolutionState state){
        double avg = 0; 
        for(int i=1; i<state.population.subpops[0].individuals.length; i++){
            avg += state.population.subpops[0].individuals[i].fitness.fitness();
        }
        return avg / state.population.subpops[0].individuals.length;
    }
    
    // Variance
    private double varFitness(final EvolutionState state){
        double var = 0;
        double avg = avgFitness(state);
        
        for(int i=1; i<state.population.subpops[0].individuals.length; i++){
            double tmp = state.population.subpops[0].individuals[i].fitness.fitness();
            var += (avg - tmp) * (avg - tmp);
        }

        return var/state.population.subpops[0].individuals.length;
    }
    
    // Std Dev
    private double stdFitness(final EvolutionState state){
        return Math.sqrt(varFitness(state));
    }
    
    
//Delete folders and its containing files.
    private void delAllFile(String path) {
       File file = new File(path);
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
//           System.out.println(tempList[i]);
           temp = new File(path + tempList[i]);
           temp.delete();
       }
       file.delete();
     }
}
