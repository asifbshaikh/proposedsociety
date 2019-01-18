#!/bin/bash

deploy_dir=$base_dir/current

if [ -f $deploy_dir/RUNNING_PID ]; then
	pid=`cat $deploy_dir/RUNNING_PID`
	if [ -d /proc/$pid ]; then
		echo "About to kill process"
		kill -9 `cat $deploy_dir/RUNNING_PID`
	else
		echo "Process does not exist. Wont kill."
	fi

	rm $deploy_dir/RUNNING_PID	
else 
	echo "No running process found. Did not kill any process."
fi

 
