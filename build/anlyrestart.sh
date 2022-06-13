#!/bin/bash
##设置报错就退出
set -e
echo "该脚本的处理内容如下，执行前请务必确认是否需要这些步骤："
echo "本地处理（1、删除原maven打包目录（变量CURRENT_JAR_PATH代表的文件夹），2、运行maven命令重新打包，3、解压tar包来生成新jar包，4、复制新jar包至服务器并重命名后带当前时间）"
echo "连接服务器（5、把旧jar包进程杀死，6、把旧jar包名字后加上时间，7、重命名新jar包并启动）"



##当前电脑上的jar包目录！！！该目录会被先删除后更新，设置前务必确认，脚本不会做备份！！！（/结尾）
CURRENT_PATH="/Users/anlythree/my/springboot-swagger-demo/"
# shellcheck disable=SC1068
##服务器的jar包位置(/结尾)
SERVER_JAR_PATH="/home/jar/"
##jar包名称
JAR_NAME="springboot-swagger-demo-1.0-SNAPSHOT-boot.jar"

##当前时间
CURRENT_TIME=$(date "+%Y-%m-%d%H:%M:%S")
CURRENT_JAR_PATH="${CURRENT_PATH}target/"

##startOf本地处理（1、删除原maven打包目录，2、运行maven命令重新打包，3、解压tar包来生成新jar包，4、复制新jar包至服务器并重命名后带当前时间）
echo "startOf本地处理（1、删除原maven打包目录，2、运行maven命令重新打包，3、解压tar包来生成新jar包，4、复制新jar包至服务器并重命名后带当前时间）"
##maven打包目录校验
if [ ! -e $CURRENT_JAR_PATH ]
  then
    echo "${CURRENT_JAR_PATH}目录不存在"
    exit 1
fi
if [ $CURRENT_PATH = "/" ]
  then
    echo "CURRENT_PATH为根目录！该shell会先删除CURRENT_PATH后再重新创建！所以先修改CURRENT_PATH后再运行该脚本！"
    exit 1
fi
if [ ! -e "${CURRENT_PATH}pom.xml" ]
  then
    echo "${CURRENT_PATH}目录下不是maven项目"
    exit 1
fi
##删除旧jar包位置，命令很危险！！前边有三个关于路径的校验
rm -rf ${CURRENT_JAR_PATH}

##重新打jar包
cd ${CURRENT_PATH}
mvn clean package -Dmaven.test.skip=true
##复制jar包至服务器

/usr/bin/expect <<EOF
  ##scp复制时把文件名修改为带日期格式的
  spawn scp -r -P 233 "${CURRENT_JAR_PATH}${JAR_NAME}" anlyconnect@43.132.237.240:${SERVER_JAR_PATH}${JAR_NAME}${CURRENT_TIME}
  ##输入密码
  expect "*password"
  send "Anly233\r"
  interact
EOF

#
#if [ ${copy_result} != 0 ];
#  echo "scp复制文件失败，原因可能：【1、目录无权限2、目录不存在】"
#  then exit 1
#fi

echo "endOf本地处理（1、删除原maven打包目录，2、运行maven命令重新打包，3、解压tar包来生成新jar包，4、复制新jar包至服务器并重命名后带当前时间）"
##endOf本地处理（1、删除原maven打包目录，2、运行maven命令重新打包，3、解压tar包来生成新jar包，4、复制新jar包至服务器并重命名后带当前时间）

}
##startOf连接服务器（5、把旧jar包进程杀死，6、把旧jar包名字后加上时间，7、重命名新jar包并启动）
#echo "startOf连接服务器（5、把旧jar包进程杀死，5、把旧jar包名字后加上时间，7、重命名新jar包并启动）"
#/usr/bin/expect <<EOF
#
#  ##连接到远程服务器
#  spawn ssh anlyconnect@43.132.237.240 -p 233
#
#  ##捕捉密码并登录
#  expect "*password"
#  send "Anly233\r"
#
#  expect "*Lastlogin:*"
#
#  ##查看已经在运行的jar包，并kill掉
#  #send "result = ps -ef|grep bus-arrive\r"
#
#  #echo "result:${result}"
#
#  #send "OLD_PID = ps -f | grep java | grep ${JAR_NAME} |awk '{print \$2}'"
#  #
#  #send "kill -9 ${OLD_PID}\r"
#
#  ## expect ""
#  #echo "杀掉旧jar包"
#
#  send "exit\r"
#
#  interact
#
#EOF
#
#echo "endOf连接服务器（5、把旧jar包进程杀死，6、把旧jar包名字后加上时间，7、重命名新jar包并启动）"
###endOf连接服务器（5、把旧jar包进程杀死，6、把旧jar包名字后加上时间，7、重命名新jar包并启动）

