#!/usr/bin/bash

. /data0/batchadmin/cm/shell/mstbashrc.conf

SAMFILE=$1
ORIGSSAMFILE=`head -n1 ${SAMFILE} | awk '{if (substr($1,1,1)) print substr($1,16)}'`


echo "========================================"
echo "[[ Accuracy Check ]]"
echo "SAMFILE        = "${SAMFILE}
echo "ORIGSSAMFILE   = "$ORIGSSAMFILE}

cd ${FILE_DIR}


FILE_HEADER_WC=` wc -l ${SAMFILE}_H | awk '{print $1}' `
echo "Header_wc       = "${FILE_HEADER_WC}

if [[ ! -e ${SAMFILE}_P ]]
then
      FILE_DATA_WC=0
else
      FILE_DATA_WC=` wc -l ${SAMFILE}_P | awk '{print $1}'`
fi
echo "P_wc            = "${FILE_DATA_WC}

if [[ ! -e ${SAMFILE}_O ]]
then
      FILE_DATA2_WC=0
else
      FILE_DATA2_WC=` wc -l ${SAMFILE}_O | awk '{print $1}'`
fi
echo "O_wc            = "${FILE_DATA2_WC}

if [[ ! -e ${SAMFILE}_R ]]
then
      FILE_DATA3_WC=0
else
      FILE_DATA3_WC=` wc -l ${SAMFILE}_R | awk '{print $1}'`
fi
echo "R_wc            = "${FILE_DATA3_WC}

if [[ ! -e ${SAMFILE}_I ]]
then
      FILE_DATA4_WC=0
else
      FILE_DATA4_WC=` wc -l ${SAMFILE}_I | awk '{print $1}'`
fi
echo "I_wc            = "${FILE_DATA4_WC}

FILE_TAILER_WC=` wc -l ${SAMFILE}_T | awk '{print $1}' `
echo "Tailer_wc       = "${FILE_TAILER_WC}


if [[ ${ORIGSSAMFILE} == "DT9122" ]]
then
      FILE_WC_SUM=` tail -l ${SAMFILE} | awk '{print int(substr($0,28,8))}'`
else
      FILE_WC_SUM=` tail -l ${SAMFILE} | awk '{print int(substr($0,27,7))}'`
fi
echo "File_cnt        = "${FILE_WC_SUM}
echo

FILE_TOTAL_WC=` expr ${FILE_DATA_WC} + ${FILE_DATA2_WC} + ${FILE_DATA3_WC} + ${FILE_DATA4_WC}`
echo "File_total_wc   = "${FILE_TOTAL_WC}

FILE_WC_CHK=` expr ${FILE_TOTAL_WC} - ${FILE_WC_SUM}`
echo "expr ${FILE_TOTAL_WC} - ${FILE_WC_SUM} = "${FILE_WC_CHK}
echo "=================================="
echo "[ Accuracy check ]"

if [[ ${FILE_WC_CHK} -ne 0 ]]
then
      ehco "File Cnt Error"
      echo "=================================="
      echo "[ Accuracy Check ]"
      exit 1
fi
