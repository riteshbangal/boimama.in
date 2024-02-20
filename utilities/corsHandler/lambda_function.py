import json

def load_config():
    # Load the config file
    with open('config.json', 'r') as config_file:
        config = json.load(config_file)
    return config

def lambda_handler(event, context):

    # Extract the 'origin', 'stage' headers from the event
    origin_header = event.get('headers', {}).get('origin', 'Unknown origin')
    stage_header = event.get('requestContext', {}).get('stage', 'Unknown stage')

    # Print the 'origin', 'stage' headers
    print(f"Origin Header: {origin_header}")
    print(f"Stage Header: {stage_header}")
    
    # Load the configuration
    config = load_config()

    # Determine the environment based on your logic (e.g., from event, stage, etc.)
    environment = stage_header  # Determine the environment

    # Get the whitelisted origins for the specified environment
    whitelisted_origins = config['environments'].get(environment, {}).get('whitelistedOrigins', [])

    # Check if the incoming origin is whitelisted
    # if origin_header in whitelisted_origins or '*' in whitelisted_origins:
    if origin_header in whitelisted_origins:
        print(f"Allowed Origin: {origin_header}")
        # Your Lambda function logic here
        response = {
            'statusCode': 200,
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': origin_header,
                'Access-Control-Allow-Methods': 'OPTIONS,GET',
                'Access-Control-Allow-Headers': 'Content-Type,Authorization',
            },
            'body': '{"message": "Hello from CORS Handler!"}'
        }
    else:
        print(f"Blocked Origin: {origin_header}")
        response = {
            'statusCode': 403,
            'body': '{"error": "Forbidden"}'
        }

    return response
