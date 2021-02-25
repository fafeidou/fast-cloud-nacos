
* 启动logstash ./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/

*  springBoot+kafka+ELK分布式日志收集 https://www.cnblogs.com/niechen/p/10149962.html

* elk 

```shell script
input {
  kafka {
      bootstrap_servers => "192.168.56.124:9092"
      topics_pattern => ".*"
      group_id => "logstash2_servivce"
      consumer_threads => 10
      auto_offset_reset => "earliest"
      decorate_events => "true"
  }
}


filter {
  json{
    source => "message"
  }
}

output {
  elasticsearch {
    hosts  => "192.168.56.124:9200"
    action => "index"
    index  => "%{[@metadata][kafka][topic]}-%{+YYYY.MM.dd}"
  }
}

```

* logstash 同步日志

```shell script
input {
  kafka {
      bootstrap_servers => "192.168.56.124:9092"
      topics_pattern => ".*"
      group_id => "logstash2_servivce"
      consumer_threads => 10
      auto_offset_reset => "earliest"
      decorate_events => "true"
  }
}


filter {
  mutate {
    remove_field => ["@version", "@timestamp"]
  }

}

output {
    file {
	  path => "/data/log/kafka/%{[@metadata][kafka][topic]}/%{[@metadata][kafka][topic]}.log"
      codec => line { format => "%{message}"}
	}
}
```


* ElasticSearch——Logstash输出到Elasticsearch配置 https://www.cnblogs.com/caoweixiong/p/11791396.html

* kafka leader not found KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.56.124:9092