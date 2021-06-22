---
layout: default
title: Setup
nav_order: 4
---
# Setup

## Current Version
```gradle
def bless_version = '0.1.0'
```

## Gradle

### Maven Repository
Check that you have the BLESS artifactory repository in the list of your repositories.
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        // Add BLESS Repository
        maven{
            url "https://troido.jfrog.io/artifactory/bless-libs-release"
        }
}
```

### Dependencies
In your app module, declare corresponding dependencies
```gradle
// BLESS core features
implementation "com.troido.bless:bless-core:$bless_version"
// BLESS UI components
implementation "com.troido.bless:bless-ui:$bless_version"
// BLESS aconno components
implementation "com.troido.bless:bless-aconno:$bless_version"
```

For further documentation take a look at [Documentation References](./reference.md).
