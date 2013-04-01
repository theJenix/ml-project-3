import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import shared.DataSet;
import shared.filt.PrincipalComponentAnalysis;


public class PCARunner extends AFilterRunner {

    public static final void main(String [] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: PCARunner <arff path> <variance thresholds>");
            return;
        }
        new PCARunner(args[0], args[1].split(",")).run();
    }

    private String dataFilePath;
    private List<Double> varThresholds;

    public PCARunner(String dataFilePath, String[] varThresholdStrs) {
        super(dataFilePath);
        this.varThresholds = new ArrayList<Double>();
        for (String thold : varThresholdStrs) {
            this.varThresholds.add(Double.parseDouble(thold));
        }
    }

    public void run() throws Exception {
        DataSet segmentation = null;

        DataSet dataset = loadDataSet();
        
        OutputStream os = openLogFile("_pca");
        PrintWriter writer = new PrintWriter(os);
        //load the dataset just to print out the eigenvalues...these should be the same
        // regardless of the threshold used for dim. reduction
        writer.println(new PrincipalComponentAnalysis(dataset.copy()).getEigenValues());
        for (double varThold : this.varThresholds) {
            String ext = String.format("_pca_%02.02f", varThold * 100);
            DataSet curset = dataset.copy();
            PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis(curset, varThold);
            runFilter(curset, pca, ext);
        }
        
        writer.close();
    }
}
