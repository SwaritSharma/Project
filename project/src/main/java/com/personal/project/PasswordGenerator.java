package com.personal.project;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

        public static void main(String[] args) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                String userPassword = encoder.encode(
                                "User@123");

                String vendorPassword = encoder.encode(
                                "Vendor@123");

                System.out.println(
                                "User Password Hash:");

                System.out.println(
                                userPassword);

                System.out.println(
                                "Vendor Password Hash:");

                System.out.println(
                                vendorPassword);
        }
}