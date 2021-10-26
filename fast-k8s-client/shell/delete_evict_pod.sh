#!/bin/bash
for each in $(kubectl get pods -n default|grep Evicted|awk '{print $1}');
do
  kubectl delete pods $each -n default
done

## 删除指定namespace下所有evict的pod