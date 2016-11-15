echo "<<< OpenSplice HDE Release V6.4.140407OSS For x86_64.linux, Date 2014-04-15 >>>"

SPLICE_ORB=DDS_OpenFusion_2
export SPLICE_ORB

SPLICE_JDK=jdk
export SPLICE_JDK

OSPL_HOME="@@INSTALLDIR@@/opensplice-linux"
PATH=$OSPL_HOME/bin:$PATH
LD_LIBRARY_PATH=$OSPL_HOME/lib${LD_LIBRARY_PATH:+:}$LD_LIBRARY_PATH
CPATH=$OSPL_HOME/include:$OSPL_HOME/include/sys:${CPATH:=}
OSPL_URI=file://$OSPL_HOME/etc/config/ospl_sp_ddsi.xml
OSPL_TMPL_PATH=$OSPL_HOME/etc/idlpp
. $OSPL_HOME/etc/java/defs.jdk
export OSPL_HOME PATH LD_LIBRARY_PATH CPATH OSPL_TMPL_PATH OSPL_URI
