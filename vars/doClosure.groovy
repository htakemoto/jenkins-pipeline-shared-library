#!/bin/groovy

/**
 * Description: This provides basic function with closure
 *
 * Usage:
 *
 *   doClosure deploy: true
 *   doClosure deploy: true {
 *       stage("extra step") { ... }
 *   }
 *
 */

def call(Map config=[:], Closure body) {
    if (config.deploy) {
        echo "You picked deploy"
    } else {
        echo "You picked non deploy"
    }
    body()
}
