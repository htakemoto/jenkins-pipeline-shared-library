#!/bin/groovy

/**
 * Description: This provides basic function
 *
 * Usage:
 *
 *   log.info 'Starting'
 *   log.warning 'Nothing to do!'
 *
 */

def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}
