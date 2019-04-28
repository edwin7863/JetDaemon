########################################################################
# SYSTEM  ID : BwsStockReport                                          #
# PROGRAM ID :                                                         #
# AUTHOR     : Endra Lee                                               #
# CREATE DATE: 2008/01/22                                              #
########################################################################
# MODIFICATION LOG:                                                    #
########################################################################

msg()
{
  echo "$1"
  date +"DATE: %D %H:%M:%S"   
  echo " "                  
}
JAVA_HOME=/opt/WebSphere/AppServer/java;export JAVA_HOME;
PATH=$JAVA_HOME/bin:$PATH;export PATH;
CLASSPATH=.:/user1/dmmgr/BwsDaemon/lib/ifxjdbc.jar:/user1/dmmgr/BwsDaemon/lib/ifxjdbcx.jar:/user1/dmmgr/BwsDaemon/lib/ifxtools.jar:/user1/dmmgr/BwsDaemon/lib/jdom.jar:/user1/dmmgr/BwsDaemon/lib/xerces.jar:/user1/dmmgr/BwsDaemon/lib/j2ee.jar:/user1/dmmgr/BwsDaemon/lib/classes12.jar;
export CLASSPATH;
CLIENT_LOCALE=zh_tw.big5;export CLIENT_LOCALE;
LANG=zh_TW.BIG5 ;     export LANG;
LC_ALL=zh_TW.BIG5 ;   export LC_ALL;
#####################################################
tday=`date +%m%d%y`
FM_YEAR=`echo $tday | cut -c5-6`   # 年度
FM_MONTH=`echo $tday | cut -c1-2`  # 月份
FM_DAY=`echo $tday | cut -c3-4`    # 日份
OUTDIR=/user1/dmmgr/BwsDaemon/output; export OUTDIR
#####################################################

DD=`date +%d`
errfile=${OUTDIR}/BwsStockReport.err
txtfile=${OUTDIR}/BwsStockReport.txt
monfile=${OUTDIR}/BwsMon.err
whour="`date +%H`">> $errfile

msg "BwsDaemon.sh begin">> $errfile

cd /user1/dmmgr/BwsDaemon
> $txtfile
java app.sertek.batch.BwsStockReport >> $txtfile
chmod 777 $txtfile
cp $txtfile /opt/WebSphere/AppServer/installedApps/kap1Network/BwsWebAppORACLE_war.ear/BwsWebAppORACLE.war/ 
msg "end">> $errfile

java app.sertek.batch.BwsMonthProc >> $monfile
