mkdir build
call javac *.java -d build

cd build

start cmd /k java Server 8010

java Client localhost 8010 REGISTER google.com 8.8.8.8
java Client localhost 8010 LOOKUP google.com
java Client localhost 8010 REGISTER fe.up.pt 1.1.1.1
java Client localhost 8010 REGISTER fe.up.pt 1.1.1.1
java Client localhost 8010 LOOKUP fe.up.pt
