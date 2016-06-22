#!/bin/bash

if [ $EAP_USER ] && [ $EAP_PASSWORD ]; then
    /opt/jboss/eap/bin/add-user.sh $EAP_USER $EAP_PASSWORD --silent
fi

exec /opt/jboss/eap/bin/standalone.sh $@
exit $?
