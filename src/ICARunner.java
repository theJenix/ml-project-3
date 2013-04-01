import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.PrincipalComponentAnalysis;
import shared.filt.VarianceCounter;
import shared.reader.ArffDataSetReader;
import util.linalg.Matrix;


public class ICARunner extends AFilterRunner {

    public static final void main(String [] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: ICARunner <arff path> <variance thresholds>");
            return;
        }
        new ICARunner(args[0], args[1].split(",")).run();
    }

    private String dataFilePath;
    private List<Double> varThresholds;

    public ICARunner(String dataFilePath, String[] varThresholdStrs) {
        super(dataFilePath);
        this.varThresholds = new ArrayList<Double>();
        for (String thold : varThresholdStrs) {
            this.varThresholds.add(Double.parseDouble(thold));
        }
    }

    public void run() throws Exception {
        DataSet segmentation = null;

        DataSet dataset = loadDataSet();
        
        OutputStream os = openLogFile("_ica");
        PrintWriter writer = new PrintWriter(os);
        //load the dataset just to print out the eigenvalues...these should be the same
        // regardless of the threshold used for dim. reduction
        IndependentComponentAnalysis ica = new IndependentComponentAnalysis(dataset.copy());
        VarianceCounter vc = new VarianceCounter(ica.getPCA().getEigenValues());

        for (double varThold : this.varThresholds) {
            String ext = String.format("_ica_%02.02f", varThold * 100);
            DataSet curset = dataset.copy();
            int toKeep = vc.countLeft(varThold);
            ica = new IndependentComponentAnalysis(curset, toKeep);
            runFilter(curset, ica, ext);
        }
        
        writer.close();
    }
}
