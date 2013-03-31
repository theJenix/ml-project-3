java -cp ../weka-3-6-9/weka.jar weka.filters.unsupervised.attribute.AddCluster -W "weka.clusterers.SimpleKMeans -N $2 -S 42" -I last -i $1 -o $1_out.arff
