package com.example.urlshorteningservice.runner;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@AllArgsConstructor
@Component
public class UrlRunner implements CommandLineRunner {
    private UrlService service;

    @Override
    public void run (String... args) {
        Scanner input = new Scanner (System.in);
        System.out.println ("URL Shortening Service!");

        while (true) {
            System.out.println ("\nCommands: create, get, count, delete, exit");
            System.out.print ("> ");
            String command = input.nextLine ();

            if (command.equals ("create")) {
                System.out.print ("Enter the URL: ");
                String url = input.nextLine ();

                Url urlObject = this.service.createUrl (url);
                System.out.println ("Short code: " + urlObject.getShortCode ());
            }

            else if (command.equals ("get")) {
                System.out.print ("Enter the short code: ");
                String shortCode = input.nextLine ();

                this.service.getUrlByShortCode (shortCode).ifPresentOrElse
                        (u -> {
                            this.service.incrementAccessCount (u);
                            System.out.println ("Original URL: " + u.getUrl ());
                        },
                        () -> System.out.println ("Not found"));
            }

            else if (command.equals ("count")) {
                System.out.print ("Enter the short code: ");
                String shortCode = input.nextLine ();

                try {
                    System.out.println ("Access count: " + this.service.getAccessCount (shortCode));
                } catch (Exception e) {
                    System.out.println ("Not found");
                }
            }

            else if (command.equals ("delete")) {
                System.out.print ("Enter the short code: ");
                String shortCode = input.nextLine ();

                try {
                    this.service.deleteUrl (shortCode);
                    System.out.println ("Deleted");
                } catch (Exception e) {
                    System.out.println ("Not found");
                }
            }

            else if (command.equals ("exit")) {
                System.out.println ("Thank you for using the program");
                break;
            }

            else {
                System.out.println ("Invalid command");
            }
        }

        System.exit (0);
    }
}
