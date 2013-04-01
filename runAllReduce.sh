weka_dir=../weka-3-6-9
echo 'Vote dataset...'
java -cp ABAGAIL.jar:project3.jar PCARunner $weka_dir/data/vote.arff 0.6,0.8,1.0 
java -cp ABAGAIL.jar:project3.jar ICARunner $weka_dir/data/vote.arff 0.6,0.8,1.0 
java -cp ABAGAIL.jar:project3.jar RPRunner $weka_dir/data/vote.arff 2,7,16 
java -cp ABAGAIL.jar:project3.jar InsigRunner $weka_dir/data/vote.arff 0.6,0.8,1.0 

echo 'Segmentation dataset...'
java -cp ABAGAIL.jar:project3.jar PCARunner $weka_dir/data/segmentation-train.arff 0.6,0.8,1.0 
java -cp ABAGAIL.jar:project3.jar ICARunner $weka_dir/data/segmentation-train.arff 0.6,0.8,1.0 
java -cp ABAGAIL.jar:project3.jar RPRunner $weka_dir/data/segmentation-train.arff 1,2,19 
java -cp ABAGAIL.jar:project3.jar InsigRunner $weka_dir/data/segmentation-train.arff 0.6,0.8,1.0 
