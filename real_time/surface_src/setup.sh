
#!/bin/bash
#This downloads and installs all of the software projects that robobuggy needs 

#opencv
  git clone https://github.com/Itseez/opencv.git
  cd opencv 
  git checkout 3.0.0
  mkdir build
  cd build
  cmake -DCMAKE_BUILD_TYPE="Release" ..
  make -j4
  sudo make -j4 install
  mvn install:install-file -Dfile=/usr/local/share/OpenCV/java/opencv-300.jar -DgroupId=opencv -DartifactId=opencv -Dversion=3.0.0 -Dpackaging=jar
  cd ../..