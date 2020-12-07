
# QA-Platform

## Overview
LandSense QA-Platform for setting up a REST service of quality assurance and assessment analyses. This repository contains a maven module for the service (`wps-rest`), Java QA / processing algorithms (`wps-app`) and [QA-R-Scripts](https://github.com/LandSense/QA-R-Scripts) (which run locally). The R scripts are already cloned from the [QA-R-Script](https://github.com/LandSense/QA-R-Scripts) repository into the `resources` of `wps-rest` (i.e. `QA-Platform/wps-rest/src/main/resources/rscripts`). Integration with a separate HTTP service for detecting faces in photographs is through configuration of `FaceDetectorHTTPService.java` class.
## Requirements
For running the QA-Platform:
 - Java.
 - Tomcat (or other servlet container).
 
To support running the scripts from QA-R-Scripts (optional):
 - R. The [RScript](https://www.rdocumentation.org/packages/utils/versions/3.5.2/topics/Rscript) binary, which should come with the standard R distribution, must be configured as an environment variable (i.e. [RScript](https://www.rdocumentation.org/packages/utils/versions/3.5.2/topics/Rscript) can be called from any shell location).
 
To support running the face detection (for blurring) service (optional):
 - The [Dockerized-Object-Recognition](https://github.com/LandSense/Dockerized-Object-Recognition) service should be running somewhere. 

To support running the vehicle licence plate detection (for blurring) (optional):
 - [OpenALPR](https://github.com/openalpr) needs to be installed locally. This project was tested with v2.3.0 (on Windows) and uses the OpenALPR java bindings. Download a binary [OpenALPR Releases](https://github.com/openalpr/openalpr/releases), unzip to somewhere on the local system and add as a system path variable (e.g. C:\openalpr_64\). 


## Installation for development
1. Clone this repository.
2. Import the code as a multi-module maven project. I do this in Eclipse. 
3. (Optional) Modify FaceDetectorHTTPService.java to point to a running instance of the `Dockerized-Object-Recognition` service. I.e. modify the following line with the correct URL and port:
`private final static String myService = "";` 
4. Deploy `wps-rest` into Tomcat. This can be done in Eclipse (Run As -> Run on Server) if the Tomcat server is configured or it can be exported as a .war file.

## Deployment on headless server 
1. Clone this repository.
2. `cd QA-Platform`
3. `sudo mvn -Dmaven.test.skip=true package`
4. `sudo mv QA-Platform/wps-rest/target/wps-rest.war /opt/tomcat/apache-tomcat-7.0.90/webapps/`
5. Modify the web.xml swagger configuration (e.g `/opt/tomcat/apache-tomcat-7.0.90/webapps/wps-rest/WEB-INF/`) to have the correct host (e.g. not localhost).
6. `sudo /opt/tomcat/apache-tomcat-7.0.90/bin/startup.sh`

## Example usage
Test the REST interface is running on:
[http://localhost:8080/wps-rest/swagger/index.html](http://localhost:8080/wps-rest/swagger/index.html)

Test calling the laplaceblurcheck from cURL:
`curl -X POST "http://localhost:8080/wps-rest/service/test/laplaceblurcheck?threshold=100" -H "accept: application/json" -H "Authorization: Bearer my-key" -H "Content-Type: application/json" -d "\"https://landsense.eu/img/news/2018-11-21_year2.jpg\""`

Test whether R is working (returns some system details - bit of a security risk I suppose) from cURL:
`curl -X POST "http://localhost:8080/wps-rest/service/rintegration/rsystemcall" -H "accept: application/json" -H "Authorization: Bearer secret-key" -H "Content-Type: application/json" -d "\"Bearer secret-key\""`

## Adding further R scripts
The R script integration is achieved through adding argument parsing to the script so that it can be executed completely from a terminal using [RScript.](https://www.rdocumentation.org/packages/utils/versions/3.5.2/topics/Rscript) 
The code for enabling calling and exposing of the scripts via the REST interface is in `rintegration` package. The R scripts themselves can be added to the resources directory (`wps-rest/src/main/resources/rscripts/`). A Java class, a POJO for the result data and some configuration of the RIntegrationService code is also required. For example, the LandUseQA test comprises:
 - `wps-rest/src/main/resources/rscripts/LandUse_qa_v1.R`
 - `wps-rest/src/main/java/rintegration/LandUseQA.java`
 - `wps-rest/src/main/java/rintegration/LandUseQAInfo.java`
 
Then, these also need to have the LandUse test details added to them, in the same way as achieved for adding a Java test:
 - `wps-rest/src/main/java/rintegration/RIntegrationResource.java`
 - `wps-rest/src/main/java/rintegration/RIntegrationService.java`
 - `wps-rest/src/main/java/rintegration/RIntegrationServiceImpl.java`


## Troubleshooting
During development / debugging, Tomcat starts complaining it can't find something e.g. ClassNotFoundException or wps_config.xml isn't seen in WEB-INF. Quickest solution is start a new development server (Tomcat V7)...   
