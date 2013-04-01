import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import shared.DataSet;
import shared.DataSetDescription;
import shared.DataSetWriter;
import shared.filt.LabelSplitFilter;
import shared.filt.ReversibleFilter;
import shared.reader.ArffDataSetReader;


public class AFilterRunner {

    private static final String DATA_FOLDER = "data/";
    private String dataFilePath;
    private String fileName;

    public AFilterRunner(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.fileName     = getFileNameNoExt(this.dataFilePath);
        
        //create the data folder if it doesnt exist already
        new File(DATA_FOLDER).mkdir();
    }

    /**
     * @param filePath 
     * 
     */
    private String getFileNameNoExt(String filePath) {
        String fileName = new File(filePath).getName();
        int dotInx = fileName.lastIndexOf(".");
        if (dotInx >= 0) {
            fileName = fileName.substring(0, dotInx);
        }
        return fileName;
    }

    /**
     * @param dataFile
     * @return
     * @throws Exception
     */
    protected DataSet loadDataSet() throws Exception {
        DataSet dataset;
        dataset = (new ArffDataSetReader(this.dataFilePath)).read();
        (new LabelSplitFilter()).filter(dataset);
        return dataset;
    }


    protected OutputStream openLogFile(String ext) throws FileNotFoundException {
        return new FileOutputStream(DATA_FOLDER + this.fileName + ext + ".log");
    }
    
    protected void runFilter(DataSet set, ReversibleFilter filter, String ext) throws IOException {
        filter.filter(set);
        String fileName = DATA_FOLDER + this.fileName + ext + ".arff";
        String[] classNames = getClassNamesFromLabelDescription(set.getLabelDataSet().getDescription());
        writeArffHeader(fileName, set.getDescription(), classNames);
        DataSetWriter wr = new DataSetWriter(set, fileName, true, classNames);
        try {
            wr.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
        filter.reverse(set);
    }

    private void writeArffHeader(String filePath,
            DataSetDescription description, String[] classNames) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filePath, false));
        String relation = getFileNameNoExt(filePath);
        pw.println(String.format("@relation '%s'", relation));
        pw.println();
        for (int ii = 1; ii <= description.getAttributeCount(); ii++) {
            pw.println(String.format("@attribute attr_%d numeric", ii));
        }
        pw.println();
        pw.print("@attribute Classes {");
        boolean firstTime = true;
        for (String className : classNames) {
            if (!firstTime) {
                pw.print(",");
            }
            pw.print(className);
            firstTime = false;
        }
        pw.println("}");
        pw.println();
        pw.println("@data");
        pw.close();
    }
    
    private String[] getClassNamesFromLabelDescription(DataSetDescription labelDescription) {
        String[] classNames = new String[labelDescription.getDiscreteRange()];
        for (int ii = 0; ii < labelDescription.getDiscreteRange(); ii++) {
            classNames[ii] = String.format("class%d", ii);
        }
        return classNames;
    }
}
