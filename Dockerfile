FROM openjdk:17               
VOLUME /tmp                
EXPOSE 8080                  
ADD target/school-0.0.1-SNAPSHOT.jar school-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/school-0.0.1-SNAPSHOT.jar"]              # command to execute jar