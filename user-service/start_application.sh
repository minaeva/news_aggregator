cd /home/ec2-user/services/user-service
source .env
#java -jar user-service-0.0.1-SNAPSHOT.jar
#
java -jar user-service-0.0.1-SNAPSHOT.jar > /tmp/logfile.log 2> /tmp/errorlog.log
