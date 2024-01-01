# Getting Started

#### Maven build:
``` bash
mvn clean install
```

#### Maven build package:
``` bash
mvn package -DskipTests
```

#### After creating the Dockerfile, you can build the Docker image using the following command:
``` bash
docker build -t boimama-app:0.0.0-SNAPSHOT .
```

#### Run the Docker container:
``` bash
docker run -p 8080:8080 \
    -e AWS_ACCESS_KEY_ID=<aws_access_key_id> \
    -e AWS_SECRET_ACCESS_KEY=<aws_secret_access_key> \
    boimama-app:0.0.0-SNAPSHOT
```