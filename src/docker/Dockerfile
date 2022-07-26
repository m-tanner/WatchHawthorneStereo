FROM amazoncorretto:11-alpine3.14-jdk as corretto

FROM corretto as build0

# we need this to run the sbt scripts later
RUN ["apk", "add", "--no-cache", "bash"]

FROM build0 as build1

# we need this to unzip later
RUN ["apk", "add", "--no-cache", "unzip"]

FROM build1 as build2

ARG SERVICE_VERSION

# copy the file from the machine with the source code into the container
COPY . /watch_hawthorne_stereo
# cd into the "copied to" directory
WORKDIR /watch_hawthorne_stereo

# create a distribution independent of sbt
RUN ["./bin/sbt", "clean", "dist"]

# cd into the distribution directory
WORKDIR /watch_hawthorne_stereo/target/universal
# unzip the files for the production build to use below
RUN unzip watch_hawthorne_stereo-$SERVICE_VERSION.zip
# get rid of the version identifier
RUN mv watch_hawthorne_stereo-$SERVICE_VERSION watch_hawthorne_stereo

FROM build0

# Add apex user and group
RUN addgroup -S serviceGroup && adduser -S serviceUser -G serviceGroup
# Change to non-root privilege
USER serviceUser

# copy the unzipped files from above into the production container
COPY --from=build2 /watch_hawthorne_stereo/target/universal /opt

EXPOSE 9000

# set running the service as the entrypoint
ENTRYPOINT ["/bin/bash", "/opt/watch_hawthorne_stereo/bin/watch_hawthorne_stereo"]
# but allow for runtime commands to overwrite the arguments
CMD ["-Dconfig.file=/opt/watch_hawthorne_stereo/conf/application.conf"]
