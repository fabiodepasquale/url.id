# URL.id - URL Shortener

[![Hbase](https://img.shields.io/badge/hbase-1.1.0.1-orange.svg)](http://hbase.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.4-brightgreen.svg)](http://projects.spring.io/spring-boot/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-3.3.5-brightgreen.svg)](http://getbootstrap.com/)
[![AngularJS](https://img.shields.io/badge/AngularJS-1.4.1-blue.svg)](https://angularjs.org/)

In order to use our URL Shortener you need to have `boot2docker` or `docker-machine` installed.
Then you can choose to use automatic download of the docker image from our repository or build it yourself from the source.

###Download image from repository:

#####1. Start boot2docker
```
boot2docker start
boot2docker ssh
```
#####2. Pull the image
```
docker pull rehelpstudio/url.id
```
#####3. Follow post install instructions

###Build from source:

#####1. Start boot2docker
```
boot2docker start
boot2docker ssh
```
#####2. Clone repository with Dockerfile
```
git clone https://github.com/alex-krestin/uid-docker.git
```
#####3. Build docker image
```
docker build --tag=url.id:latest uid-docker/
```
#####4. Follow post install instructions

##Post Install:
#####Start container (install from repository)
```
docker run -d -p 2181:2181 -p 60010:60010 -p 60000:60000 -p 60020:60020 -p 60030:60030 -p 8080:8080 --name uid -h uid rehelpstudio/url.id
```
#####Start container (install from source)
```
docker run -d -p 2181:2181 -p 60010:60010 -p 60000:60000 -p 60020:60020 -p 60030:60030 -p 8080:8080 --name uid -h uid url.id
```
#####Enter in container
```
docker exec -it uid bash
```
#####First run
Firstly you need to initialize your database before running web server.
```bash
> cd uid
> java -jar uid-1.0.jar -i
```
If you got an error during this operation, please control if you have HBase running
```bash
> jps
> start-hbase.sh
```
After succesfully initialization you can start normally use the application. For starting web and API server use the next command
```bash
> java -jar uid-1.0.jar -a
```
Then go to `http://localhost:8080` in your browser.

> NOTE:
> If you're using `virtual-box`, don't forget to open port 8080 in order to comunicate with API and client GUI.

