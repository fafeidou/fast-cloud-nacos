#!/bin/bash

kubectl create -f namespace.yaml
kubectl create -f node-exporter.yaml
kubectl create -f prometheus.yaml
kubectl create -f grafana.yaml

echo "create prometheus and grafana successful"