/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_entity;

/**
 *
 * @author gpsr
 */
public class Individual {
    
    private String name;
    private double score;

    public Individual(String name, double score) {
        this.name = name;
        this.score = score;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return this.score;
    }
    
}
