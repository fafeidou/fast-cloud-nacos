#!/bin/bash

kubectl create -f ../grafana/node-exporter.yaml
kubectl create -f ../prometheus/rbac-setup.yaml
kubectl create -f ../prometheus/configmap.yaml
kubectl create -f ../prometheus/prometheus.yaml
kubectl create -f ../prometheus/grafana.yaml

echo "create prometheus and grafana successful"