#!/bin/bash
base_dir=/home/ps/prod
mem_flags="-Xms1g -Xms1g"
conf_dir=$base_dir/conf
conf=$conf_dir/prod.conf
logger=$conf_dir/logger-prod.xml
host="127.0.0.1"
port="9000"
use_mock_gateway="N"

