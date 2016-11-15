export JAVA_HOME=/usr/lib/jvm/@@JAVA_VER@@
export JAVA_PATH=$JAVA_HOME/bin

export JDK_HOME=$JAVA_HOME
export JRE_HOME=$JAVA_HOME/jre
export JRE_PATH=$JRE_HOME/bin

export CLASSPATH=.:$JAVA_HOME/lib

export PATH=$JAVA_PATH:$JRE_PATH:$PATH
