mkdir build
call javac *.java -d build

cd build

start cmd /k java Server 8010 224.1.1.1 8011

java Client 224.1.1.1 8011 LOOKUP google.com
java Client 224.1.1.1 8011 REGISTER google.com 8.8.8.8
java Client 224.1.1.1 8011 REGISTER google.com 8.8.8.1
java Client 224.1.1.1 8011 LOOKUP google.com
java Client 224.1.1.1 8011 REGISTER example.com 1.1.1.1
java Client 224.1.1.1 8011 LOOKUP example.com
java Client 224.1.1.1 8011 LOOKUP example.com
java Client 224.1.1.1 8011 REGISTER example.com 1.2.3.4
java Client 224.1.1.1 8011 LOOKUP example.com
