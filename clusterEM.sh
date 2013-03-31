java -cp ../weka-3-6-9/weka.jar weka.filters.unsupervised.attribute.AddCluster -W "weka.clusterers.EM -I $3 -N $2 -M 1.0E-6 -S 100" -I last -i $1 -o $1_outEM.arff
