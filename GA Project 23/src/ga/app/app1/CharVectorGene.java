/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.app.app1;

import ec.EvolutionState;
import ec.util.Code;
import ec.util.DecodeReturn;
import ec.util.Parameter;
import ec.vector.Gene;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author max
 */
public class CharVectorGene extends Gene{

    public final static String P_ALPHABET = "alphabet";
    
    static char[]      alphabet;
    char               allele;
    
    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);

        Parameter def = defaultBase();

        String alphabetStr = state.parameters.getStringWithDefault(
                          base.push(P_ALPHABET), def.push(P_ALPHABET), "");

        if (alphabetStr.length() == 0)
            state.output.fatal(
                       "CharVectorGene must have a default alphabet", 
                       base.push(P_ALPHABET));

        alphabet = alphabetStr.toCharArray();
    }
    
    @Override
    public void reset(EvolutionState state, int thread) {
        int idx = state.random[thread].nextInt(alphabet.length);
        allele = alphabet[idx];
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && other instanceof CharVectorGene &&
                ((CharVectorGene)other).allele == allele);
    }

    @Override
    public int hashCode() {
        int hash = this.getClass().hashCode();
        
        hash = (hash << 1 | hash >>> 31) ^ allele;
        
        return hash;
    }
    
    
    @Override
    public String toString() {
        return Character.toString(allele);
    }
    
    @Override
    public void readGeneFromString(String string, EvolutionState state){
        allele = string.charAt(0);
    }
    
    @Override
    public void readGene(EvolutionState state, DataInput in) throws IOException{
        allele = in.readChar();
    }
    
    @Override
    public void writeGene(EvolutionState state, DataOutput out) throws IOException{
        out.writeChar(allele);
    }
    
}
