cd /home/ec2-user/services/user-service
#java -jar user-service-0.0.1-SNAPSHOT.jar
#
java -jar user-service-0.0.1-SNAPSHOT.jar > /var/log/user-service/logfile.log 2> /var/log/user-service/errorlog.log
