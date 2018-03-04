/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 *
 * @author gpsr
 */
public class GA_Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String ifn = "input.fasta";
        String ofn = "out.txt";
        
        // Call perl script
        String pgm = "perl";
        String perlPath = "/home/gpsr/webserver/cgibin/ifnepitope/svm.pl";
        String parm1 = "/home/gpsr/webserver/cgidocs/tmp/ifnepitope/IFNtest";
        //String parm2 = "";
        String[] perlCmd = {pgm, perlPath, parm1};
        InputStream in = null;
        // Running perl script
        try{
            Process pro = Runtime.getRuntime().exec(perlCmd);
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String result = read.readLine();
            System.out.println("Info:" + result);
        } catch (Exception e){
            System.out.println(e);
        }
        
        System.out.println("The program is finished.");
    }
    
}
