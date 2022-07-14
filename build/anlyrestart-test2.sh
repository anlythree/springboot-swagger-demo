#!/usr/bin/expect

##当前电脑上的jar包目录！！！该目录会被先删除后更新，设置前务必确认，脚本不会做备份！！！（/结尾）
set CURRENT_PATH "/Users/anlythree/my/springboot-swagger-demo/"
set SERVER_USER_IP "anlyconnect@43.132.237.240"
set SERVER_PORT "233"
set SERVER_PASSWD "Anly233"
##服务器的jar包位置(/结尾)
set SERVER_JAR_PATH "/home/jar/"
##jar包名称
set JAR_NAME "springboot-swagger-demo-1.0-SNAPSHOT-boot.jar"

##当前时间
set CURRENT_JAR_PATH "${CURRENT_PATH}target/"


spawn ssh ${SERVER_USER_IP} -p ${SERVER_PORT}
##捕捉密码并登录
expect "*password"
send "${SERVER_PASSWD}\r"
expect "*Lastlogin:*"
##查看已经在运行的jar包，并kill掉
set field1 send "ps -f | grep java | grep ${JAR_NAME}\r"

send "${field1}\r"

send "ps -ef | grep java\r"

send "exit\r"


