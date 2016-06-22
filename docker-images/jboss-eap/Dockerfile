# Use latest jboss/base-jdk:8 image as the base
FROM jboss/base-jdk:8

# Set the EAP_VERSION env variable
ENV EAP_VERSION 7.0.0
ENV EAP_SHA256 7c28c74e475a296b6e3370b5eb2e21f158c31f4b50d56dafadada6483b23340e
ENV JBOSS_HOME /opt/jboss/eap

USER root
# Add the EAP distribution to /opt, and make eap the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
COPY support/jboss*.zip $HOME
COPY docker-entrypoint.sh $HOME
RUN chown -R jboss.jboss $HOME

USER jboss

RUN cd $HOME \
    && sha256sum jboss-eap-$EAP_VERSION.zip | grep $EAP_SHA256 \
    && unzip -q jboss-eap-$EAP_VERSION.zip \
    && mv $HOME/jboss-eap-7.0 $JBOSS_HOME \
    && rm jboss-eap-$EAP_VERSION.zip

RUN ls -la $HOME;pwd

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Expose the ports we're interested in
EXPOSE 8080 9990

# Set the default command to run on boot
# This will boot EAP in the standalone mode and bind to all interface
#CMD ["/opt/jboss/eap/bin/standalone.sh", "-b", "0.0.0.0"]
ENTRYPOINT [ "/opt/jboss/docker-entrypoint.sh" ]

CMD ["-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

