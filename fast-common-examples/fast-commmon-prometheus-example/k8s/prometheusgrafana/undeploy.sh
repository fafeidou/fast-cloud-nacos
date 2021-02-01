#!/bin/bash

kubectl delete -f namespace.yaml
kubectl delete -f node-exporter.yaml
kubectl delete -f prometheus.yaml
kubectl delete -f grafana.yaml

echo "undeploy successful"