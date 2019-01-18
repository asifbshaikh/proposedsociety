#!/bin/bash
base_dir=/home/ps/test
mem_flags="-Xms512m -Xms700m"
conf_dir=$base_dir/conf
conf=$conf_dir/test.conf
logger=$conf_dir/logger-test.xml
host="0.0.0.0"
port="9090"
use_mock_gateway="Y"

