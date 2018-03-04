/*
 * Example in Sec 5.1.3 
 */
package ga.test.tc03;

import ec.EvolutionState;
import ec.util.Code;
import ec.util.DecodeReturn;
import ec.vector.Gene;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author max
 */
public class TrigGene extends Gene{
    
    double x;
    double y;
    
    @Override
    public void reset(EvolutionState state, int thread){
        double alpha = state.random[thread].nextDouble() * Math.PI * 2;
        x = Math.cos(alpha);
        y = Math.sin(alpha);
    }
    
    @Override
    public void mutate(EvolutionState state, int thread){
        double alpha = Math.atan2(y, x);
        double dalpha = (state.random[thread].nextDouble() - 0.5) * Math.PI * 2 / 100.0;
        x = Math.cos(alpha + dalpha); 
        y = Math.sin(alpha + dalpha);
    }
    
    @Override
    public int hashCode(){
        long a = Double.doubleToRawLongBits(x);
        long b = Double.doubleToRawLongBits(y);
        return (int)((a & (int)-1) ^ (a >> 32) ^ (b & (int)-1) ^ (b >> 32));
    }
    
    @Override
    public boolean equals(Object other){
        return (other != null && other instanceof TrigGene && 
                ((TrigGene)other).x == x && ((TrigGene)other).y == y);
    }
    
    @Override
    public String printGeneToStringForHumans() {
        return ">" + x + " " + y;
    }
    
    @Override
    public String printGeneToString() {
        return ">" + Code.encode(x) + " " + Code.encode(x);
    }
    
    @Override
    public void readGeneFromString(String string, EvolutionState state){
        string = string.trim().substring(0);
        DecodeReturn dr = new DecodeReturn(string);
        Code.decode(dr);
        x = dr.d;
        Code.decode(dr);
        y = dr.d;
    }
    
    @Override
    public void writeGene(EvolutionState state, DataOutput out) throws IOException{
        out.writeDouble(x);
        out.writeDouble(y);
    }
    
    @Override
    public void readGene(EvolutionState state, DataInput in) throws IOException{
        x = in.readDouble();
        y = in.readDouble();
    }
}
