#!/bin/bash
# Rultor release versioning script for Maven projects.
#
#
# It looks for the project’s version, which MUST respect the pattern 
# [0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT and BE THE FIRST MATCH in pom.xml
#
# What it does: updates the pom.xml version of the project according to
# the variable ${tag} provided to rultor. Specifically, it increments the 
# 3rd digit and adds '-SNAPSHOT' to it.
#
#
# IMPORTANT:
#     the given tag has to contain 3 numbers separated by dots!
#     
#     e.g. tag = 1.0.1 or tag = 3.2.53 will result in new versions of 1.0.2-SNAPSHOT
#     or 3.2.54-SNAPSHOT


[[ "${tag}" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || exit -1

mvn -ntp versions:set "-DnewVersion=${tag}"
sed -i "s/, version \`.*\`/, version \`${tag}\`/" README.md
sed -i "s/(\`\`queen-of-java-[0-9]*\.[0-9]*\.[0-9]*.jar\`\`)/(\`\`queen-of-java-${tag}.jar\`\`)/" README.md
git commit -am "${tag}"

mvn clean deploy -Pitcases,signArtifactsGpg,releaseToGithubPackages --settings /home/r/settings.xml

