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

#### Run the Docker container with local image:
``` bash
docker run -p 8080:8080 \
    -e AWS_ACCESS_KEY_ID=<aws_access_key_id> \
    -e AWS_SECRET_ACCESS_KEY=<aws_secret_access_key> \
    boimama-app:0.0.0-SNAPSHOT
```
#### Retrieve an authentication token and authenticate your Docker client to your registry. Use the AWS CLI:
``` bash
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 742608208319.dkr.ecr.eu-central-1.amazonaws.com
```

#### Push the docker image into Docker.io (docker.io/314201/boimama-app):
``` bash
docker tag boimama-app:0.0.0-SNAPSHOT 314201/boimama-app:0.0.0-SNAPSHOT
docker push 314201/boimama-app:0.0.0-SNAPSHOT
```

#### Push the docker image into ECR:
``` bash
docker tag boimama-app:0.0.0-SNAPSHOT 742608208319.dkr.ecr.eu-central-1.amazonaws.com/boimama-app:0.0.0-SNAPSHOT
docker push 742608208319.dkr.ecr.eu-central-1.amazonaws.com/boimama-app:0.0.0-SNAPSHOT
```

#### Run the Docker container with ECR image:
``` bash
docker run -p 8080:8080 \
    -e AWS_ACCESS_KEY_ID=<aws_access_key_id> \
    -e AWS_SECRET_ACCESS_KEY=<aws_secret_access_key> \
    742608208319.dkr.ecr.eu-central-1.amazonaws.com/boimama-app:0.0.0-SNAPSHOT
```

## Infrastructure
The infrastructure is built using the AWS CDK. The deployed resources contain:
- API Gateway.

### Application

The application is deployed using AWS SAM. You can [install AWS SAM here](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html). To deploy the application run the below commands:
``` bash
sam build
sam deploy --guided
```
