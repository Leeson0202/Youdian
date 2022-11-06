http://47.108.182.213:8888/93c61b30/

### 0. 问题

###### 4.1 解决80端口被占用 ：

netstat -lnp|grep 80

 kill -9  pid



###### 4.2 后台启动jar

cd /home/leeson/music 

 nohup java -jar music.jar >log.log & 

