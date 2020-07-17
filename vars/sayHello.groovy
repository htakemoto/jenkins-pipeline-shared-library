#!/bin/groovy

/**
 * Description: This provides basic function
 *
 * Usage:
 *
 *   sayHello 'Mom'
 *
 */

// call method tells Jenkins what method to run
// when the var is called from a Jenkinsfile
def call(String name = 'human') {
    echo "Hello, ${name}."
}
