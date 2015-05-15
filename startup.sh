#!/bin/bash
JARFile="target/panda-ws-1.0-SNAPSHOT.jar"
PIDFile="/comicpanda/logs/app/ws/application.pid"
LogFile="/comicpanda/logs/app/ws/application.log"

function check_if_pid_file_exists {
    if [ ! -f $PIDFile ]
    then
 echo "PID file not found: $PIDFile"
        exit 1
    fi
}

function check_if_process_is_running {
 if ps -p $(print_process) > /dev/null
 then
     return 0
 else
     return 1
 fi
}

function print_process {
    echo $(<"$PIDFile")
}

case "$1" in
  status)
    check_if_pid_file_exists
    if check_if_process_is_running
    then
      echo $(print_process)" is running"
    else
      echo "Process not running: $(print_process)"
    fi
    ;;
  stop)
    check_if_pid_file_exists
    if ! check_if_process_is_running
    then
      echo "Process $(print_process) already stopped"
      exit 0
    fi
    kill -TERM $(print_process)
    echo -ne "Waiting for process to stop"
    NOT_KILLED=1
    for i in {1..20}; do
      if check_if_process_is_running
      then
        echo -ne "."
        sleep 1
      else
        NOT_KILLED=0
      fi
    done
    echo
    if [ $NOT_KILLED = 1 ]
    then
      echo "Cannot kill process $(print_process)"
      exit 1
    fi
    echo "Process stopped"
    ;;
  start)
    if [ -f $PIDFile ] && check_if_process_is_running
    then
      echo "Process $(print_process) already running"
      exit 1
    fi
    nohup java -jar -Dspring.profiles.active=$2 -DredisPassword=$3 $JARFile >/dev/null 2>&1 &
    echo "$!" > $PIDFile
    echo "Process started"

    tail -n0 -F $LogFile | while read line; do
        echo $line
    if echo $line | grep -q 'Started Application'; then
        pkill -f "tail -n0 -F $LogFile"
        exit 0
    fi
    done
    ;;
  *)
    echo "Usage: $0 {start|stop|status}"
    exit 1
esac

exit 0
