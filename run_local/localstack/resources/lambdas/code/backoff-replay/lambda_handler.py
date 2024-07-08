import boto3
import os

sqs = boto3.client("sqs")
queue_destination = os.environ.get("QUEUE_DESTINATION")

def handler(event, context):

    print(f"Handling {len(event)} messages.")

    messages = event["Records"]
    for message in messages:
        handler_message(message)

def handler_message(message):
    sqs.send_message(
        QueueUrl=queue_destination,
        MessageBody=message["body"],
        MessageAttributes=message["messageAttributes"]
    )

    print("Message redirected with success")