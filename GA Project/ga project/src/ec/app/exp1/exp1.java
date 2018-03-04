/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.app.exp1;

import ec.*;
import ec.simple.*;
import ec.vector.*;

public class exp1 extends Problem implements SimpleProblemForm
    {
    private char[] _expected = "XXXXXXXXXXXXXXX".toCharArray();
    
    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {             
            
        if (ind.evaluated)
            return;
        
        int fitnessValue = 0;
        
        GeneVectorIndividual charVectorIndividual
                              = (GeneVectorIndividual) ind;
        System.out.println(charVectorIndividual.genotypeToStringForHumans());
        long length = charVectorIndividual.size();
        for (int i = 0; i < length; i++) {
            CharVectorGene charVectorGene
                      = (CharVectorGene) charVectorIndividual.genome[i];
            char actual = charVectorGene.getAllele();
            if (actual == _expected[i]) {
                fitnessValue += 1;
            }
        }
        
        if (fitnessValue == 15)
        {
            //System.out.println(charVectorIndividual.genotypeToStringForHumans().toString());
        }
        
        SimpleFitness fitness 
                         = (SimpleFitness) charVectorIndividual.fitness;
        fitness.setFitness(state, fitnessValue, 
                fitnessValue == charVectorIndividual.genomeLength());

        charVectorIndividual.evaluated = true;       
        }
}


