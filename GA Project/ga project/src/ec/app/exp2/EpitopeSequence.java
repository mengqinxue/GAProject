/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.app.exp2;

import ec.EvolutionState;
import ec.util.Parameter;
import ec.vector.VectorGene;
import java.io.*;

/**
 * @author max
 */
public class EpitopeSequence extends VectorGene {
    public  final static String FILE_PATH = "filepath";
    public  static int         a = -1;
    public  static int         b = 0;
    private char               allele;
    private String             filepath; 
    private int                r_count;

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        Parameter def = defaultBase();
        
        // Clear runningspace folder
        deleteFile("/home/gpsr/NetBeansProjects/GARunningSpace/");
        
        filepath = state.parameters.getStringWithDefault(base.push(FILE_PATH), def.push(FILE_PATH), "");
        if (filepath.length() == 0)
            state.output.fatal("Specify the file path.", base.push(FILE_PATH));

        try{
            DataInputStream in = new DataInputStream(new FileInputStream(new File(filepath)));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            while(bufferedreader.readLine()!= null){  
                r_count++;
            }
            in.close();
            
        }catch (IOException e){
            System.out.println(e);
        }

    }

    /*
     * (non-Javadoc)
     * @see ec.vector.VectorGene#reset(ec.EvolutionState, int)
     */
    @Override
    public void reset(EvolutionState state, int thread) {
        if (a == -1){
            b = state.random[thread].nextInt(r_count);
            a++;
        } 
        else if (a == 15) {a = -1;}
        else {
            //System.out.println("a = " + a);
                        
            int count = 0;
            // System.out.println("reset");
            try{
                DataInputStream in = new DataInputStream(new FileInputStream(new File(filepath)));
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String tmp_str;
                while((tmp_str = bufferedreader.readLine())!= null){                  
                    if (count == b){
                        allele = tmp_str.charAt(a);
                        //System.out.println(allele);
                        //System.out.println("b = " + b);
                        a++;
                        break;
                }
                    count++;
                }
                in.close();
            }catch (IOException e){
                System.out.println(e);
            }           
        }
    }

    public char getAllele() {
        return allele;        
    }

    /*
     * (non-Javadoc)
     * @see ec.vector.VectorGene#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (!this.getClass().isInstance(other)) {
            return false;
        }
        EpitopeSequence that = (EpitopeSequence) other;
        return allele == that.allele;
    }

    /*
     * @see ec.vector.VectorGene#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = this.getClass().hashCode();
        return hash;
    }

    @Override
    public Object clone() {
        EpitopeSequence charVectorGene = (EpitopeSequence) (super.clone());
        return charVectorGene;
    }

    @Override
    public String toString() {
        return Character.toString(allele);
    }
    
    private void deleteFile(String path){
        File file = new File(path);
        File[] ff = file.listFiles();
        for (int i=0; i<ff.length; i++){
            ff[i].delete();
        }
    }
}
