#!/usr/bin/bash

. /data0/batchadmin/cm/shell/mstbashrc.conf

SAMFILENAME=$1
JOBDT=$2
ROUND=$3

FILENAME=${SAMFILENAME}_${JOBDT}_${ROUND}

echo "========================================"
echo "[[ Tailer Check ]]"
echo
echo "SAMFILENAME = "${SAMFILENAME}
echo "JOBDATE     = "${JOBDT}
echo "FILENAME    = "${FILENAME}
echo "ROUND       = "${ROUND}
echo "RECORD_DIR  = "${RECORD_DIR}
echo "FILE_DIR    = "${FILE_DIR}

cd ${FILE_DIR}

FILECNT=` find . -name ${FILENAME} -print | wc -l`
if [[ ${FILENAME} -eq 0 ]]
then
      echo ${FILENAME} FILE Not Found
      echo "=================================="
      exit 1
else
      echo "File File"

      WORKSAM=${FILENAME::-4}
      echo ${WORKSAM}

      CR_CK=$?
      if [[ ${CR_CK} -eq 0 ]]
      then
            echo "Create WORKSAM"
      else
            echo "Fail Create WORKSAM"
            exit 1
      fi
fi

FILE_ENDCK=`tail -l ${FILENAME} | awk '{print substr($0,7,2}'`

echo FILE_ENDCHK = ${FILE_ENDCK}

if [[ ${FILE_ENDCK} -ne 33 ]]
then
      echo END Error
      echo "=================================="
      exit 1
else
      echo "Normal"
      echo
fi

echo SAMFILENAME = ${SAMFILENAME}
if [[ ${SAMFILENAME} == DT9122 ]]
then
      FILE_CNT=`tail -l ${FILENAME} | awk '{print int(substr($0,28,8))}'`
else
      FILE_CNT=`tail -l ${FILENAME} | awk '{print int(substr($0,27,7))}}'`
fi
echo "File_cnt      = " ${FILE_CNT}

FILE_WC=`wc -l ${FILENAME} | awk '{print $1}'`
echo "File_wc       = " ${FILE_WC}

FILE_HDTR=2
echo "file_hdtr     = " ${FILE_HDTR}

FILE_CNTCHK=`EXPR ${FILE_WC} - ${FILE_HDTR} `
echo "file_cntchk   = " ${FILE_CNTCHK}

FILECHK=`expr ${FILE_CNT} - ${FILE_CNTCHK}`
echo "expr ${FILE_CNT} - ${FILE_CNTCHK} = "${FILECHK}

if [[ ${FILECHK} -ne 0 ]]
then
      echo "File Count Error"
      echo "=================================="
      exit 1
fi


/usr/bin/sh ${RECORD_DIR}/${SAMFILENAME}_split.sh ${FILENAME}


R_BK=$?
if [[ ${R_BK} -eq 0 ]]
then
      echo "SUCCESS BY FIR"
      echo "=================================="
else
      ehco "Split Call Failed"
      echo "=================================="
      exit 1
fi