weka_dir=../weka-3-6-9
java -cp ../weka-3-6-9/weka.jar weka.filters.unsupervised.attribute.AddCluster -W "weka.clusterers.SimpleKMeans -N $2 -S 42 -I $3 " -I last -i $weka_dir/data/$1 -o $1_out_$3.arff
