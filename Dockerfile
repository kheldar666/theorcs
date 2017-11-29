#Starting with the CentOS base
FROM centos:7

MAINTAINER Martin Papy <martin.papy@gmail.com>

#Updating Centos first then
#Download Java from Oracle and install it
RUN yum -y update && \
    yum -y install wget && \
    wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u152-b16/aa0333dd3019491ca4f6ddbe78cdb6d0/jdk-8u152-linux-x64.rpm" && \
    yum localinstall -y jdk-8u152-linux-x64.rpm && \
    rm -f jdk-8u152-linux-x64.rpm && \
    yum clean all

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Define default command.
CMD ["bash"]

#Add this Volume for Spring Boot to work properly
VOLUME /tmp

#Now Copy the Artifact
ADD /target/theorcs-4.0.0-SNAPSHOT.jar /theorcs.jar

#Updates the Timestamps on the jar. Useful for avoiding caching issues
RUN sh -c 'touch /theorcs.jar'

#The second argument help Tomcat to start faster
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/theorcs.jar"]

EXPOSE 8080