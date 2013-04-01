import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shared.DataSet;
import shared.Instance;
import shared.filt.LabelSplitFilter;
import shared.reader.ArffDataSetReader;
import shared.tester.ConfusionMatrixTestMetric;

/**
 * A quick app to process Clusterer results (from Weka) using the Confusion Matrix test metric
 * to present a label vs cluster histogram.  Labels are represented in the rows, clusters in
 * columns.
 * 
 * @author Jesse Rosalia
 *
 */
public class ClusterResults {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: KMeansResults <arff path>");
            return;
        }

        DataSet dataset;
        dataset = (new ArffDataSetReader(args[0])).read();
        (new LabelSplitFilter()).filter(dataset);
        
        ConfusionMatrixTestMetric testMetric = new ConfusionMatrixTestMetric(dataset.getLabelDataSet().getDescription());
        for (Instance inst : dataset) {
            testMetric.addResult(inst.getLabel(), new Instance(inst.getDiscrete(inst.size() - 1)));
        }
        testMetric.printResults();
    }
}
