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
            System.out.println("Usage: RPRunner <arff path> <components to keep>");
            return;
        }
        new RPRunner(args[0], args[1].split(",")).run();
    }

    private String dataFilePath;
    private List<Integer> toKeepList;

    public RPRunner(String dataFilePath, String[] toKeepStrs) {
        super(dataFilePath);
        this.toKeepList = new ArrayList<Integer>();
        for (String toKeepStr : toKeepStrs) {
            this.toKeepList.add(Integer.parseInt(toKeepStr));
        }
    }

    public void run() throws Exception {
        DataSet segmentation = null;

        DataSet dataset = loadDataSet();
        
        OutputStream os = openLogFile("_ica");
        PrintWriter writer = new PrintWriter(os);
        //load the dataset just to print out the eigenvalues...these should be the same
        // regardless of the threshold used for dim. reduction
        for (int toKeep : this.toKeepList) {
            String ext = String.format("_rp_%d", toKeep);
            DataSet curset = dataset.copy();
            RandomizedProjectionFilter rp = new RandomizedProjectionFilter(toKeep, dataset.getDescription().getAttributeCount());
            runFilter(curset, rp, ext);
        }
        
        writer.close();
    }
}
