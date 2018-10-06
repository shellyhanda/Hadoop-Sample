echo 'I am startting sdsds  ds'
pwd
ls
#echo "rthis stets" > /data/home/svchdphdo4ecfd/ruhul.jar
#echo "this is tests" | hadoop fs -put - ruhul1.tar

echo "$1" > "test_key"
chmod 700 test_key

ssh -q -v -i test_key -o "StrictHostKeyChecking no" zh722e@charon.cs.boeing.com /boeing/sw/ecfd/cg/dev/bin/TransferFileHpcHdp get /ptmp/webhfs_test/solution_300MB.tar | hadoop fs -put - solution_300MB.tar_3Oct2018 

#hadoop fs -cat /user/svchdphdo4ecfd/webhfs_test/solution_100GB.tar | ssh -q -v -i id_rsa_ecfddev -o "StrictHostKeyChecking no" zh722e@charon.cs.boeing.com /boeing/sw/ecfd/cg/dev/bin/TransferFileHpcHdp put /ptmp/webhfs_test/ssh_oozie_reverse/solution_100GB.tar
rm test_key
echo 'i am sleeping'
#sleep 1m
echo 'I am finish'
