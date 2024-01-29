import json

# This Lambda function in Python that checks the Access-Control-Allow-Origin header and logs the origin.
def lambda_handler(event, context):
    # Lambda function triggered by API Gateway

    headers = event.get('headers', {})

    # Check if Access-Control-Allow-Origin header is present
    if 'access-control-allow-origin' in headers:
        # Log the origin
        allowed_origin = headers['access-control-allow-origin']
        print(f"Request allowed from origin: {allowed_origin}")

        # You can perform additional logic based on the allowed origin if needed
        # For example, you might want to restrict access based on specific origins
        if allowed_origin == 'https://example.com':
            # Perform specific actions for this origin
            pass
        else:
            # Access-Control-Allow-Origin header is not in accepted list!
            print('Access-Control-Allow-Origin header is not in accepted list')

    else:
        # Access-Control-Allow-Origin header is not present
        print('Access-Control-Allow-Origin header is missing')

        # You can handle this case based on your requirements
        # For example, you might want to reject the request or add the header dynamically

    # Your actual Lambda logic here

    response = {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!'),
        'headers': {
            'Content-Type': 'application/json',
            # Add other headers as needed
        },
    }

    return response
