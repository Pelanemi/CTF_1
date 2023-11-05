# Verwenden Sie ein offizielles Java-Basisimage
FROM openjdk:11

# Setzen Sie das Arbeitsverzeichnis in Ihrem Container
WORKDIR /Users/emilpelanovic

# Kopieren Sie die JAR-Datei Ihrer Java-Anwendung in den Container
COPY /app/app.jar /app/app.jar

# Exponieren Sie den Port, auf dem Ihre Java-Anwendung läuft
# EXPOSE 8080

# Starten Sie Ihre Java-Anwendung beim Start des Containers
CMD ["java", "-jar", "app.jar"]
