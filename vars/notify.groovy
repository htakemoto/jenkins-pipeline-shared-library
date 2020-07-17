#!/bin/groovy

import org.example.Constants

/**
 * Description: This provides basic function
 *
 * Usage:
 *
 *   notify type: 'slack', message: 'a slack notification'
 *   notify type: 'email', message: 'a email notification'
 *
 */

def call(Map config=[:]) {
    if (config.type == "slack") {
        echo Constants.SLACK_MESSAGE
        echo config.message
    } else {
        echo Constants.EMAIL_MESSAGE
        echo config.message
    }
}