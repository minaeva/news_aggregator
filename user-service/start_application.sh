#cd /home/ec2-user/services/user-service
#java -jar user-service-0.0.1-SNAPSHOT.jar >> /tmp/logfile.log 2>> /tmp/errorlog.log

#!/bin/bash
echo "Starting application..."
cd /home/ec2-user/services/user-service
pwd
ls -la
docker-compose up --build > /tmp/logfile.log 2> /tmp/errorlog.log
echo "Application started."
