package org.example;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.rxjava3.core.Single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkNWRmNTY4Yi04MmFhLTRlY2QtYjU4Ny05ODVmZWZmMjAwMmIiLCJuYW1laWQiOlsiZDVkZjU2OGItODJhYS00ZWNkLWI1ODctOTg1ZmVmZjIwMDJiIiwiZDVkZjU2OGItODJhYS00ZWNkLWI1ODctOTg1ZmVmZjIwMDJiIl0sIm5hbWUiOiJUdXJnYXkiLCJlbWFpbCI6InR1cmdheS1wYWNrYWdlLWNvYWNoQGdtYWlsLmNvbSIsImp0aSI6ImU3YTBlZjQ1LTMyNzktNDM5Ni04NTIzLWRkYjZlNDAxMjc5ZiIsInJvbGUiOiJTdHVkZW50IiwibmJmIjoxNzIwODc4NTMwLCJleHAiOjE3MjM0NzA1MzAsImlhdCI6MTcyMDg3ODUzMCwiaXNzIjoiYmF5a3VzIiwiYXVkIjoiYmF5a3VzIn0.LWoo6lHgDg1PaIjBHjmnn6SK289pmXqRuF-ohggMYmM";
    private static final String URL = "https://api.baykusmentorluk.com/chatHub";
    private static final String RECEIVER_ID = "00000000-0000-0000-0000-000000000018";

    public static void main(String[] args) {
        HubConnection hubConnection = HubConnectionBuilder.create(URL)
                .withAccessTokenProvider(Single.defer(() -> {
                    // Your logic here.
                    return Single.just(TOKEN);
                })).build();

        hubConnection.start().blockingAwait();

        hubConnection.on("ReceiveMessage", (senderId, receiverId, message, timestamp) -> {
                    if(receiverId.equals("00000000-0000-0000-0000-000000000018")){
                        System.out.println("coach: " + message);
                    } else {
                        System.out.println("student: " + message);
                    }
                },
                String.class,
                String.class,
                String.class,
                String.class
        );

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        System.out.println("****** Send message to coach ******");
        do {
            try {
                input = reader.readLine();
                hubConnection.invoke("SendMessage", RECEIVER_ID, input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!input.equals("exit"));
    }
}