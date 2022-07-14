#!/usr/bin/expect

spawn ssh anlyconnect@43.132.237.240 -p 233

set timeout 20

##捕捉密码并登录
expect "*assword"
send "Anly233\n"

expect "Last login:*"
send "echo #!/bin/bash ##1、找到并杀掉旧jar包的进程 > /home/shtest/test.sh\n"

send "exit\n"
