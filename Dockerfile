FROM jenkins/jenkins:lts

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

RUN jenkins-plugin-cli --plugins \
    cloudbees-folder \
    configuration-as-code \
    credentials \
    github \
    instance-identity \
    job-dsl \
    script-security \
    structs \
    role-strategy \
    ws-cleanup

USER root
RUN mkdir -p /usr/share/jenkins/ref/job_dsl.groovy.d/
COPY job_dsl.groovy /usr/share/jenkins/ref/job_dsl.groovy.d/job_dsl.groovy
USER jenkins