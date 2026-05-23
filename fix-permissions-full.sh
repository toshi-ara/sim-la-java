#!/bin/bash

TEMPDIR=temp_dist
APPDIR=SimLocalAnesthetics


mkdir -p ${TEMPDIR}/${APPDIR}
tar -xjf build/dist/SimLA-Linux-WithJRE.tar.bz2 -C ${TEMPDIR}/${APPDIR}

chmod 755 ${TEMPDIR}/${APPDIR}/run.sh
chmod 755 ${TEMPDIR}/${APPDIR}/runtime/bin/*
tar -cjf build/dist/SimLA-Linux-WithJRE.tar.bz2 -C ${TEMPDIR} ${APPDIR}

rm -rf ${TEMPDIR}

