# WatchHawthorneStereo

"I just want to jump into the code!" See: [Quickstart](#Quickstart)

Language: Scala 2.13

Runtime: Java 11

Web Framework: Play 2.8.X

Containerization: Docker

Deployment: Google Cloud Run

## Product Design

I want to know what's new on Hawthorne's website. Hawthorne posts listings in batches.
I want a digest emailed each day.

I am looking for product from a specific brand. I want to get a text for every item that matches that brand.

I am looking for a specific product. I want to get a text for every listing that contains a custom keyword.

## System Design

A "cloud scheduler" job (a cron job) runs 2x/day (midnight, noon).
Its only job is to trigger an endpoint in this web service.

### V1

The endpoint in this web service is basically a script. It:

1) Fetches the most recently stored previous listings from blob storage
2) Fetches the currently posted listings from hawthorne's rss feed
3) Stores the currently posted listings as a new blob
4) Diffs the previous and current listings
5) Sends an email of the diff to me directly

### V2

5) Posts the diff to a queue
6) Queue pushes the message to another endpoint in this service

The second endpoint is also a script. It:

1) Parses the diff into listings
2) Builds an email
3) Sends the email just to me

TODO: can i use the queue a lot more?

TODO:

- users (simple json in blob storage: email, name, brands, keywords)
- ui for subscribing to notifications

## Technologies Used

### Scala/sbt/Play

You must have a Java JDK installed on your machine. See [quickstart](#Quickstart) for instruction.

An installation of `sbt` is packaged with the repo so that build runners and docker images have access to a
distribution. But you can also rely on this as a developer by running commands like:

```shell
# clean the target folders, format all files, compile all subprojects, run all tests
./bin/sbt clean fmt compile test

# run
./bin/sbt run

# the above commands can also be run separately
./bin/sbt clean
./bin/sbt fmt
./bin/sbt compile
./bin/sbt test
./bin/sbt "testOnly *PublisherSpec"
```

You can also [install sbt](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html) locally on your machine, but
this is not required.

For help, you can take a look
at [Scala's official getting started docs](https://docs.scala-lang.org/getting-started/index.html).

After you're all setup, be sure to check out [Twitter's Scala School](https://twitter.github.io/scala_school/) if you've
never written Scala before.

### Docker

#### Intro

We use Docker to produce containers that Kubernetes can orchestrate. For this explained in an image,
see [containers](./documentation/k8s.svg).

#### Setup

[Get started with Docker](https://docs.docker.com/get-started/)

#### Useful Commands

```bash
# to inspect the filesytem of an image at rest 
docker run --rm -it --entrypoint=/bin/bash watch_hawthorne_stereo

# build from the repo root
docker build . --platform linux/amd64 --file ./src/docker/Dockerfile --tag watch_hawthorne_stereo:latest --build-arg SERVICE_VERSION="0.1.0-SNAPSHOT"
# add `--target build0` to stop at the build0 stage

# run
# setting a memory limit (to behave like it will on Google Cloud Run)
# setting environment variables
# you must have these environment variables set appropriately on the host machine
# setting 8080 on your machine mapped to 9000 on the container
docker run -m 256m -e PLAY_APPLICATION_SECRET -e SPOTIFY_CLIENT_SECRET -p 8080:9000 watch_hawthorne_stereo

# cleanup your system
docker system prune -a -f --volumes
```

### Skaffold

Skaffold allows developers to turn their laptops into CI/CD machines. It speeds up the local development cycle. It can
also deploy to remote servers, but we have TeamCity, `k8s-deploy`, and `argo-apps` for that.

- [Docs](https://skaffold.dev/docs/)
- [Quickstart](https://skaffold.dev/docs/quickstart/)

```shell
# stop at the build and push
skaffold build

# like a ci/cd pipeline would, build and deploy the app one time
skaffold run
```

### Google Cloud Run

Cloud Run can run arbitrary containers.

The app is
deployed [here](https://console.cloud.google.com/run/detail/us-west1/streaming-service-converter-3/revisions?project=four-track-friday-2) (
access is required to view this page).

# Quickstart

## Steps

1) Install [Git](https://git-scm.com/downloads)
2) Then clone this repository
3) Install [Homebrew](https://brew.sh/)
   ```shell
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
   ```
   OR ensure brew is up-to-date
   ```shell
   brew upgrade
   ```
4) Then install a JDK
   ```shell
   brew tap AdoptOpenJDK/openjdk
   brew install --cask adoptopenjdk11
   java -version
   # confirm the installed version is what you intended
   ```
   OR [install GraalVM on Mac](https://www.graalvm.org/docs/getting-started/macos/)
5) Install Docker Desktop
   1) [Intel Mac](https://desktop.docker.com/mac/main/amd64/Docker.dmg)
   2) [M Mac](https://desktop.docker.com/mac/main/arm64/Docker.dmg)
6) Ensure Docker Desktop is totally up-to-date! This may take several updates.
7) Run the following:
   ```shell
   skaffold build
   
   docker run -m 256m -e PLAY_APPLICATION_SECRET -e SPOTIFY_CLIENT_SECRET -p 8080:9000 watch_hawthorne_stereo
   ```
8) Or if you prefer to do the setup manually, see [that readme](./documentation/README_LOCAL_K8S.md)

## IntelliJ Tips

If you are seeing errors in build.sbt files regarding trailing commas, manually enable trailing commas for the project
in `Preferences | Languages & Frameworks | Scala | Misc | Trailing commas`

You will need to restart Intellij for this change to take effect
