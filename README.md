#PhotoShow
--- 

## How to run? 
- JDK version: 11
```shell
java -version

```
- config store root path
```properties
store.root.path=/home/test/photo
```
- config watermark pic: add a pic as watermark in below path
```
${store.root.path}/cfg/watermark.png
```
- Compile and use it
```shell
mvn clean package
cd target
java -jar photoShow-1.0-SNAPSHOT-jar-with-dependencies.jar
```
- click http://localhost:13319/main.html and you will see it.

## how to upload?
```shell
curl -F "desc=描述" -F "pics=@${image path}" -F "pics=@${image path}" http://localhost:13319/pic/upload

```
