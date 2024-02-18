from cassandra.cluster import Cluster
from cassandra import ConsistencyLevel
from ssl import SSLContext, PROTOCOL_TLSv1_2 , CERT_REQUIRED
from cassandra.auth import PlainTextAuthProvider
from os import environment

# Replace these values with your AWS Keyspaces connection details
contact_points = ['cassandra.eu-central-1.amazonaws.com']
username = environment.get('aws_cassandra_access_key_id')
password = environment.get('aws_cassandra_secret_access_key')
keyspace = 'boimama'

# Setup SSL Configuration: 
# Amazon Keyspaces requires the use of Transport Layer Security (TLS) to help secure connections with clients. 
# To connect to Amazon Keyspaces using TLS, 
# you need to download an Amazon digital certificate and configure the Python driver to use TLS.
ssl_context = SSLContext(PROTOCOL_TLSv1_2 )
ssl_context.load_verify_locations('./sf-class2-root.crt')
ssl_context.verify_mode = CERT_REQUIRED

# Set up authentication
auth_provider = PlainTextAuthProvider(username=username, password=password)

# Connect to the cluster
cluster = Cluster(contact_points=contact_points, ssl_context=ssl_context, auth_provider=auth_provider, port=9142)

# Create a session with LOCAL_QUORUM consistency level
session = cluster.connect(keyspace)
# session.default_consistency_level = 3  # Use 3 for LOCAL_QUORUM
session.default_consistency_level = ConsistencyLevel.LOCAL_QUORUM


# Select data
# query = "SELECT version, story_id, story_image_path FROM story;"
query = "SELECT story_id FROM story;"
result = session.execute(query)

# Update data
# update_query = "UPDATE story SET story_image_path = '/story/3bdfc1a2-98ad-4878-a03a-909608760c3a/image' WHERE story_id = 3bdfc1a2-98ad-4878-a03a-909608760c3a AND version = 1;"
# session.execute(update_query)

# Process the query result
for row in result:
    # print(row.story_id)
    update_query = "UPDATE story SET story_image_path = '/story/" + str(row.story_id) + "/image' WHERE story_id = " + str(row.story_id) + " AND version = 1;"
    session.execute(update_query)


# Delete data
# delete_query = "DELETE FROM example_table WHERE name = 'John Doe';"
# session.execute(delete_query)

# Select data
query = "SELECT version, story_id, story_image_path FROM story;"
result = session.execute(query)

# Process the query result
for row in result:
    print(row)

# Close the session and cluster when done
session.shutdown()
cluster.shutdown()
