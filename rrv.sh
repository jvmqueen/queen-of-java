#!/bin/bash
# Rultor release versioning script for Maven projects.
#
# What it does: updates the pom.xml version of the project according to
# the variable ${tag} provided to rultor. Specifically, it increments the 
# 3rd digit and adds '-SNAPSHOT' to it.
# 
# Also updates various places in the README file where the latest version should
# be specified.
#
# IMPORTANT:
#     the given tag has to contain 3 numbers separated by dots!
#     
#     e.g. tag = 1.0.1 or tag = 3.2.53 will result in new iteration version of 1.0.2-SNAPSHOT
#     or 3.2.54-SNAPSHOT respectively.


[[ "${tag}" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || exit -1

# Set release verion everywhere
git checkout master
sed -i "s/, version \`.*\`/, version \`${tag}\`/" README.md
sed -i "s/(\`\`queen-of-java-[0-9]*\.[0-9]*\.[0-9]*.jar\`\`)/(\`\`queen-of-java-${tag}.jar\`\`)/" README.md
mvn -ntp versions:set "-DnewVersion=${tag}"
rm pom.xml.versionsBackup
mvn clean deploy -Pitcases,signArtifactsGpg,releaseToGithubPackages --settings /home/r/settings.xml

git commit -am "${tag}" # release commit

git checkout __rultor
git rebase master # rebase the __rultor Branch on master, so Rultor tags and releaseas the release commit.

git checkout master # check out master again and set the next -SNAPSHOT version

NUMBERS=($(echo $tag | grep -o -E '[0-9]+'))
NEXT_DEV_VERSION=${NUMBERS[0]}'.'${NUMBERS[1]}'.'$((${NUMBERS[2]}+1))'-SNAPSHOT'
mvn -ntp versions:set "-DnewVersion=${NEXT_DEV_VERSION}"
rm pom.xml.versionsBackup

git commit -am "${NEXT_DEV_VERSION}" # "next iteration commit", will not be part of the tag or release

git checkout __rultor # Rultor continues the release from its branch. At the end, it will push all the branches, so the above 2 commits will be visible in master commit-history
