import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import shared.DataSet;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.InsignificantComponentAnalysis;
import shared.filt.VarianceCounter;


public class InsigRunner extends AFilterRunner {

    public static final void main(String [] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: InsigRunner <arff path> <variance thresholds>");
            return;
        }
        new InsigRunner(args[0], args[1].split(",")).run();
    }

    private String dataFilePath;
    private List<Double> varThresholds;

    public InsigRunner(String dataFilePath, String[] varThresholdStrs) {
        super(dataFilePath);
        this.varThresholds = new ArrayList<Double>();
        for (String thold : varThresholdStrs) {
            this.varThresholds.add(Double.parseDouble(thold));
        }
    }

    public void run() throws Exception {
        DataSet segmentation = null;

        DataSet dataset = loadDataSet();
        
        OutputStream os = openLogFile("_insig");
        PrintWriter writer = new PrintWriter(os);
        //load the dataset just to print out the eigenvalues...these should be the same
        // regardless of the threshold used for dim. reduction
        InsignificantComponentAnalysis insig = new InsignificantComponentAnalysis(dataset.copy());
        for (double varThold : this.varThresholds) {
            String ext = String.format("_insig_%02.02f", varThold * 100);
            DataSet curset = dataset.copy();
            insig = new InsignificantComponentAnalysis(curset, varThold);
            runFilter(curset, insig, ext);
        }
        
        writer.close();
    }
}
