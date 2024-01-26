# Getting Started
Connect to Amazon Keyspaces using the Python driver for Apache Cassandra and service-specific credentials

## Before you begin
You need to complete the following task before you can start.

Amazon Keyspaces requires the use of Transport Layer Security (TLS) to help secure connections with clients. To connect to Amazon Keyspaces using TLS, you need to download an Amazon digital certificate and configure the Python driver to use TLS.

Download the Starfield digital certificate using the following command and save sf-class2-root.crt locally or in your home directory.

``` bash
curl https://certs.secureserver.net/repository/sf-class2-root.crt -O
```

#### Make sure you have the cassandra-driver library installed:
``` bash
pip install cassandra-driver
```

#### Run the python file:
Replace your-cluster-name, your-username, your-password, and your-keyspace with your actual Amazon Keyspaces cluster details.
``` bash
python data_processing_utils.py
```