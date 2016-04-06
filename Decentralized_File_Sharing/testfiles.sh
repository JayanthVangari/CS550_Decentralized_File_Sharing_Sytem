#!/bin/bash
x="0";
filenum="$1";
node_id="$3";
DIR=`pwd`
while [ "$x" -lt "$filenum" ]
do
let "x=x+1"
fallocate -l $2 $DIR/peer$3/test$3_$2_$x.BIN;
#dd if=/dev/zero bs="$2" count=1  of=$DIR/peer$3/test$3_$2_$x.BIN;
done


