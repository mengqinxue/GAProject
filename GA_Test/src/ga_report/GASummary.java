/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_report;

import ga_entity.Generation;
import ga_entity.Individual;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author gpsr
 */
public class GASummary {
    
    private String ifolder_name = "";
    private int num_generation = 0;
    private Generation[] gs; 
    private Individual ind;
    
    public GASummary(String ifolder_name){
        this.ifolder_name = ifolder_name;
        
        File ifolder = new File(ifolder_name);
        
        if (ifolder.isDirectory()){
            File[] filelist = ifolder.listFiles();
            num_generation = filelist.length;
            gs = new Generation[num_generation];
            
            for (int i=0; i<num_generation; i++){
                try{
                    BufferedReader buf = new BufferedReader(new FileReader(ifolder_name+"G"+i+".txt"));                                    
                    gs[i] = new Generation();
                    String str = buf.readLine();
                    while (str != null){        
                        String name = str.substring(28, 43);
                        double score = Double.valueOf(str.substring(63, str.length()));;
                        ind = new Individual(name, score);
                        //System.out.println(ind.getName()+ind.getScore());
                        gs[i].add(ind);
                        str = buf.readLine();
                    }                    
                    buf.close();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
            
            for (int i=0; i<gs.length; i++){
                //System.out.println(gs[i].size());
                //System.out.println(gs[i].get(0).getName());
                //System.out.println(gs[i].get(0).getScore());
                gs[i].calc();
                //System.out.println("Avg: " + gs[i].getAvg());
                //System.out.println("Max: " + gs[i].getMax());
                //System.out.println("Std: " + gs[i].getStd());
            }
            
            GALineChart chart1 = new GALineChart(gs, 1);
            GALineChart chart2 = new GALineChart(gs, 2);
            GALineChart chart3 = new GALineChart(gs, 3);
            //GALineChart chart4 = new GALineChart(gs, 4);
            
        }else{
            System.out.print("The input is not a dir name.");
        }
        
    
        
    }
    
    
    
    
}
