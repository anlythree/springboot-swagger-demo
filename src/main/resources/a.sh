function alog(){
   if [ "-p" = $1 ]; then
       echo -e "\033[36m 👉 show leadeon-cmcc-dbprovider's log \033[0m"
       # shellcheck disable=SC2164
       cd /opt/cmcc-tomcat/dbprovider-tomcat
       tail -1000f /opt/cmcc-tomcat/dbprovider-tomcat/logs/catalina.out
       echo -e "\033[36m  show leadeon-cmcc-dbprovider's log👈 \033[0m"
  elif [ "-b" = $1 ]; then
      echo -e "\033[36m 👉 show leadeon-cmcc-biz's log \033[0m"
      # shellcheck disable=SC2164
      cd /opt/cmcc-tomcat/biz-tomcat-db
      tail -1000f /opt/cmcc-tomcat/biz-tomcat-db/logs/catalina.out
      echo -e "\033[36m  show leadeon-cmcc-biz's log👈 \033[0m"
  fi
}

function arestart(){
   if [ "-p" = $1 ]; then
       echo -e "\033[36m 👉 restart leadeon-cmcc-dbprovider and show leadeon-cmcc-dbprovider's log \033[0m"
       tail -1000f /opt/cmcc-tomcat/dbprovider-tomcat/logs/catalina.out
       echo -e "\033[36m  restart leadeon-cmcc-dbprovider and show leadeon-cmcc-dbprovider's log 's log👈 \033[0m"
  elif [ "-b" = $1 ]; then
      echo -e "\033[36m 👉 restart leadeon-cmcc-dbprovider and show leadeon-cmcc-dbprovider's log 's log \033[0m"
      tail -1000f /opt/cmcc-tomcat/biz-tomcat-db/logs/catalina.out
      echo -e "\033[36m  show leadeon-cmcc-dbprovider 's log👈 \033[0m"
  fi
}
