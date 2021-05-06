#!/bin/sh
## java env
JAR_NAME=vxinv-inte-1.0.3.jar

is_exist(){
  pid=$(ps -ef|grep $JAR_NAME |grep -v grep|awk '{print $2}' )
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
    return 1
  else
    return 0
  fi
}

usage() {
    echo "Usage: sh net-ocrot.sh [start|stop|restart|status] -[s|c]"
    exit 1
}

#启动方法ls

start(){

  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is already running PID=${pid} <<<"
  else
#    -Xms512m -Xmx1024m
    nohup java  -jar "$JAR_NAME" "$which" >/dev/null 2>&1 &
    echo ">>> start $JAR_NAME successed PID=$! <<<"
   fi
  }

#停止方法
stop(){
  #is_exist
#  pidf=$(cat $PID)
#  #echo "$pidf"
#  echo ">>> api PID = $pidf begin kill $pidf <<<"
#  kill $pidf
#  rm -rf $PID
#  sleep 2
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> api 2 PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2``
    echo ">>> $JAR_NAME process stopped <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
which="$2"
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0
