# Fusiontables library for Java

The package tries to simplify access to your fusion tables data using Table objects. It allows to query, save and delete data in fusion table while supporting batching operation for insertion, mapping POJO to tables and storing Json objects as a texts into fusion table fields. 

I'm using this library for accessing fusion tables from my app engine projects but it whouldn't be limited to this scenario only - feel free to submit pull requests if you use it in some other way, thanks!

## How to setup your Fusion table

The setup for using Fusiontables from java client is not really user friendly and consists of following steps (I found all of this when googling and it's possible to do this in much more simple way I'm not aware of)

1. Log into your Google cloud manager and generate service account key under your project at https://console.cloud.google.com/apis/credentials (you can use 'App Engine default service account') - save it as p12 (JSON could work too but I haven't tested it)
2. Still in Google cloud manager, enable Fusiontable API https://console.cloud.google.com/apis/api/fusiontables-json.googleapis.com/overview 
3. Create your fusion table by logging to google drive and hitting new button, then selecting 'More' and choosing Google Fusion table (or you can visit https://www.google.com/fusiontables/DataSource?docid=1TfabMnZPa3APgUkFDZs_UN10tOnL_wp1dHr3tVHx and clone it into your google drive from there) 
4. Hit Share button and add app engine user (project-name@appspot.gserviceaccount.com) to your fusion table sharing option with full write/read access

## How to test it

Clone the project, run 'mvn eclipse:eclipse' and import into eclipse. In 'test/integration' you will find three different scenarios and you just need to update Configuration.java with .p12 key file and you service account id and then set the 'id' of your fusion table in Table.java file. 
