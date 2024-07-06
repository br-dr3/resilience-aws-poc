import json
import uuid
import sys

batch = [{"Id": f"{uuid.uuid4()}", "MessageBody": f"Test-{i+1}"} for i in range(0, int(sys.argv[1]))]

print(json.dumps(batch))