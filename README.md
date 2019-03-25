# Bike_clustering
#############################################################################################

# Problem Statment

Clustering of citybikes stations based on the location. 

Data: Static geographical information of CityBike‘s stations in Brisbane (“Brisbane_CityBike.json”)

Instructions:
Propose a script to perform a clustering based on either the location or characteristics of bike
stations.
The code should be launched by a UNIX like command. It should be developed as close as a real
industrialized process.

Languages: Either Scala or Python3

Deliveries: The delivery of this test should contain:
 Code: A zip file containing all your code or a link to a Github repository
 Readme: command line to launch the job, and short description of your program
 Output: directory with the clustering result
 Backlog: The User Stories &amp; Tasks (done or to do) related to industrializing this code.

## Backlog

STEP 1 : Import Libraries
Importing necessary Spark libraries to process data and solving the problem below

STEP 2 : SparkContext Init
Initiazing the Master Application Spark

STEP 3 : Import data
Importing the JSON DATA of Bike Stations to a Dataset to be processed

STEP 4 : Display Imported data
Displaying the Data to verify and validate the quality of Data

STEP 5 : Creating Features Input by Assembling (latitude and longitude)
As the KMeans Model need a victor as Input to fit the model, we assembly the two components component determining a single station, 
namely latitude and longitude into one vector called "Featues"

STEP 6 : Initializing the KMeans clustering model
we choose the KMeans clustering model because is one of the most commonly used clustering algorithms that clusters the data points 
into a predefined number of clusters. we choose to train the model with 5 clusters as parameter

STEP 7 : Building the Pipeline
Building the pipeline with the Transformer Described in STEP 5 and the Estimator Described in STEP 6

STEP 8 : Running the pipeline
Training the model and predecting the Clusters

STEP 9 : Displaying the clustering results
Displaying the model results

STEP 10 : Saving the results
Saving the model results


# running on the cluster

spark-submit --class Bike_clustering --master yarn \
  $Local_Path_Jar_Dir/bike_clustering_2.11-0.1.jar $HDFS_Path_Input_DIR/Brisbane_CityBike.json $HDFS_Path_Output_DIR $NumberClusters
  
where :
  $Local_Path_Jar_Dir is the local path to find the Jar containing the Bike_Clustering Class and the compagnion class
  $HDFS_Path_Input_DIR is the HDFS path to locate the Input file source Brisbane_CityBike.json
  $HDFS_Path_Output_DIR is the HDFS Output file directory where we will deliver the result of bike clustering 
  $NumberClusters to define the number of clusters choosen

Example :
spark-submit --class Bike_clustering --master yarn \
 --conf spark.ui.port=12567 \
 /home/iharraki/CCA_Ex/bike_clustering_2.11-0.1.jar /user/iharraki/CCA_Ex/CCA_Ex/Brisbane_CityBike.json /user/iharraki/CCA_Ex/Data 5

