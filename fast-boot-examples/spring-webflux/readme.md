* docker搭建mongo分片集群（docker-compose）https://www.jianshu.com/p/8ae0cbaa309a

docker network create mongo


* Docker容器CPU、memory资源限制 https://www.cnblogs.com/zhuochong/p/9728383.html


* DOCKER-COMPOSE搭建MONGODB分片集群(单机版)  https://www.cnblogs.com/xiaofengxzzf/p/12100730.html


rs.initiate({
    _id: "rs_config_server",
    configsvr: true,
    members: [
        { _id : 0, host : "172.20.0.13:27019" },
        { _id : 1, host : "172.20.0.14:27019" }
    ]
});

#!/bin/sh

docker-compose up -d

sleep 30s

docker-compose  exec config1 bash -c "echo 'rs.initiate({_id: \"fates-mongo-config\",configsvr: true, members: [{ _id : 0, host : \"config1:27019\" },{ _id : 1, host : \"config2:27019\" }, { _id : 2, host : \"config3:27019\" }]})' | mongo --port 27019"
docker-compose  exec shard1 bash -c "echo 'rs.initiate({_id: \"shard1\",members: [{ _id : 0, host : \"shard1:27018\" }]})' | mongo --port 27018"
docker-compose  exec shard2 bash -c "echo 'rs.initiate({_id: \"shard2\",members: [{ _id : 0, host : \"shard2:27018\" }]})' | mongo --port 27018"
docker-compose  exec shard3 bash -c "echo 'rs.initiate({_id: \"shard3\",members: [{ _id : 0, host : \"shard3:27018\" }]})' | mongo --port 27018"
docker-compose  exec mongos bash -c "echo 'sh.addShard(\"shard1/shard1:27018\")' | mongo"
docker-compose  exec mongos bash -c "echo 'sh.addShard(\"shard2/shard2:27018\")' | mongo"
docker-compose  exec mongos bash -c "echo 'sh.addShard(\"shard3/shard3:27018\")' | mongo"