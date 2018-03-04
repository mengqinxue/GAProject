/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_entity;

import java.util.ArrayList;

/**
 *
 * @author gpsr
 */
public class Generation extends ArrayList<Individual> {
    
    private double avg = 0;
    private double std = 0;
    private double max = 0;
    private int  count = 0;
    
    public void calc(){
        this.avg = 0;
        this.max = 0;
        this.std = 0;
        this.count = 0;
        avg_score();
        max_score();
        std_score();
        count();
    }
    
    private void avg_score(){
        double sum =0;
        for (int i=0; i<this.size(); i++){
            sum = sum + this.get(i).getScore();
            //System.out.println(this.get(i).getScore());
            //System.out.println(sum);
        }
        this.avg = sum / this.size();
    }
    
    private void max_score(){
        for (int i=0; i<this.size(); i++){
            if(this.get(i).getScore() > this.getMax()){
                this.max = this.get(i).getScore();
            }
        }
    }
    
    private void std_score(){
        for (int i=0; i<this.size(); i++){
            this.std += Math.pow((this.get(i).getScore() - this.getAvg()), 2);
        }
        this.std = Math.sqrt(this.getStd() / this.size());
    }
    
    private void count(){
        for (int i=0; i<this.size(); i++){
            this.count ++;
        }
    }
    /**
     * @return the avg
     */
    public double getAvg() {
        return avg;
    }

    /**
     * @return the std
     */
    public double getStd() {
        return std;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return max;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }
    
}
