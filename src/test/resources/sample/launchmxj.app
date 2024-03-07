#!/bin/bash

# Check for correct arguments
if [ "$1" == "-s" ]; then
    # Echo the sample output
    echo " "
    echo "Found running service(s) :"
    echo " vm000003654.aagent.site1.public.mxres.common.launcheraagent.mxres infos :"
    echo "         UID: mx3_uat9 PID: 83901 CPUTIME: 00:30:29 STIME: Feb12"
    echo " vm000003654.agent.vm000003654 infos :"
    echo "         UID: mx3_uat9 PID: 82429 CPUTIME: 00:38:53 STIME: Feb12"
    echo " vm000003654.alert.site1.public.mxres.common.launcheralert.mxres infos :"
    echo "         UID: mx3_uat9 PID: 85626 CPUTIME: 00:17:21 STIME: Feb12"
    echo " vm000003654.bos.site1.public.mxres.common.launcherbos.mxres infos :"
    echo "         UID: mx3_uat9 PID: 85023 CPUTIME: 01:09:36 STIME: Feb12"
else
    echo "Usage: ./launchmxj.app -s"
fi