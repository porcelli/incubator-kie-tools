#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

# runs the e2e locally
# You must have minikube installed
MINIKUBE_PROFILE=${1:-minikube}
echo "Using minikube profile ${MINIKUBE_PROFILE}"
export OPERATOR_IMAGE_NAME=localhost/sonataflow-operator:0.0.1

# clean up previous runs
kubectl get namespaces -o name | awk -F/ '/^namespace\/test/ {print $2}' | xargs kubectl delete namespace
make undeploy ignore-not-found=true
make deploy IMG="${OPERATOR_IMAGE_NAME}"

eval "$(minikube -p "${MINIKUBE_PROFILE}" docker-env)"
if ! make container-build BUILDER=docker IMG="${OPERATOR_IMAGE_NAME}"; then
  echo "Failure: Failed to build image, exiting " >&2
  exit 1
fi

make deploy IMG="${OPERATOR_IMAGE_NAME}"
make test-e2e
