#!/usr/bin/env bash

set -x -e

VERSION=`cat ./VERSION`
git tag -a $VERSION -m $VERSION
git push origin $VERSION
