#!/bin/groovy

/**
 * Description: This provides basic function
 *
 * Usage:
 *
 *   runShell()
 *
 */

def call() {
    def myShell = libraryResource 'org/example/my_shell.sh' 
    return sh(myShell) 
}
