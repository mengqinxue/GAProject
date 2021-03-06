/*
 * The example in ECJ manual
 * - Section 4.1.1
 */
package ga.test.tc01;

import ec.*;
import ec.simple.*;
import ec.vector.*;

/**
 *
 * @author max
 */
public class tc01 extends Problem implements SimpleProblemForm{
    
    @Override
    public void evaluate(EvolutionState state, Individual ind, int subpopulation, int thread){
        
        if (ind.evaluated) return;
        
        if (!(ind instanceof IntegerVectorIndividual))
            state.output.fatal("Whoa~ It's not an IntegerVectorIndividual!!!");
        
        int[] genome = ((IntegerVectorIndividual)ind).genome;
        double product = 1.0;
        
        for (int x=0; x<genome.length; x++)
            product = product * genome[x];
    
        ((SimpleFitness)ind.fitness).setFitness(state, product, product == Double.POSITIVE_INFINITY);
        
        ind.evaluated = true;
    }
    
}
