#!/usr/bin/bash

. /data0/batchadmin/cm/shell/mstbashrc.conf

SAMFILE=$1
TEMPIDFILE=$1"_id"
IDENTFILE=$1"_identinfo"

FILENAME=${SAMFILENAME}_${JOBDT}_${ROUND}

echo "========================================"
echo "[[ Duplicate Check ]]"
echo
echo "SAMFILE     = "${SAMFILE}
echo "TEMPIDFILE  = "${TEMPIDFILE}
echo "IDENTFILE   = "${IDENTFILE}


cd ${FILE_DIR}

if [[ -e ${TEMPIDFILE} ]]
then
      echo "Continue Duplication"

      sed -i '/000000000000/d' ${TEMPIDFILE}
      awk '!x[$0]++{print > "'${IDENTFILE}'"}' ${TEMPIDFILE}
else
      echo "Not exists("${TEMPIDFILE}")"
      echo "Failed Duplication"
fi

DUP_CK=$?
if [[ ${DUP_CK} -eq 1 ]]
then
      echo "Identification Duplicate Check Error"
      echo "=================================="
else
      rm -f ${TEMPIDFILE}
      RM_CK=$?
      if [[ ${RM_CK} -eq 1 ]]
      then
            echo "Fail Delete Duplicate File"
            echo "=================================="
      else
            if [[ ! -e ${IDENTFILE} ]]
            then
                  touch ${IDENTFILE}
                  echo "Create "${IDENTFILE}" seperately"
            fi
            echo "Identification Duplicate Check Success"
      fi
fi


/usr/bin/sh ${RECORD_DIR}/com_mtxt_talr_verify.sh ${SAMFILE}


R_BK=$?
if [[ ${R_BK} -eq 1 ]]
then
      ehco "Accuracy Check Call Failed"
      exit 1
fi
echo "SUCCESS BY THI"
