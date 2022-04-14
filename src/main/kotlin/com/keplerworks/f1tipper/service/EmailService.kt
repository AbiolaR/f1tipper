package com.keplerworks.f1tipper.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
   fun sendChangePasswordMail(username: String, encryptedPassword: String) {
       val message = SimpleMailMessage()

       message.setFrom("noreply@f1tipper.support")
       message.setTo("abiola.rasheed@gmail.com")
       message.setSubject("Change Password")
       message.setText("User $username wants to change their password to: $encryptedPassword")

       mailSender.send(message)
   }
}