/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_report;

import ga_entity.Generation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.DefaultXYDataset;

/**
 *
 * @author gpsr
 */
public class GALineChart {    
    ChartPanel lframe;
    
    public GALineChart(Generation[] gs, int mod){
        DefaultXYDataset ds = new DefaultXYDataset();
        double[][] datagroup = new double[2][gs.length];
        String title = "title";
        String xName = "Generation";
        String yName = "Value";
        
        switch(mod){
            case 1:
                for (int i=0; i<gs.length; i++){
                    datagroup[0][i] = i;
                    datagroup[1][i] = gs[i].getAvg();
                }
                title = "Average scores of generations";
                yName = "Score";
                break;
            case 2:
                for (int i=0; i<gs.length; i++){
                    datagroup[0][i] = i;
                    datagroup[1][i] = gs[i].getMax();
                }
                title = "Maximum scores of generations";
                yName = "Score";
                break;
            case 3:
                for (int i=0; i<gs.length; i++){
                    datagroup[0][i] = i;
                    datagroup[1][i] = gs[i].getStd();
                }
                title = "Standard deviation of generations";
                yName = "Value";
                break;
            case 4:
                for (int i=0; i<gs.length; i++){
                    datagroup[0][i] = i;
                    datagroup[1][i] = gs[i].getCount();
                }
                title = "Population size of generations";
                yName = "Number of population";
                break;      
            default:
                System.out.println("mod is wrong.");
        }
        
        ds.addSeries("GA Project", datagroup);
        
        displayChart(title, xName, yName, ds);
        
    }
    
    private static void displayChart(String title, String xName, String yName, XYDataset ds){
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(title, xName, yName, ds, 
                PlotOrientation.VERTICAL, true, true, true);
        ChartFrame chartFrame = new ChartFrame("Summary", xyLineChart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    
    }
    
}
