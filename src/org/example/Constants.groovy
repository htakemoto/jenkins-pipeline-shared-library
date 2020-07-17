#!/usr/bin/env groovy

package org.example

class Constants {
   static final String SLACK_MESSAGE = "Sending Slack Notification"
   static final String EMAIL_MESSAGE = "Sending Email"

   // refer to this in a pipeline using:
   //
   // import org.example.Constants
   // println Constants.EMAIL_MESSAGE
}