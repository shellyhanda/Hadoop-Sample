#################################
# Name: uploadFile.sh
# Location: remote node where we
#           want to run an 
#           operation
#################################
#!/bin/bash

hadoop fs -put ~/sshtest_2018_Aug/* /user/svchdphdo4ecfd

status=$?

if [ $status = 0 ]; then
	echo "STATUS=SUCCESS"
else
	echo "STATUS=FAIL"
fi