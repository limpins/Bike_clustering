//STEP 1 : Import Libraries
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{SparkSession, SaveMode}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._

// STEP 2 : SparkContext Init
val spark = SparkSession.
  builder().
  appName("bike-clustering").
  config("spark.master","local[*]").
  getOrCreate()

import spark.implicits._

val InputDir = "C:\\Users\\Administrator\\Documents\\Scala Course\\Bike_clustering\\src\\main\\resources"

/*
// STEP 3 : Import data
val schema = new StructType()
  .add("id",IntegerType,true)
  .add("name",StringType,true)
  .add("address",StringType,true)
  .add("latitude",DoubleType,true)
  .add("longitude",DoubleType,true)


    val data = spark.read.format("json").
      option("mode", "DROPMALFORMED").
      schema(schema).
      load(InputDir)*/

val data = spark.read.json("C:\\Users\\Administrator\\Documents\\Scala Course\\Bike_clustering\\src\\main\\resources")




// STEP 4 : Display Imported data
//data.show(10)

// STEP 5 : Creating Features Input by Assembling (latitude and longitude)
val Featuring = new VectorAssembler().setInputCols(Array("latitude","longitude")).setOutputCol("features")

// STEP 6 : Initializing the KMeans clustering model
val kmeans = new KMeans().setK(5).setFeaturesCol("features").setPredictionCol("cluster")

// STEP 7 : Building the Pipeline
val pipeline = new Pipeline().setStages(Array(Featuring, kmeans))

// STEP 8 : Running the pipeline
val model = pipeline.fit(data)

val clusterResults = model.transform(data)

// STEP 9 : Displaying the clustering results
clusterResults.drop("features").show()

// STEP 10 : Saving the results

  //rdd.saveAsTextFile("C:\\Users\\Administrator\\Documents\\Scala Course\\data")
  //.write
  //.mode(SaveMode.Overwrite)
  //.format("com.databricks.spark.csv").option("header", "true").option("delimiter", ";").save("C:\\Users\\Administrator\\Documents\\Scala Course\\data\\result.csv")

/*
clusterResults.drop("features").collect().foreach {case bike(address : String, latitude : Double, longitude : Double, name : String, number : Int, cluster : Int) =>
    println(s"($address,$latitude,$longitude,$name,$number,$cluster) --> address=$address, latitude=$latitude, longitude=$longitude, name=$name, number=$number, cluster=$cluster")
}*/
/*
case class bike(address : String, latitude : Double, longitude : Double, name : String, number : Int, cluster : Int)
clusterResults.rdd.map { line => bike(line(0).toString(), line(1).t, line(2), line(3).toString(), line(4).toString(), line(5).toString()) }.
  toDF.write.csv("C:\\\\\\\\Users\\\\\\\\Administrator\\\\\\\\Documents\\\\\\\\Scala Course\\\\\\\\data")
//clusterResults.write.format("csv").save("C:\\\\Users\\\\Administrator\\\\Documents\\\\Scala Course\\\\data")
*/

clusterResults.drop("features").printSchema()
clusterResults.rdd.map(line => line(0) + "," + line(1) + "," + line(2) + "," + line(3) + "," + line(4) + "," + line(5) + ";")
  .saveAsTextFile("C://Users//Administrator//Documents//Scala Course//data3")

/*
val stringify = udf((vs: Seq[String]) => s"""[${vs.mkString(",")}]""")
clusterResults.withColumn("address", stringify($"address"))
  .withColumn("latitude", stringify($"latitude"))
  .withColumn("longitude", stringify($"longitude"))
  .withColumn("name", stringify($"name"))
  .withColumn("number", stringify($"number"))
  .withColumn("cluster", stringify($"cluster"))
  .write.csv("C:\\\\\\\\Users\\\\\\\\Administrator\\\\\\\\Documents\\\\\\\\Scala Course\\\\\\\\data")
*/

/*
def stringify(c: Column) = concat(lit("["), concat_ws(",", c), lit("]"))

clusterResults.withColumn("res1", stringify($"res1"))
  .write.csv("C:\\\\\\\\Users\\\\\\\\Administrator\\\\\\\\Documents\\\\\\\\Scala Course\\\\\\\\data")
*/

/*
def stringifyArrays(dataFrame: DataFrame): DataFrame = {
  val colsToStringify = dataFrame.schema.filter(p => p.dataType.typeName == "array").map(p => p.name)

  colsToStringify.foldLeft(dataFrame)((df, c) => {
    df.withColumn(c, concat(lit("["), concat_ws(", ", col(c).cast("array<string>")), lit("]")))
  })
}

val result = stringifyArrays(clusterResults)
result.collect().foreach(println)
*/
spark.close()