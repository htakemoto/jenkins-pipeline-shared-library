# Jenkins Pipeline Shared Library

Demonstrates how to use a Shared Library in Jenkins pipelines.

Ref: https://www.jenkins.io/doc/book/pipeline/shared-libraries/

## Context

### Terms

- **Jenkins Pipeline**: eliminates the needs of Jenkins config on Jenkins console. The config will be handled by `Jenkinsfile` which typically sits on root directory of each project
- **Jenkins Pipeline Shared Library**: provides centralized config across all projects to eliminate repepetive configs from each `Jenkinsfile` << This project is a sample library of this

### Syntax Comparison

Jenkins pipeline provides two different syntax:

- **Scripted Pipeline**: offers a tremendous amount of flexibility and extensibility. (Scripted Pipeline typically starts with `node {}`)
- **Declarative Pipeline**: offer a simpler and more opinionated syntax. (Declarative Pipeline typically starts with `pipeline {}`)

#### Scripted Pipeline Syntax

```groovy
// variable
def message = "Hello World!"

// node
node {
    // stage
    stage("stage 1") {
        // print
        echo "${message}"
    }

    stage("stage 2") {
        try {
            // throw error
            error "my exception"
        } catch (e) {
            // show object as string
            echo "${e}"
        } finally {
            // send message email or slack 
            echo "final message"
        }
    }

    stage("stage 3") {
        // execute command
        // sh() returns 0 when success, return other thing otherwise.
        def result = sh (
            script: "echo \"${message}\"",
            returnStatus: true
        ) == 0

        if(result) {
            echo "command was success"
        } else {
            echo "command failed"
        }
    }

    stage("stage 4") {
        if(fileExists("index.js")) {
            echo "index.js exists!"
        }
    }

}
```

#### Declarative Pipeline Syntax

```groovy
pipeline {
    agent any
    parameters {
        string(name: 'MESSAGE', defaultValue: 'Hello World!', description: 'First message')
    }
    stages { 
        stage('stage 1') {
            steps {
                echo "${params.MESSAGE}"
                sh 'pwd'
            }
        }
    }
}
```

### Jenkins Pipeline Shared Library Directory Structure

This is a sample project structure of Jenkins Library from official document (not this project itself)

```
(root)
+- src                     # Groovy source files
|   +- org
|       +- foo
|           +- Bar.groovy  # for org.foo.Bar class
+- vars
|   +- foo.groovy          # for global 'foo' variable
|   +- foo.txt             # help for 'foo' variable
+- resources               # resource files (external libraries only)
|   +- org
|       +- foo
|           +- bar.json    # static helper data for org.foo.Bar
```

## How to Use This Sample Library

Note: You do not need to clone this repository to achieve the following:

1. Prepare Jenkins

    Option1: Standalone Jenkins

    ```bash
    java -jar jenkins.war --httpPort=8080
    ```

    Option2: Docker

    ```bash
    docker run \
        -u root \
        --rm \
        -d \
        -p 8080:8080 \
        -v jenkins_home:/var/jenkins_home \
        -v /var/run/docker.sock:/var/run/docker.sock \
        jenkinsci/blueocean
    ```

2. Access to your Jenkins, go to **Manage Jenkins** >> **Configure System**. Under **Global Pipeline Libraries**, add a library with the following settings:

    - Name: `pipeline-library-demo`
    - Default version: Specify a Git reference (branch or commit SHA), e.g. `master`
    - Retrieval method: _Modern SCM_
    - Select the _Git_ type
    - Project repository: `https://github.com/htakemoto/jenkins-pipeline-shared-library.git`
    - Credentials: (leave blank)

3. Then create a Jenkins job with the following pipeline (note that the underscore `_` is not a typo):

    ```groovy
    @Library('pipeline-library-demo') _

    stage('Demo') {
        echo 'Hello World'
        sayHello 'Mom'
        log.info 'Starting'
        log.warning 'Nothing to do!'
    }
    ```

    This will output the following from the build:

    ```
    [Pipeline] stage
    [Pipeline] { (Demo)
    [Pipeline] echo
    Hello world
    [Pipeline] echo
    Hello, Mom.
    [Pipeline] echo
    INFO: Starting
    [Pipeline] echo
    WARNING: Nothing to do!
    [Pipeline] }
    [Pipeline] // stage
    [Pipeline] End of Pipeline
    Finished: SUCCESS
    ```

4. Do more with `agent any`:

    ```groovy
    @Library('pipeline-library-demo') _

    pipeline {
        agent any
        stages {
            stage('Demo') {
                steps {
                    echo 'Hello World'
                    sayHello 'Mom'
                    // required to be in script for non call() methods
                    script {
                        log.info 'Starting'
                        log.warning 'Nothing to do!'
                        runShell()
                    }
                }
            }
            
            stage('Notify') {
                steps {
                    notify type: 'slack', message: 'a slack notification'
                }
            }
            
            stage('Notify with Closure') {
                steps {
                    doClosure deploy: false, {
                        notify type: 'email', message: 'a email notification'
                    }
                }
            }
        }
    }
    ```

5. Execute class

    ```groovy
    @Library('pipeline-library-demo')
    import org.example.Mock
    def mock = new Mock(this);
    node() {
        mock.checkout()
        mock.compile()
        mock.deploy()
        mock.test()
    }
    ```

    or

    ```groovy
    @Library('pipeline-library-demo')
    def mock = new org.example.Mock(this);
    node() {
        mock.checkout()
        mock.compile()
        mock.deploy()
        mock.test()
    }
    ```

## Tips

On `vars/sayHello.groovy`, we have the `call()` method. The `call()` method allows the global variable to be invoked in a manner similar to a step: such as `sh` or `git`. You can still call a specific method from `/vars/*`, but it requires to be `script` closure on **Declarative Pipeline**.
