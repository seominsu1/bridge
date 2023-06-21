FROM khipu/openjdk17-alpine
WORKDIR /usr/local/src
# java17 install
RUN yum -y install wget curl
RUN wget https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_linux-x64_bin.tar.gz
RUN tar xvf openjdk-17_linux-x64_bin.tar.gz
RUN mv jdk-17 /opt/
RUN echo "export JAVA_HOME=/opt/jdk-17" >> ~/.bashrc
RUN echo "export PATH=$PATH:$JAVA_HOME/bin " >> ~/.bashrc
RUN source ~/.bashrc
RUN echo $JAVA_HOME
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9090/tcp
ENTRYPOINT ["java","-jar","/app.jar"]