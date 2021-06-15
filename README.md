# BLESS
BLESS is a set of Android libraries that makes work with Android BLE framework easier.

## Documentation
* [Getting Started](./docs/README.md)
* [Documentation References](./docs/reference.md)

## Reporting Issues
Found a bug on a specific feature? Open an issue on [Github issues](https://github.com/troido/bless/issues).

# Setup

## Current Version
```Gradle
// latest snapshot
def bless_version = '0.1.0-SNAPSHOT'
```

## Github Credentials
You need to have the following credentials:
* Github username
* Github personal access token with `read:packages` scope
    * You can generate your Github's personal access token at: Github Profile -> Settings -> Developer settings -> Personal access tokens -> Generate new token
    * Don't forget to check `read:packages` scope when generating the token

## Gradle

### Github Repository
Check that you have the BLESS Github repository in the list of your repositories.
```Gradle
// Add BLESS Github Repository
repositories {
    maven {
        url "https://maven.pkg.github.com/troido/bless"
        credentials {
            username = GITHUB_USERNAME
            password = GITHUB_READ_PACKAGES_ACCESS_TOKEN
        }
    }
}
```
*Note: You can setup your credentials as Gradle environment variables.*

### Dependencies
```Gradle
// BLESS core features
implementation "com.troido.bless:bless-core:$bless_version"
// BLESS UI components
implementation "com.troido.bless:bless-ui:$bless_version"
```
