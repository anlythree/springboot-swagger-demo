#!/bin/bash
##设置报错就退出
set -e

##当前电脑上的jar包目录！！！该目录会被先删除后更新，设置前务必确认，脚本不会做备份！！！（/结尾）
CURRENT_PATH="/Users/anlythree/my/springboot-swagger-demo/"
SERVER_USER_IP="anlyconnect@43.132.237.240"
SERVER_PORT="233"
SERVER_PASSWD="Anly233"
##服务器的jar包位置(/结尾)
SERVER_JAR_PATH="/home/jar/"
##jar包名称
JAR_NAME="springboot-swagger-demo-1.0-SNAPSHOT-boot.jar"

##当前时间
CURRENT_TIME=$(date "+%Y-%m-%d%H:%M:%S")
CURRENT_JAR_PATH="${CURRENT_PATH}target/"


/usr/bin/expect << EOF
##连接到远程服务器
  spawn ssh ${SERVER_USER_IP} -p ${SERVER_PORT}

  ##捕捉密码并登录
  expect "*password"
  send "${SERVER_PASSWD}\r"

  expect "*Lastlogin:*"
  ##查看已经在运行的jar包，并kill掉

  # shellcheck disable=SC2154
  set field1 send "OLD_PID = ps -f | grep java | grep ${JAR_NAME} "

  send "exit\r"
  expect eof
EOF

echo "field1:${field1}"
