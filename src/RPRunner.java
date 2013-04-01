import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.PrincipalComponentAnalysis;
import shared.filt.RandomizedProjectionFilter;
import shared.filt.VarianceCounter;
import shared.reader.ArffDataSetReader;
import util.linalg.Matrix;


public class RPRunner extends AFilterRunner {

    public static final void main(String [] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: RPRunner <arff path> <variance thresholds>");
            return;
        }
        new RPRunner(args[0], args[1].split(",")).run();
    }

    private String dataFilePath;
    private List<Double> varThresholds;

    public RPRunner(String dataFilePath, String[] varThresholdStrs) {
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
        RandomizedProjectionFilter rp = new RandomizedProjectionFilter(dataset.copy());
        VarianceCounter vc = new VarianceCounter(ica.getPCA().getEigenValues());

//        writer.println(.getEigenValues());
        for (double varThold : this.varThresholds) {
            String ext = String.format("_ica_%02.02f", varThold * 100);
            DataSet curset = dataset.copy();
            int toKeep = vc.countLeft(varThold);
            ica = new IndependentComponentAnalysis(curset, toKeep);
            runFilter(curset, ica, ext);
        }
        
        writer.close();

//        ArffDataSetReader reader = new ArffDataSetReader(this.dataFilePath);
//        DataSet set = reader.read();
//
//        System.out.println("Before PCA");
//        System.out.println(set);
//        PrincipalComponentAnalysis filter = new PrincipalComponentAnalysis(set);
//        System.out.println(filter.getEigenValues());
//        System.out.println(filter.getProjection().transpose());
//        filter.filter(set);
//        System.out.println("After PCA");
//        System.out.println(set);
//        Matrix reverse = filter.getProjection().transpose();
//        for (int i = 0; i < set.size(); i++) {
//            Instance instance = set.get(i);
//            instance.setData(reverse.times(instance.getData()).plus(filter.getMean()));
//        }
//        System.out.println("After reconstructing");
//        System.out.println(set);
    }
}
