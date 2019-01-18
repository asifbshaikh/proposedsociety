#!/bin/bash

deploy_dir=$base_dir/current

echo "Starting prod."

nohup \
$deploy_dir/start \
$mem_flags \
-Dhttp.address=$host \
-Dconfig.file=$conf \
-DapplyEvolutions.default=true \
-Dhttp.port=$port \
-Depg.usemockgateway=$use_mock_gateway \
-Dlogger.file=$logger > startup.log &

echo "Spawned prod process."

