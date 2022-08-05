#!/usr/bin/bash

. /data0/batchadmin/cm/shell/mstbashrc.conf

SAMFILENAME=$1
SAMFILE=${FILE_DIR}"/"${SAMFILENAME}
ORIGSAMFILE=`head -n1 ${SAMFILE} | awk '{if (substr($1,1,1)) print substr($1,1,6)}'`
TEMPIDENTFILE=${SAMFILE}"_id"


echo "========================================"
echo "[[ File Split ]]"
echo
echo "SAMFILE       = "${SAMFILE}
echo "ORIGSAMFILE   = "${ORIGSAMFILE}
echo "TEMPIDENTFILE = "${TEMPIDENTFILE}
echo "FILE_DIR      = "${FILE_DIR}

cd ${FILE_DIR}


FILE_CHK=`head -n1 ${SAMFILE} | awk '{print substr($0,1,6)}'`

if [[ ${FILE_CHK} != ${ORIGSAMFILE} ]]
then
      echo "File Search Error"
      echo "=================================="
      exit 1
else
      echo "File File"
      echo
fi


if [[ -e ${SAMFILE}_"H" ]]
then
      rm -f ${SAMFILE}_H
      echo "Deleted "${SAMFILE}"_H"
fi

if [[ -e ${SAMFILE}_"T" ]]
then
      rm -f ${SAMFILE}_T
      echo "Deleted "${SAMFILE}"_T"
fi

if [[ -e ${SAMFILE}_"P" ]]
then
      rm -f ${SAMFILE}_P
      echo "Deleted "${SAMFILE}"_P"
fi

if [[ -e ${SAMFILE}_"O" ]]
then
      rm -f ${SAMFILE}_O
      echo "Deleted "${SAMFILE}"_O"
fi

if [[ -e ${SAMFILE}_"R" ]]
then
      rm -f ${SAMFILE}_R
      echo "Deleted "${SAMFILE}"_R"
fi

if [[ -e ${SAMFILE}_"I" ]]
then
      rm -f ${SAMFILE}_I
      echo "Deleted "${SAMFILE}"_I"
fi

if [[ -e ${SAMFILE}_"identinfo" ]]
then
      rm -f ${SAMFILE}_identinfo
      echo "Deleted "${SAMFILE}"_identinfo"
fi
echo


awk '{if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="11" ) print $0 > "'${SAMFILE}'_H"
  else if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="33" ) print $0 > "'${SAMFILE}'_T"
  else if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="22" && (substr($1, 16,3)=="0") print $0 > "'${SAMFILE}'_P"
  else if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="22" && (substr($1, 16,3)=="101" && substr($1,16,3)<="500")) print $0 > "'${SAMFILE}'_O"
  else if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="22" && (substr($1, 16,3)=="601" && substr($1,16,3)<="700")) print $0 > "'${SAMFILE}'_R"
  else if (substr($1,1,6)=="'${ORIGSAMFILE}'" && substr($1,7,2)=="22" && (substr($1, 16,3)=="701" && substr($1,16,3)<="800")) print $0 > "'${SAMFILE}'_I"
    }' ${SAMFILE}

RESULT_C=$?
if [[ ${RESULT_C} -eq 1 ]]
then
      echo "Split Error"
      exit 1
else
      echo "File Split Result = "${RESULT_C}
      echo
fi


if [[ ! -e ${SAMFILE}"_P" ]]
then
      touch ${SAMFILE}"_P"
      echo "create "${SAMFILE}"_P seperately"
fi
if [[ ! -e ${SAMFILE}"_O" ]]
then
      touch ${SAMFILE}"_O"
      echo "create "${SAMFILE}"_P seperately"
fi
if [[ ! -e ${SAMFILE}"_R" ]]
then
      touch ${SAMFILE}"_R"
      echo "create "${SAMFILE}"_P seperately"
fi
if [[ ! -e ${SAMFILE}"_I" ]]
then
      touch ${SAMFILE}"_I"
      echo "create "${SAMFILE}"_P seperately"
fi
echo
########################################################################

awk '{ if(substr($1,19,1)) print substr($1,19,13) > "' ${TEMPIDENTFILE}'"}' ${SAMFILE}_P
awk '{ if(substr($1,108,1)) print substr($1,108,13) >> "' ${TEMPIDENTFILE}'"}' ${SAMFILE}_P

RESULT_P=$?

if [[ ${RESULT_P} -eq 1 ]]
then
      echo "Failed Add P to File"
else
      echo "Create Add P to File"
fi


if [[ ! -e ${TEMPIDENTFILE} ]]
then
      touch ${TEMPIDENTFILE}
      echo
      echo "Create "${TEMPIDENTFILE}" seperately"
fi


/usr/bin/sh ${RECORD_DIR}/com_idfc_split.sh ${FILENAME}


R_BK=$?
if [[ ${R_BK} -eq 0 ]]
then
      echo "SUCCESS BY SEC"
      echo "=================================="
else
      ehco "Accuracy Check Call Failed"
      echo "=================================="
      exit 1
fi


