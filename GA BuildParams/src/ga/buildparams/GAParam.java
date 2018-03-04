/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.buildparams;

/**
 *
 * @author gpsr
 */
public class GAParam {
    
    String ParamName;
    String ParamValues[]; 
    
    /**
     *
     * @param ParamName
     * @param ParamValues
     */
    public GAParam(String ParamName, String ParamValues[]){
        this.ParamName = ParamName;
        
        try{
            if (ParamValues[0].contains(".")) {  
                double min  = Double.parseDouble(ParamValues[0]);
                double max  = Double.parseDouble(ParamValues[1]);
                double step = Double.parseDouble(ParamValues[2]);
                int    num  = getMyInt((double) (max-min)/step);
            
                String[] tpv = new String[num];
            
                double tmp = min;
            
                for (int i=0; i<num; i++) {
                    if (tmp >= max) {
                        tpv[i] = max + "";
                        break;
                    } else {
                        tpv[i] = tmp + "";
                    }
                    tmp = tmp + step;
                }
                
                this.ParamValues = tpv;
            } else {
                int min  = Integer.parseInt(ParamValues[0]);
                int max  = Integer.parseInt(ParamValues[1]);
                int step = Integer.parseInt(ParamValues[2]);
                int num  = getMyInt((double) (max-min)/step);
            
                String[] tpv = new String[num];
            
                int tmp = min;
            
                for (int i=0; i<num; i++) {
                    if (tmp >= max) {
                        tpv[i] = max + "";
                        break;
                    } else {
                        tpv[i] = tmp + "";
                    }
                    tmp = tmp + step;
                }
                
                this.ParamValues = tpv;
              
            }
             
        } catch (Exception e) {
            this.ParamValues = ParamValues;
        } finally {
        
        }
        
    }
    
    public void print(){
        System.out.print(this.ParamName + " : ");
        for (String ParamValue : this.ParamValues) {
            System.out.print(ParamValue + ", ");
        }
        System.out.println();
    }
    
    private int getMyInt(double a) {
        int myInt = 0;
        double tmp;
        try {
            String tmpStr = a + "";
            tmpStr = tmpStr.substring(0, tmpStr.indexOf("."));
            tmp = Double.parseDouble(tmpStr);
            if (tmp == a) {
                myInt = (int) a + 1;
            } else {
                myInt = ((int) tmp) + 2;
            
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return myInt;
    }
    
}
