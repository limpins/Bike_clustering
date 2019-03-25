object Bike_clustering {
  def main(args: Array[String]): Unit = {
    //STEP 1 : Import Libraries
    import org.apache.spark.ml.clustering.KMeans
    import org.apache.spark.ml.Pipeline
    import org.apache.spark.sql.{SparkSession, SaveMode}
    import org.apache.spark.ml.feature.VectorAssembler

    // STEP 2 : SparkContext Init
    val spark = SparkSession.
      builder().
      appName("bike-clustering").
      config("spark.master","local[*]").
      getOrCreate()

    val InputDir = args(0)
    val Output_Dir = args(1)
    val NumKMeans = args(2).toInt


    // STEP 3 : Import data
    val data = spark.read.json(InputDir)

    // STEP 4 : Display Imported data
    data.show(10)

    // STEP 5 : Creating Features Input by Assembling (latitude and longitude)
    val Featuring = new VectorAssembler().setInputCols(Array("latitude","longitude")).setOutputCol("features")

    // STEP 6 : Initializing the KMeans clustering model
    val kmeans = new KMeans().setK(NumKMeans).setFeaturesCol("features").setPredictionCol("cluster")

    // STEP 7 : Building the Pipeline
    val pipeline = new Pipeline().setStages(Array(Featuring, kmeans))

    // STEP 8 : Running the pipeline
    val model = pipeline.fit(data)

    val clusterResults = model.transform(data)

    // STEP 9 : Displaying the clustering results
    clusterResults.drop("features").show()

    // STEP 10 : Saving clustering results
    clusterResults.drop("features").write.
    mode(SaveMode.Overwrite).
    format("com.databricks.spark.csv").option("header", "true").option("delimiter", ";").save(Output_Dir)

    spark.close()
  }

}
